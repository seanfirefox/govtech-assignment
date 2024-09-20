package com.govtech.champions.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    private int goalsA;
    private int goalsB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id")
    @JsonBackReference
    private Team teamA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id")
    @JsonBackReference
    private Team teamB;

    public Match(String teamAName, String teamBName, int goalsA, int goalsB) {
        this.teamAName = teamAName;
        this.teamBName = teamBName;
        this.goalsA = goalsA;
        this.goalsB = goalsB;
    }
}

