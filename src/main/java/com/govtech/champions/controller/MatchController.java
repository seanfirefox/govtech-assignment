package com.govtech.champions.controller;

import com.govtech.champions.command.AddMatchCommand;
import com.govtech.champions.command.ClearMatchesCommand;
import com.govtech.champions.repository.MatchRepository;
import com.govtech.champions.repository.TeamRepository;
import com.govtech.champions.service.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/batch")
    public ResponseEntity<?> addMatches(@RequestBody String matchData) {
        try {
            AddMatchCommand command = new AddMatchCommand(matchData, matchRepository, teamRepository);
            commandExecutor.executeCommand(command);
            return ResponseEntity.ok("Matches added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearMatches() {
        try {
            ClearMatchesCommand command = new ClearMatchesCommand(matchRepository);
            commandExecutor.executeCommand(command);
            return ResponseEntity.ok("All matches have been cleared.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error clearing matches: " + e.getMessage());
        }
    }
}