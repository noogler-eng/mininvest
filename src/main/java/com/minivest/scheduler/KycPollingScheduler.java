package com.minivest.scheduler;

import java.util.List;
import java.util.Map;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.minivest.mutualfunds.entity.User;
import com.minivest.mutualfunds.enums.KycStatus;
import com.minivest.mutualfunds.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KycPollingScheduler {
    // This scheduler polls the KYC simulator for investors whose KYC is UNDERPROCESS.
    // In production, this polls NDML/CVL APIs every hour.

    private final UserRepo userDao;
    // RestTemplate = Spring's HTTP client for calling external APIs.
    // In production, use WebClient (reactive) or RestClient (modern).
    private final RestTemplate restTemplate = new RestTemplate();

    public KycPollingScheduler(UserRepo userDao){
        this.userDao = userDao;
    }

    // runs every minute
    // Different from fixedRate: fixedRate starts new run even if previous is still running.
    // fixedDelay is SAFER for jobs that might take variable time.
    @Scheduled(fixedDelay = 60 * 1000) 
    public void pollKycStatus(){
        log.info("Polling KYC status...");

        // 1. fetch all the unverified users whose KYC is under process
        List<User> users = userDao.findAll().stream()
                .filter(user -> user.getKycStatus() == KycStatus.UNDER_PROCESS)
                .toList();

        // 2. for each user calling NDML / CVL api to get the latest KYC status and update in database
        for(User user: users){
            log.info("Polling KYC status for user: {}", user.getEmail());
            KycStatus newKycStatus = pollKycStatusForUser(user);
            if(newKycStatus != user.getKycStatus()){
                user.setKycStatus(newKycStatus);
                userDao.save(user);
            }
        }
    }

    // Retry Rules:
    // 1. network timeout, server error (5xx). Don't retry validation errors (4xx) or business logic failures.
    // 2. exponential or at least fixed delay. Never retry immediately.
    // 3. infinite retries = infinite loop = thread exhaustion = app crash.
    @Retryable(
        retryFor = Exception.class, // retry on any exception
        maxAttempts = 3, // try a maximum of 3 times
        // Exponential backoff: 1st retry after 1s, 2nd after 2s, 3rd after 4s.
        // wait 1 seconds before
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public KycStatus pollKycStatusForUser(User user){
        try{
            // call NDML / CVL api to get the latest KYC status for the user
            String url = "http://localhost:8080/mock/kyc/status/" + user.getId();

            // java dont know about incommming response, 
            // so we use Map to store the response in key-value format.
            // @SuppressWarnings("unchecked") is used to suppress the warning that arises due to type 
            // erasure in Java generics when we cast the response to a Map.
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            KycStatus status = (KycStatus) response.get("status");

            return status;
        }catch(Exception e){
            log.error("Error while polling KYC status for user: {}", user.getEmail(), e);
            return KycStatus.UNDER_PROCESS; 
        }
    }

    @Recover
    // if all retries fail, we return UNDER_PROCESS to try again in the next scheduled run.
    public KycStatus recover(Exception e, User user){
        log.error("All retries failed for user: {}. Marking KYC status as UNDER_PROCESS", user.getEmail(), e);
        return KycStatus.UNDER_PROCESS; 
    }
}