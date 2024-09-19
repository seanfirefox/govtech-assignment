package com.govtech.champions.repository;

import com.govtech.champions.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {}
