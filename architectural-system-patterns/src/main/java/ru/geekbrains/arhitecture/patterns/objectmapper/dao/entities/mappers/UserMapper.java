package ru.geekbrains.arhitecture.patterns.objectmapper.dao.entities.mappers;

import ru.geekbrains.arhitecture.patterns.objectmapper.dao.entities.User;
import ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers.Field;
import ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMapper implements Mapper<User> {
    @Override
    public User createFromFields(List<Field> fields) {
        User result = new User();

        Map<String, Field> stringField = fields.stream().collect(Collectors.toMap(Field::getName, field ->  field));

        result.setId((Long) stringField.get("id").getValue());
        result.setName((String) stringField.get("name").getValue());

        return result;
    }

    @Override
    public List<Field> getAllFields(User entity) {
        List<Field> result = new ArrayList<>();

        result.add(new Field("id", entity.getId(), long.class));
        result.add(new Field("name", entity.getName(), String.class));

        return result;
    }

    @Override
    public List<Field> getID(User entity) {
        List<Field> result = new ArrayList<>();

        result.add(new Field("id", entity.getId(), long.class));

        return result;
    }

    @Override
    public User getEmptyElement() {
        return new User();
    }
}
