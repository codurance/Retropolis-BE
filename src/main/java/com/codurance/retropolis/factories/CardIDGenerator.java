package com.codurance.retropolis.factories;

public class CardIDGenerator {
    private static int currentID = 0;

    public static int nextID() {
        return ++currentID;
    }
}
