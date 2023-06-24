package khu.RewardChallenge.repository;

import khu.RewardChallenge.entity.OnlyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OnlyChallengeRepository extends JpaRepository<OnlyChallenge, Long> {
    @Query("select oh.challenge_id from OnlyChallenge oh where oh.username= :username")
    List<Long> findChallenge_idByUsername(@Param("username") String username);

    @Query("select oh.username from OnlyChallenge oh where oh.challenge_id= :challenge_id")
    String findUsernameBychallenge_id(@Param("challenge_id") Long challenge_id);
}