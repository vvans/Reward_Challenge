package khu.RewardChallenge.repository;

import khu.RewardChallenge.entity.Penalty;
import khu.RewardChallenge.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select p.filepath from Photo p where p.challenge_id= :challenge_id")
    List<String> findFilepathById(@Param("challenge_id") Long challenge_id);

    @Query("select p.photo_date from Photo p where p.challenge_id= :challenge_id")
    List<String> findPhotodateById(@Param("challenge_id") Long challenge_id);
}
