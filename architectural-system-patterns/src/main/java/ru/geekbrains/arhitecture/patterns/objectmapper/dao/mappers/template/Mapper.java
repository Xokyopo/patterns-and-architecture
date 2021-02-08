package ru.geekbrains.arhitecture.patterns.objectmapper.dao.mappers.template;

import java.util.List;

public abstract class Mapper<O> {
    public abstract O setFields(List<Field> fields);
    public abstract List<Field> getFields(O entity);
    public abstract List<Field> getID(O entity);
}
