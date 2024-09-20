package com.govtech.champions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchDTO {
    private Long id;
    private String teamAName;
    private String teamBName;
    private int goalsA;
    private int goalsB;
}
