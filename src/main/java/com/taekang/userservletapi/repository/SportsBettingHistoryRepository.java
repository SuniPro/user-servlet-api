package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.betting.SportsBettingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsBettingHistoryRepository extends JpaRepository<SportsBettingHistory, Long> {
}
