package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.betting.CasinoBettingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasinoBettingHistoryRepository extends JpaRepository<CasinoBettingHistory, Long> {
}
