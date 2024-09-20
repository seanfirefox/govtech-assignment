package com.govtech.champions.service;

import com.govtech.champions.entity.Match;
import com.govtech.champions.entity.Team;
import com.govtech.champions.model.MatchDTO;
import com.govtech.champions.repository.MatchRepository;
import com.govtech.champions.repository.TeamRepository;
import com.govtech.champions.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    public List<MatchDTO> createMatchesFromInput(String matchesInput) throws Exception {
        List<Match> matches = ParserUtils.parseMatchData(matchesInput);
        for (Match match : matches) {
            processMatch(match);
            matchRepository.save(match);
        }
        return matches.stream()
                .map(match -> new MatchDTO(match.getId(), match.getTeamAName(), match.getTeamBName(), match.getGoalsA(), match.getGoalsB()))
                .toList();
    }

    private void processMatch(Match match) throws Exception {
        Optional<Team> teamAOpt = teamRepository.findByName(match.getTeamAName());
        Optional<Team> teamBOpt = teamRepository.findByName(match.getTeamBName());

        if (teamAOpt.isEmpty()) {
            log.error("Team A not found: {}", match.getTeamAName());
            throw new Exception("Team A not found: " + match.getTeamAName());
        }

        if (teamBOpt.isEmpty()) {
            log.error("Team B not found: {}", match.getTeamBName());
            throw new Exception("Team B not found: " + match.getTeamBName());
        }

        Team teamA = teamAOpt.get();
        Team teamB = teamBOpt.get();

        int teamAGoals = match.getGoalsA();
        int teamBGoals = match.getGoalsB();

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
                teamA.getName(), teamA.getWins(), teamA.getDraws(), teamA.getLosses(),
                teamA.getTotalMatchPoints(), teamA.getGoalsScored(), teamA.getAlternateMatchPoints());

        log.info("Updated Statistics - {}: Wins: {}, Draws: {}, Losses: {}, Total Points: {}, Goals Scored: {}, Alternate Points: {}",
                teamB.getName(), teamB.getWins(), teamB.getDraws(), teamB.getLosses(),
                teamB.getTotalMatchPoints(), teamB.getGoalsScored(), teamB.getAlternateMatchPoints());
    }

    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(match -> new MatchDTO(match.getId(), match.getTeamAName(), match.getTeamBName(), match.getGoalsA(), match.getGoalsB()))
                .toList();
    }

    public void deleteMatch(Long matchId) throws Exception {
        Optional<Match> matchOpt = matchRepository.findById(matchId);
        if (matchOpt.isEmpty()) {
            log.error("Match not found with ID: {}", matchId);
            throw new Exception("Match not found with ID: " + matchId);
        }
        Match match = matchOpt.get();
        matchRepository.delete(match);
        log.info("Deleted match with ID: {}", matchId);
    }

    public void clearAllMatches() {
        matchRepository.deleteAll();
        log.info("All matches have been cleared.");
    }
}