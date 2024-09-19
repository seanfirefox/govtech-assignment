package com.govtech.champions.entity;

import com.govtech.champions.utils.MonthDayAttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Team {
    @Id
    private String name;
    @Convert(converter = MonthDayAttributeConverter.class)
    private MonthDay registrationDate;
    private int groupNumber;

    private int totalMatchPoints = 0;
    private int goalsScored = 0;
    private int alternateMatchPoints = 0;
    private int matchesPlayed = 0;
    private int wins = 0;
    private int draws = 0;
    private int losses = 0;

    public Team(String name, String registrationDateStr, int groupNumber) {
        this.name = name;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        this.registrationDate = MonthDay.parse(registrationDateStr, formatter);
        this.groupNumber = groupNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}