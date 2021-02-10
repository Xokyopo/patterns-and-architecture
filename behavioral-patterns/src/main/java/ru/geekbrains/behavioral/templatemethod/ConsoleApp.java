package ru.geekbrains.behavioral.templatemethod;

import java.util.Scanner;

public class ConsoleApp extends App<String> {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    protected void printWelcomeMessage() {
        System.out.println("It's a simple console app");
    }

    @Override
    protected void printRules() {
        System.out.println("Input text and press 'Enter' and i repeat him, type '\\q' for quit");
    }

    @Override
    protected String getUserInput() {
        System.out.print(">>");
        String result = this.scanner.nextLine();
        System.out.println();
        return result;
    }

    @Override
    protected boolean isValidInput(String userInput) {
        return true;
    }

    @Override
    protected boolean processing(String userInput) {
        if (userInput.startsWith("\\q")) return false;
        System.out.println("repeat: " + userInput + "\n");
        return true;
    }
}
