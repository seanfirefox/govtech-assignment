package com.govtech.champions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
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
}
