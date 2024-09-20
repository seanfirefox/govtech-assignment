package com.govtech.champions.service;

import com.govtech.champions.entity.Team;
import com.govtech.champions.model.TeamDTO;
import com.govtech.champions.repository.MatchRepository;
import com.govtech.champions.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public List<TeamDTO> createTeamsFromInput(String teamsInput) throws Exception {
        List<TeamDTO> createdTeams = parseAndSaveTeams(teamsInput);
        log.info("Successfully created {} teams.", createdTeams.size());
        return createdTeams;
    }

    private List<TeamDTO> parseAndSaveTeams(String input) throws Exception {
        String[] lines = input.split("\\r?\\n");
        return java.util.Arrays.stream(lines)
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    String[] parts = line.split(" ");
                    if (parts.length != 3) {
                        log.error("Invalid team format: {}", line);
                        throw new RuntimeException("Invalid team format: " + line);
                    }
                    String name = parts[0];
                    String regDate = parts[1];
                    int groupNumber;
                    try {
                        groupNumber = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        log.error("Invalid group number for team: {}", name);
                        throw new RuntimeException("Invalid group number for team: " + name);
                    }
                    return new Team(name, regDate, groupNumber);
                })
                .map(teamRepository::save)
                .map(team -> new TeamDTO(
                        team.getId(),
                        team.getName(),
                        team.getRegistrationDate(),
                        team.getGroupNumber(),
                        team.getGoalsScored(),
                        team.getMatchesPlayed(),
                        team.getWins(),
                        team.getDraws(),
                        team.getLosses(),
                        team.getTotalMatchPoints(),
                        team.getAlternateMatchPoints()
                ))
                .toList();
    }

    public List<TeamDTO> getRankings(int groupNumber) throws Exception {
        List<Team> teams = teamRepository.findByGroupNumberOrderByTotalMatchPointsDescAlternateMatchPointsDescGoalsScoredDesc(groupNumber);
        if (teams.isEmpty()) {
            log.warn("No teams found for group number: {}", groupNumber);
            throw new Exception("No teams found for group number: " + groupNumber);
        }
        List<TeamDTO> rankings = teams.stream()
                .map(team -> new TeamDTO(
                        team.getId(),
                        team.getName(),
                        team.getRegistrationDate(),
                        team.getGroupNumber(),
                        team.getGoalsScored(),
                        team.getMatchesPlayed(),
                        team.getWins(),
                        team.getDraws(),
                        team.getLosses(),
                        team.getTotalMatchPoints(),
                        team.getAlternateMatchPoints()
                ))
                .collect(Collectors.toList());
        log.info("Retrieved rankings for group number: {}", groupNumber);
        return rankings;
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamDTO(team.getId(), team.getName(), team.getRegistrationDate(), team.getGroupNumber(), team.getGoalsScored(), team.getMatchesPlayed(), team.getWins(), team.getDraws(), team.getLosses(), team.getTotalMatchPoints(), team.getAlternateMatchPoints()))
                .toList();
    }

    public TeamDTO getTeamDetails(Long teamId) throws Exception {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) {
            log.error("Team not found with ID: {}", teamId);
            throw new Exception("Team not found with ID: " + teamId);
        }
        Team team = teamOpt.get();
        TeamDTO teamDTO = new TeamDTO(
                team.getId(),
                team.getName(),
                team.getRegistrationDate(),
                team.getGroupNumber(),
                team.getGoalsScored(),
                team.getMatchesPlayed(),
                team.getWins(),
                team.getDraws(),
                team.getLosses(),
                team.getTotalMatchPoints(),
                team.getAlternateMatchPoints()
        );
        log.info("Fetched details for team: {}", team.getName());
        return teamDTO;
    }

    @Transactional
    public TeamDTO editTeam(Long teamId, TeamDTO updatedTeam) throws Exception {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) {
            log.error("Attempted to edit non-existent team with ID: {}", teamId);
            throw new Exception("Team not found with ID: " + teamId);
        }
        Team team = teamOpt.get();

        if (!team.getName().equals(updatedTeam.getName())) {
            Optional<Team> existingTeam = teamRepository.findByName(updatedTeam.getName());
            if (existingTeam.isPresent()) {
                log.error("Team name conflict: {}", updatedTeam.getName());
                throw new Exception("Team name already exists: " + updatedTeam.getName());
            }

            matchRepository.findByTeamANameOrTeamBName(team.getName(), team.getName()).forEach(match -> {
                if (match.getTeamAName().equals(team.getName())) {
                    match.setTeamAName(updatedTeam.getName());
                }
                if (match.getTeamBName().equals(team.getName())) {
                    match.setTeamBName(updatedTeam.getName());
                }
                matchRepository.save(match);
            });

            log.info("Updated team name from {} to {}", team.getName(), updatedTeam.getName());
            team.setName(updatedTeam.getName());
        }

        team.setRegistrationDate(updatedTeam.getRegistrationDate());
        team.setGroupNumber(updatedTeam.getGroupNumber());

        team.setGoalsScored(updatedTeam.getGoalsScored());
        team.setMatchesPlayed(updatedTeam.getMatchesPlayed());
        team.setWins(updatedTeam.getWins());
        team.setDraws(updatedTeam.getDraws());
        team.setLosses(updatedTeam.getLosses());
        team.setTotalMatchPoints(updatedTeam.getTotalMatchPoints());
        team.setAlternateMatchPoints(updatedTeam.getAlternateMatchPoints());

        teamRepository.save(team);
        log.info("Edited team with ID: {}", teamId);

        return new TeamDTO(
                team.getId(),
                team.getName(),
                team.getRegistrationDate(),
                team.getGroupNumber(),
                team.getGoalsScored(),
                team.getMatchesPlayed(),
                team.getWins(),
                team.getDraws(),
                team.getLosses(),
                team.getTotalMatchPoints(),
                team.getAlternateMatchPoints()
        );
    }

    @Transactional
    public void deleteTeam(Long teamId) throws Exception {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) {
            log.error("Attempted to delete non-existent team with ID: {}", teamId);
            throw new Exception("Team not found with ID: " + teamId);
        }
        teamRepository.delete(teamOpt.get());
        log.info("Deleted team with ID: {}", teamId);
    }

    @Transactional
    public void clearAllTeams() {
        teamRepository.deleteAll();
        log.info("All teams have been cleared from the repository.");
    }
}