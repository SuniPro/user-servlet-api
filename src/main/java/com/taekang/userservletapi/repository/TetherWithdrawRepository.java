package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.TetherWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TetherWithdrawRepository extends JpaRepository<TetherWithdraw, Long> {}
