package com.govtech.champions.command;

import com.govtech.champions.entity.Team;
import com.govtech.champions.repository.TeamRepository;
import com.govtech.champions.utils.ParserUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class AddTeamsCommand implements Command<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AddTeamsCommand.class);

    private final String teamData;
    private final TeamRepository teamRepository;


    @Override
    public Void execute() {
        List<Team> teams = ParserUtils.parseTeamData(teamData);
        for (Team team : teams) {
            if (teamRepository.existsById(team.getName())) {
                logger.warn("Attempted to add duplicate team: {}", team.getName());
                throw new RuntimeException("Team with name " + team.getName() + " already exists.");
            }
            logger.info("Adding team: {}", team.getName());
        }
        teamRepository.saveAll(teams);
        logger.info("All teams added successfully.");
        return null;
    }
}