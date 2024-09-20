package com.govtech.champions.repository;

import com.govtech.champions.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Optional<Team> findByName(String name);
    Optional<Team> findById(Long id);
    List<Team> findByGroupNumberOrderByTotalMatchPointsDescAlternateMatchPointsDescGoalsScoredDesc(int groupNumber);
    void deleteByName(String name);
}
