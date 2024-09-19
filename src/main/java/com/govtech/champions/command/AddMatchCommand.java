package com.govtech.champions.command;

import com.govtech.champions.entity.Match;
import com.govtech.champions.entity.Team;
import com.govtech.champions.repository.MatchRepository;
import com.govtech.champions.repository.TeamRepository;
import com.govtech.champions.utils.ParserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class AddMatchCommand implements Command<Void> {

    private final String matchData;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Override
    public Void execute() {
        List<Match> matches = ParserUtils.parseMatchData(matchData);
        for (Match match : matches) {
            processMatch(match);
            matchRepository.save(match);
        }
        return null;
    }

    private void processMatch(Match match) {
        Team teamA = teamRepository.findById(match.getTeamAName())
                .orElseThrow(() -> new RuntimeException("Team A not found: " + match.getTeamAName()));
        Team teamB = teamRepository.findById(match.getTeamBName())
                .orElseThrow(() -> new RuntimeException("Team B not found: " + match.getTeamBName()));

        int teamAGoals = match.getTeamAGoals();
        int teamBGoals = match.getTeamBGoals();

        log.info("Processing match: {} vs {} ({}-{})", teamA.getName(), teamB.getName(), teamAGoals, teamBGoals);

        teamA.setGoalsScored(teamA.getGoalsScored() + teamAGoals);
        teamB.setGoalsScored(teamB.getGoalsScored() + teamBGoals);

        teamA.setMatchesPlayed(teamA.getMatchesPlayed() + 1);
        teamB.setMatchesPlayed(teamB.getMatchesPlayed() + 1);

        if (teamAGoals > teamBGoals) {
            teamA.setWins(teamA.getWins() + 1);
            teamA.setTotalMatchPoints(teamA.getTotalMatchPoints() + 3);
            teamB.setLosses(teamB.getLosses() + 1);
            teamA.setAlternateMatchPoints(teamA.getAlternateMatchPoints() + 5);
            teamB.setAlternateMatchPoints(teamB.getAlternateMatchPoints() + 1);
            log.info("Result: {} wins against {}", teamA.getName(), teamB.getName());
        } else if (teamAGoals < teamBGoals) {
            teamB.setWins(teamB.getWins() + 1);
            teamB.setTotalMatchPoints(teamB.getTotalMatchPoints() + 3);
            teamA.setLosses(teamA.getLosses() + 1);
            teamB.setAlternateMatchPoints(teamB.getAlternateMatchPoints() + 5);
            teamA.setAlternateMatchPoints(teamA.getAlternateMatchPoints() + 1);
            log.info("Result: {} wins against {}", teamB.getName(), teamA.getName());
        } else {
            teamA.setDraws(teamA.getDraws() + 1);
            teamB.setDraws(teamB.getDraws() + 1);
            teamA.setTotalMatchPoints(teamA.getTotalMatchPoints() + 1);
            teamB.setTotalMatchPoints(teamB.getTotalMatchPoints() + 1);
            teamA.setAlternateMatchPoints(teamA.getAlternateMatchPoints() + 3);
            teamB.setAlternateMatchPoints(teamB.getAlternateMatchPoints() + 3);
            log.info("Result: Draw between {} and {}", teamA.getName(), teamB.getName());
        }

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        log.info("Updated Statistics - {}: Wins: {}, Draws: {}, Losses: {}, Total Points: {}, Goals Scored: {}, Alternate Points: {}",
                teamA.getName(), teamA.getWins(), teamA.getDraws(), teamA.getLosses(), teamA.getTotalMatchPoints(),
                teamA.getGoalsScored(), teamA.getAlternateMatchPoints());

        log.info("Updated Statistics - {}: Wins: {}, Draws: {}, Losses: {}, Total Points: {}, Goals Scored: {}, Alternate Points: {}",
                teamB.getName(), teamB.getWins(), teamB.getDraws(), teamB.getLosses(), teamB.getTotalMatchPoints(),
                teamB.getGoalsScored(), teamB.getAlternateMatchPoints());
    }
}