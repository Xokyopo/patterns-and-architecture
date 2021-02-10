package ru.geekbrains.arhitecture.patterns.objectmapper.orm;

import ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers.Field;

import java.util.List;

public interface Repository<E> {
    List<E> getAll();
    void save(E entity);
    void saveAll(List<E> entities);
    void delete(E entity);
    void delete(List<E> entities);
    List<E> findByFields(List<Field> fields);
    List<E> findByFieldsORFields(List<List<Field>> fieldOrCondition);
}
