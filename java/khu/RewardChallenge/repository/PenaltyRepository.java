package khu.RewardChallenge.repository;

import khu.RewardChallenge.entity.Challenge;
import khu.RewardChallenge.entity.Penalty;
import khu.RewardChallenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    @Query("select p from Penalty p where p.challenge_id= :challenge_id")
    Penalty findByChallenge_id(@Param("challenge_id") Long challenge_id);
    @Query("select p from Penalty p where p.id= :id")
    Penalty findByID(@Param("id") Long id);

    @Query("select p.penalty from Penalty p where p.challenge_id= :challenge_id")
    List<String> findpenaltyByChallenge_id(@Param("challenge_id") Long challenge_id);

    @Query("select p.penalty from Penalty p where p.challenge_id= :challenge_id and p.status= :status")
    List<String> findpenaltyByChallenge_idandstatus(@Param("challenge_id") Long challenge_id,@Param("status") String status);
    @Query("select p.status from Penalty p where p.id= :id")
    String findStatusByid(@Param("id") Long id);

    @Query("select p.penalty from Penalty p where p.id= :id")
    String findPenaltyByid(@Param("id") Long id);
    @Query("select p.id from Penalty p where p.penalty= :penalty and p.challenge_id= :challenge_id")
    Long findIdByPenalty(@Param("penalty") String penalty,@Param("challenge_id") Long challenge_id);
    @Query("select p.id from Penalty p where p.challenge_id= :challenge_id and p.friend_challenge_id= :friend_challenge_id")
    List<Long> findidByChallenge_idandFriend_challenge_id(@Param("challenge_id") Long challenge_id,@Param("friend_challenge_id") Long friend_challenge_id);
}
