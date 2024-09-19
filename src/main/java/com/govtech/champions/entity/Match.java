package com.govtech.champions.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamAName;
    private String teamBName;
    private int teamAGoals;
    private int teamBGoals;

    public Match(String teamAName, String teamBName, int teamAGoals, int teamBGoals) {
        this.teamAName = teamAName;
        this.teamBName = teamBName;
        this.teamAGoals = teamAGoals;
        this.teamBGoals = teamBGoals;
    }

}

