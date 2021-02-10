package ru.geekbrains.patterns.structural.composite;

import java.util.Random;

public class Utils {
    private static final Random random;

    static {
        random = new Random();
    }

    public static int getRandomInt() {
        return random.nextInt(1000);
    }
}
