package ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.mappers;

import java.util.Objects;

public class Field {
    private String name;
    private Object value;

    public Field(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Field)) return false;

        Field field = (Field) obj;

        return Objects.equals(this.value, field.value) && Objects.equals(this.name, field.name);
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }
}
