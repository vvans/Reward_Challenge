package khu.RewardChallenge.repository;

import khu.RewardChallenge.entity.Challenge;
import khu.RewardChallenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    @Query("select c from Challenge c where c.challenge_id= :challenge_id")
    Challenge findByChallenge_id(@Param("challenge_id") Long challenge_id);

    @Query("select c.challengename from Challenge c where c.challenge_id= :challenge_id")
    String findChallengenameBychallengeid(@Param("challenge_id") Long challenge_id);
    @Query("select c.penalty from Challenge c where c.challenge_id= :challenge_id")
    String findPenaltyByChallenge_id(@Param("challenge_id") Long challenge_id);

}
