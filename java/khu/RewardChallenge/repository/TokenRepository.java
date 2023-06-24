package khu.RewardChallenge.repository;

import khu.RewardChallenge.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);

}
