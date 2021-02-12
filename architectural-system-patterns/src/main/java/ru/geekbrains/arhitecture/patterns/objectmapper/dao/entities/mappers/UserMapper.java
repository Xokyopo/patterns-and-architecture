package ru.geekbrains.arhitecture.patterns.objectmapper.dao.entities.mappers;

import ru.geekbrains.arhitecture.patterns.objectmapper.dao.entities.User;
import ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.mappers.Field;
import ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper implements Mapper<User> {
    @Override
    public User create(ResultSet resultSet) throws SQLException {
        User result = new User();

        result.setId(resultSet.getLong("id"));
        result.setName(resultSet.getString("name"));

        return result;
    }

    @Override
    public List<Field> getAllFields(User entity) {
        List<Field> result = new ArrayList<>();

        result.add(new Field("id", entity.getId()));
        result.add(new Field("name", entity.getName()));

        return result;
    }

    @Override
    public List<Field> getID(User entity) {
        List<Field> result = new ArrayList<>();

        result.add(new Field("id", entity.getId()));

        return result;
    }

    @Override
    public User getEmptyElement() {
        return new User();
    }
}
