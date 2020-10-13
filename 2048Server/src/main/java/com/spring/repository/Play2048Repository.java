package com.spring.repository;

import com.spring.entity.Play2048;
import com.spring.entity.Play2048IdClass;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface Play2048Repository extends CrudRepository<Play2048, Play2048IdClass> {
    @Query(value = "SELECT e FROM Play2048 e ORDER BY e.score DESC ")
    List<Play2048> getPlay2048sByUserIdOrderByScore(Long userId);
}
