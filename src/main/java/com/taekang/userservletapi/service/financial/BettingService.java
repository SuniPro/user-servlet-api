package com.taekang.userservletapi.service.financial;

import com.taekang.userservletapi.entity.User;
import com.taekang.userservletapi.entity.betting.CasinoBettingHistory;
import com.taekang.userservletapi.entity.betting.SportsBettingHistory;
import com.taekang.userservletapi.repository.CasinoBettingHistoryRepository;
import com.taekang.userservletapi.repository.SportsBettingHistoryRepository;
import com.taekang.userservletapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public abstract class BettingService implements FinancialService{

    private final UserRepository userRepository;
    private final SportsBettingHistoryRepository sportsBettingHistoryRepository;
    private final CasinoBettingHistoryRepository casinoBettingHistoryRepository;

    @Autowired
    protected BettingService(UserRepository userRepository, SportsBettingHistoryRepository sportsBettingHistoryRepository, CasinoBettingHistoryRepository casinoBettingHistoryRepository) {
        this.userRepository = userRepository;
        this.sportsBettingHistoryRepository = sportsBettingHistoryRepository;
        this.casinoBettingHistoryRepository = casinoBettingHistoryRepository;
    }

    @Override
    public void betOnSports(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.betOnSports(amount);
        userRepository.save(user);

        SportsBettingHistory history = new SportsBettingHistory(user, amount);
        sportsBettingHistoryRepository.save(history);
    }

    @Override
    public void betOnCasino(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.betOnCasino(amount);
        userRepository.save(user);

        CasinoBettingHistory history = new CasinoBettingHistory(user, amount);
        casinoBettingHistoryRepository.save(history);
    }
}
