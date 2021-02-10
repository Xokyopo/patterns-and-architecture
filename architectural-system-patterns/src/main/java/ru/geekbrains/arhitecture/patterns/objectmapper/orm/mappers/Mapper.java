package ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers;

import java.util.List;

public interface Mapper<O> {
    O createFromFields(List<Field> fields);
    List<Field> getAllFields(O entity);
    List<Field> getID(O entity);
    O getEmptyElement();
}
