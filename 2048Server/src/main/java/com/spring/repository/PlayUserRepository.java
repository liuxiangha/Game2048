package com.spring.repository;

import com.spring.entity.PlayUser;
import org.springframework.data.repository.CrudRepository;

public interface PlayUserRepository extends CrudRepository<PlayUser, Long> {
    PlayUser getPlayUserByUserIdAndUserPassword(Long playUserId, String playUserPassword);
    PlayUser getPlayUserNameByUserId(Long userId);
    PlayUser getPlayUserByUserId(Long userId);
}
