package com.govtech.champions.command;

import com.govtech.champions.repository.TeamRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClearTeamsCommand implements Command<Void> {
    private final TeamRepository teamRepository;

    @Override
    public Void execute() {
        teamRepository.deleteAll();
        return null;
    }
}