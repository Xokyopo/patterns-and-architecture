package ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Mapper<O> {
    O create(ResultSet resultSet) throws SQLException;
    List<Field> getAllFields(O entity);
    List<Field> getID(O entity);
    O getEmptyElement();
}
