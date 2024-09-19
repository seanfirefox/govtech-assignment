package com.govtech.champions.command;

public interface Command<T> {
    T execute();
}