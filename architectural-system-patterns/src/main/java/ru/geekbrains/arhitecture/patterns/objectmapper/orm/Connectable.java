package ru.geekbrains.arhitecture.patterns.objectmapper.orm;

import java.sql.Connection;

public interface Connectable {
    Connection getConnection();
}
