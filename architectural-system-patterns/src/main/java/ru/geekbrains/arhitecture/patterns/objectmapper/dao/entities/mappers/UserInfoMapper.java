package ru.geekbrains.arhitecture.patterns.objectmapper.dao.entities.mappers;

import ru.geekbrains.arhitecture.patterns.objectmapper.dao.entities.UserInfo;
import ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.mappers.Field;
import ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserInfoMapper implements Mapper<UserInfo> {

    @Override
    public UserInfo create(ResultSet resultSet) throws SQLException {
        UserInfo result = new UserInfo();

        result.setUserId(resultSet.getLong("userId"));
        result.setFirstName(resultSet.getString("firstName"));
        result.setLastName(resultSet.getString("lastName"));
        result.setEmail(resultSet.getString("email"));
        result.setPhoneNumber(resultSet.getString("phoneNumber"));

        return result;
    }

    @Override
    public List<Field> getAllFields(UserInfo entity) {
        List<Field> fields = new LinkedList<>();

        fields.add(new Field("userId", entity.getUserId()));
        fields.add(new Field("firstName", entity.getFirstName()));
        fields.add(new Field("lastName", entity.getLastName()));
        fields.add(new Field("email", entity.getEmail()));
        fields.add(new Field("phoneNumber", entity.getPhoneNumber()));

        return fields;
    }

    @Override
    public List<Field> getID(UserInfo entity) {
        return List.of(new Field("userId", entity.getUserId()));
    }

    @Override
    public UserInfo getEmptyElement() {
        return new UserInfo();
    }
}
