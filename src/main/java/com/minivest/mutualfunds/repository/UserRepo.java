package com.minivest.mutualfunds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.minivest.mutualfunds.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {}
