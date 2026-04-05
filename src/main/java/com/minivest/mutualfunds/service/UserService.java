package com.minivest.mutualfunds.service;
import com.minivest.mutualfunds.dto.RegisterUserRequest;

public interface UserService {
   public void createUser(RegisterUserRequest req);
   public void getUser(Long userId);
   public void getBankFromIfsc(String ifsc);
}
