package ru.geekbrains.behavioral.templatemethod;

public abstract class App<O> {

    public void run() {
        O userInput;

        this.printWelcomeMessage();
        do {
            this.printRules();
            userInput = this.getUserInput();
        } while (!(this.isValidInput(userInput) && !this.processing(userInput)));
    }

    protected abstract void printWelcomeMessage();

    protected abstract void printRules();

    protected abstract O getUserInput();

    protected abstract boolean isValidInput(O userInput);

    protected abstract boolean processing(O userInput);
}
