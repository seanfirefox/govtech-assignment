package com.govtech.champions.utils;

import com.govtech.champions.entity.Match;
import com.govtech.champions.entity.Team;

import java.util.Arrays;
import java.util.List;

public class ParserUtils {
    private ParserUtils() {}

    public static List<Team> parseTeamData(String data) {
        return Arrays.stream(data.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length != 3) {
                        throw new IllegalArgumentException("Invalid team data format: " + line);
                    }
                    String name = tokens[0];
                    String date = tokens[1];
                    int group;
                    try {
                        group = Integer.parseInt(tokens[2]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid group number for team " + name + ": " + tokens[2]);
                    }
                    return new Team(name, date, group);
                })
                .toList();
    }

    public static List<Match> parseMatchData(String data) {
        return Arrays.stream(data.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length != 4) {
                        throw new IllegalArgumentException("Invalid match data format: " + line);
                    }
                    String teamAName = tokens[0];
                    String teamBName = tokens[1];
                    int teamAGoals;
                    int teamBGoals;
                    try {
                        teamAGoals = Integer.parseInt(tokens[2]);
                        teamBGoals = Integer.parseInt(tokens[3]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid goal numbers in match: " + line);
                    }
                    return new Match(teamAName, teamBName, teamAGoals, teamBGoals);
                })
                .toList();
    }
}