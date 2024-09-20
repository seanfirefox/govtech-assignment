package com.govtech.champions.repository;

import com.govtech.champions.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTeamANameOrTeamBName(String teamAName, String teamBName);
}
