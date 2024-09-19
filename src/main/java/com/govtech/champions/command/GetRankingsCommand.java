package com.govtech.champions.command;

import com.govtech.champions.entity.Team;
import com.govtech.champions.repository.TeamRepository;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class GetRankingsCommand implements Command<List<Team>> {
    private final TeamRepository teamRepository;
    private final int groupNumber;

    @Override
    public List<Team> execute() {
        return teamRepository.findAll().stream()
                .filter(team -> team.getGroupNumber() == groupNumber)
                .sorted(Comparator.comparingInt(Team::getTotalMatchPoints).reversed()
                        .thenComparingInt(Team::getGoalsScored).reversed()
                        .thenComparingInt(Team::getAlternateMatchPoints).reversed()
                        .thenComparing(Team::getRegistrationDate))
                .toList();
    }
}