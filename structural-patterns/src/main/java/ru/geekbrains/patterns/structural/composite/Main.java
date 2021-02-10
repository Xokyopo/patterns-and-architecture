package ru.geekbrains.patterns.structural.composite;


import ru.geekbrains.patterns.structural.composite.domain.entities.Directory;
import ru.geekbrains.patterns.structural.composite.domain.entities.File;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Directory root = new Directory(Stream.generate(File::new).limit(10).collect(Collectors.toList()));
        Directory subfolder = new Directory(Stream.generate(File::new).limit(10).collect(Collectors.toList()));

        root.add(subfolder);

        System.out.println(root.getSize());
    }
}
