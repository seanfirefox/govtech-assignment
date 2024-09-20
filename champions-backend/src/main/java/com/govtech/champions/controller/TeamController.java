package com.govtech.champions.controller;

import com.govtech.champions.model.TeamDTO;
import com.govtech.champions.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/batch")
    public ResponseEntity<?> createTeams(@RequestBody String teamsInput) {
        try {
            List<TeamDTO> createdTeams = teamService.createTeamsFromInput(teamsInput);
            return ResponseEntity.ok(createdTeams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating teams: " + e.getMessage());
        }
    }

    @GetMapping("/rankings/{groupNumber}")
    public ResponseEntity<?> getRankings(@PathVariable int groupNumber) {
        try {
            List<TeamDTO> rankings = teamService.getRankings(groupNumber);
            return ResponseEntity.ok(rankings);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rankings: " + e.getMessage());
        }
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamDetails(@PathVariable Long teamId) {
        try {
            TeamDTO teamDetails = teamService.getTeamDetails(teamId);
            return ResponseEntity.ok(teamDetails);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error fetching team details: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllTeams() {
        try {
            List<TeamDTO> teams = teamService.getAllTeams();
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error fetching team details: " + e.getMessage());
        }
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<?> editTeam(@PathVariable Long teamId, @RequestBody TeamDTO updatedTeam) {
        try {
            TeamDTO editedTeam = teamService.editTeam(teamId, updatedTeam);
            return ResponseEntity.ok(editedTeam);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error editing team: " + e.getMessage());
        }
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long teamId) {
        try {
            teamService.deleteTeam(teamId);
            return ResponseEntity.ok().body("Team deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting team: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearAllTeams() {
        try {
            teamService.clearAllTeams();
            return ResponseEntity.ok().body("All teams deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error clearing teams: " + e.getMessage());
        }
    }

}