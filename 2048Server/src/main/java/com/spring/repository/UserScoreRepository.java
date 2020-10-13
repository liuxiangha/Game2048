package com.spring.repository;

import com.spring.entity.UserScore;
import org.springframework.data.repository.CrudRepository;

public interface UserScoreRepository extends CrudRepository<UserScore, Long> {
}
