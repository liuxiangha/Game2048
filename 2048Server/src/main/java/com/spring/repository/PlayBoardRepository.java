package com.spring.repository;

import com.spring.entity.PlayBoard;
import com.spring.entity.PlayBoardIdClass;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayBoardRepository extends CrudRepository<PlayBoard, PlayBoardIdClass> {
    @Query(value = "SELECT e FROM PlayBoard e ORDER BY e.takeTime ASC ")
    List<PlayBoard> getPlay2048sByUserIdOrderByScore(Long userId);
}
