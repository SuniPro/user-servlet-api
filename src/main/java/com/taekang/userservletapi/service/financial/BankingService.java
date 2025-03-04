package com.taekang.userservletapi.service.financial;

import com.taekang.userservletapi.entity.BankStatement;
import com.taekang.userservletapi.entity.User;
import com.taekang.userservletapi.repository.BankStatementRepository;
import com.taekang.userservletapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public abstract class BankingService implements FinancialService{

    private final UserRepository userRepository;
    private final BankStatementRepository bankStatementRepository;

    @Autowired
    protected BankingService(UserRepository userRepository, BankStatementRepository bankStatementRepository) {
        this.userRepository = userRepository;
        this.bankStatementRepository = bankStatementRepository;
    }

    @Override
    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.deposit(amount);
        userRepository.save(user);

        BankStatement statement = new BankStatement(user, amount, BigDecimal.ZERO);
        bankStatementRepository.save(statement);
    }

    @Override
    public void withDraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.withdraw(amount);
        userRepository.save(user);

        BankStatement statement = new BankStatement(user, BigDecimal.ZERO, amount);
        bankStatementRepository.save(statement);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getMoney();
    }
}
