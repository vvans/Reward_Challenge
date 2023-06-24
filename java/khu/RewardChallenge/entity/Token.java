package khu.RewardChallenge.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Token {

    @Id
    private String token;
    private String status;
}
