package com.govtech.champions.controller;

import com.govtech.champions.command.AddTeamsCommand;
import com.govtech.champions.command.ClearTeamsCommand;
import com.govtech.champions.command.GetRankingsCommand;
import com.govtech.champions.entity.Team;
import com.govtech.champions.repository.TeamRepository;
import com.govtech.champions.service.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/batch")
    public ResponseEntity<?> addTeams(@RequestBody String teamData) {
        try {
            AddTeamsCommand command = new AddTeamsCommand(teamData, teamRepository);
            commandExecutor.executeCommand(command);
            return ResponseEntity.ok("Teams added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getTeam(@PathVariable String name) {
        Optional<Team> team = teamRepository.findById(name);
        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/rankings/{groupNumber}")
    public ResponseEntity<?> getRankings(@PathVariable int groupNumber) {
        try {
            GetRankingsCommand command = new GetRankingsCommand(teamRepository, groupNumber);
            List<Team> rankings = commandExecutor.executeCommand(command);
            return ResponseEntity.ok(rankings);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rankings: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearTeams() {
        try {
            ClearTeamsCommand command = new ClearTeamsCommand(teamRepository);
            commandExecutor.executeCommand(command);
            return ResponseEntity.ok("All teams have been cleared.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error clearing teams: " + e.getMessage());
        }
    }
}