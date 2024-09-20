package com.govtech.champions.controller;

import com.govtech.champions.model.MatchDTO;
import com.govtech.champions.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "http://localhost:3000")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/batch")
    public ResponseEntity<?> createMatches(@RequestBody String matchesInput) {
        try {
            List<MatchDTO> createdMatches = matchService.createMatchesFromInput(matchesInput);
            return ResponseEntity.ok(createdMatches);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating matches: " + e.getMessage());
        }
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<?> deleteMatch(@PathVariable Long matchId) {
        try {
            matchService.deleteMatch(matchId);
            return ResponseEntity.ok().body("Match deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting match: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearAllMatches() {
        try {
            matchService.clearAllMatches();
            return ResponseEntity.ok().body("All matches deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error clearing matches: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMatches() {
        try {
            List<MatchDTO> matches = matchService.getAllMatches();
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching matches: " + e.getMessage());
        }
    }
}