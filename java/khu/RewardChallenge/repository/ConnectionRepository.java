package khu.RewardChallenge.repository;


import khu.RewardChallenge.entity.Connection;
import khu.RewardChallenge.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    @Query("select c from Connection c where c.id= :id")
    Connection findByID(@Param("id") Long id);

    @Query("select c from Connection c where c.challenge_id= :challenge_id and c.status=:status")
    Connection findByChallenge_idstatus(@Param("challenge_id") Long challenge_id, @Param("status") String status);
    @Query("select c.challenge_id from Connection c where c.id= :id")
    Long findChallenge_idById(@Param("id") Long id);
    @Query("select c.friend_challenge_id from Connection c where c.id= :id")
    Long findFrinend_Challenge_idById(@Param("id") Long id);


    @Query("select c.id from Connection c where c.challenge_id= :challenge_id")
    List<Long> findIdByChallenge_id(@Param("challenge_id") Long challenge_id);

    @Query("select c.id from Connection c where c.challenge_id= :challenge_id and c.friend_challenge_id= :friend_challenge_id")
    Long find_f_connection_id(@Param("challenge_id") Long challenge_id,@Param("friend_challenge_id") Long friend_challenge_id);

}
