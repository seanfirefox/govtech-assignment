package com.govtech.champions.service;

import com.govtech.champions.command.Command;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutor {
    public <T> T executeCommand(Command<T> command) {
        return command.execute();
    }
}
