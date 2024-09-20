package com.govtech.champions.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String registrationDate;
    private int groupNumber;

    private int goalsScored;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int totalMatchPoints;
    private int alternateMatchPoints;

    @OneToMany(mappedBy = "teamA", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Match> matchesAsTeamA;

    @OneToMany(mappedBy = "teamB", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Match> matchesAsTeamB;

    public Team(String name, String registrationDate, int groupNumber) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.groupNumber = groupNumber;
    }
}