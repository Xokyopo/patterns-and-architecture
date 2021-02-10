package ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers;

import java.util.Objects;

public class Field {
    private String name;
    private Object value;
    private Class<?> valueType;

    public Field(String name, Object value, Class<?> valueType) {
        this.name = name;
        this.value = value;
        this.valueType = valueType;
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

    public Class<?> getValueType() {
        return valueType;
    }

    public void setValueType(Class<?> valueType) {
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Field)) return false;

        Field field = (Field) obj;

        return Objects.equals(this.valueType, field.value)
                && Objects.equals(this.value, field.value)
                && Objects.equals(this.name, field.name);
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", valueType=" + valueType.getSimpleName() +
                '}';
    }
}
