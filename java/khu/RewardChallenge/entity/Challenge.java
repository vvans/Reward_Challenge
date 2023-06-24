package khu.RewardChallenge.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challenge_id;

    private String challengename;
    private String startdate;
    private String finishdate;
    private String frequency;
    private String penalty;
    private String period;
    private String times;
    private String rate;
}
