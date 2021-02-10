package ru.geekbrains.patterns.structural.composite.domain.entities;

import ru.geekbrains.patterns.structural.composite.Utils;

public class File implements IFile {
    private final long size;

    public File() {
        size = Utils.getRandomInt();
    }

    public File(long size) {
        this.size = size;
    }

    @Override
    public long getSize() {
        return this.size;
    }
}
