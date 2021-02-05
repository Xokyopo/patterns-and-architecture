package ru.geekbrains.patterns.structural.composite.domain.entities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Directory implements IFile{
    private final List<IFile> files = new LinkedList<>();

    public Directory(IFile... files) {
        this.add(files);
    }

    public Directory(List<IFile> files) {
        this.add(files);
    }

    @Override
    public long getSize() {
        return files.isEmpty() ? 0 : files.stream().mapToLong(IFile::getSize).sum();
    }

    public void add(IFile... files) {
        this.add(Arrays.asList(files));
    }

    public void add(List<IFile> files) {
        this.files.addAll(files);
    }
}
