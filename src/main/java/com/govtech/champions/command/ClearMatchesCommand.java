package com.govtech.champions.command;

import com.govtech.champions.repository.MatchRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClearMatchesCommand implements Command<Void> {
    private final MatchRepository matchRepository;

    @Override
    public Void execute() {
        matchRepository.deleteAll();
        return null;
    }
}