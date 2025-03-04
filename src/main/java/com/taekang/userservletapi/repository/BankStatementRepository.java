package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {}
