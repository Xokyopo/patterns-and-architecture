package ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm;

import java.sql.Connection;

public interface Connectable {
    Connection getConnection();
}
