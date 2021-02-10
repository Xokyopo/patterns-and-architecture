package ru.geekbrains.arhitecture.patterns.objectmapper.orm;

import ru.geekbrains.arhitecture.patterns.objectmapper.orm.exception.UncheckedSQLException;
import ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers.Field;
import ru.geekbrains.arhitecture.patterns.objectmapper.orm.mappers.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryImpl<E> implements Repository<E> {
    private final Mapper<E> mapper;
    private final String tableName;
    private String selectAllQuery;
    private String deletePrepQuery;
    private String updatePrepQuery;
    private String insertPrepQuery;
    private final Connectable dbConnection;

    public RepositoryImpl(Connectable dbConnection, Mapper<E> mapper, String tableName) {
        if (dbConnection == null) throw new IllegalArgumentException("dvConnection not set");
        if (mapper == null) throw new IllegalArgumentException("Mapper can't be null");

        this.dbConnection = dbConnection;
        this.mapper = mapper;
        this.tableName = (tableName.isBlank()) ? this.getClass().getSimpleName() : tableName;

        this.initSqlQueryTemplates();
    }

    private void initSqlQueryTemplates() {
        E emptyEntity = this.mapper.getEmptyElement();
        List<Field> fields = this.mapper.getAllFields(emptyEntity);
        List<Field> id = this.mapper.getAllFields(emptyEntity);
        List<Field> fieldsWithoutId = fields.stream().filter(field -> id.stream().noneMatch(field::equals)).collect(Collectors.toList());

        this.selectAllQuery = String.format(
                "SELECT %s FROM %s",
                this.getFieldsAsString(fields, "%s, %s"),
                this.tableName);

        this.deletePrepQuery = String.format(
                "DELETE FROM %s WHERE %s",
                this.tableName,
                this.getFieldsAsString(id, "%s = ? AND %s = ?"));

        this.updatePrepQuery = String.format(
                "UPDATE %s SET %s WHERE %s",
                this.tableName,
                this.getFieldsAsString(fieldsWithoutId, "%s = %s"),
                this.getFieldsAsString(id, "%s = ? AND %s = ?"));


        this.insertPrepQuery = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                this.tableName,
                this.getFieldsAsString(fields, "%s, %s"),
                this.getFieldsAsString(fields, "%s, %s").replaceAll("[^,]*", "?"));
    }

    private String getFieldsAsString(List<Field> fields, String reducedSchema) {
        return fields.stream().map(Field::getName).reduce((f1, f2)->String.format(reducedSchema, f1, f2)).get();
    }

    private Connection getConnection() {
        return this.dbConnection.getConnection();
    }

    @Override
    public List<E> getAll() {
        List<E> result = new ArrayList<>();

        this.executeStatement(statement -> {
            ResultSet resultSet = statement.executeQuery(this.selectAllQuery);
            result.addAll(this.resultSetToEntities(resultSet));
        });

        return result;
    }

    @Override
    public void save(E entity) {
        //TODO not optimal!!!!!
        List<E> entitiesInBase = this.findByFields(this.mapper.getID(entity));

        if (entitiesInBase.isEmpty()) {
            this.insert(entity);
        } else {
            this.update(entity);
        }
    }

    @Override
    public void saveAll(List<E> entities) {
        //TODO not optimal!!!!!

        entities.forEach(this::save);
    }

    private void insert(E entity) {
        this.executePreparedStatement(this.insertPrepQuery, statement -> {
            List<Field> fields = this.mapper.getAllFields(entity);
            this.addValueToPrepareStatement(statement, 1, fields);

            statement.executeQuery();
        });
    }

    private void insert(List<E> entities) {
        this.executeBatchPreparedStatement(this.insertPrepQuery, statement -> {
            for (E entity : entities) {
                List<Field> fields = this.mapper.getAllFields(entity);
                this.addValueToPrepareStatement(statement, 1, fields);
                statement.addBatch();
            }

            statement.executeBatch();
        });
    }

    private void update(E entity) {
        this.executePreparedStatement(this.updatePrepQuery, statement -> {
            List<Field> fields = this.mapper.getAllFields(entity);
            List<Field> id = this.mapper.getID(entity);
            fields.removeAll(id);

            int index = this.addValueToPrepareStatement(statement, 1, fields);
            this.addValueToPrepareStatement(statement, index, id);

            statement.executeQuery();
        });
    }

    private void update(List<E> entities) {
        this.executeBatchPreparedStatement(this.updatePrepQuery, statement -> {

            for (E entity : entities) {
                List<Field> fields = this.mapper.getAllFields(entity);
                List<Field> id = this.mapper.getID(entity);
                fields.removeAll(id);

                int index = this.addValueToPrepareStatement(statement, 1, fields);
                this.addValueToPrepareStatement(statement, index, id);
                statement.addBatch();
            }

            statement.executeBatch();
        });
    }

    @Override
    public void delete(E entity) {
        this.executePreparedStatement(this.deletePrepQuery, statement -> {
            List<Field> id = this.mapper.getID(entity);
            this.addValueToPrepareStatement(statement, 1, id);
            statement.executeQuery();
        });
    }

    @Override
    public void delete(List<E> entities) {
        this.executeBatchPreparedStatement(this.deletePrepQuery, statement -> {
            List<List<Field>> ids = entities.stream().map(this.mapper::getID).collect(Collectors.toList());

            for (List<Field> id : ids) {
                this.addValueToPrepareStatement(statement, 1, id);
                statement.addBatch();
            }

            statement.executeBatch();
        });
    }

    @Override
    public List<E> findByFields(List<Field> fields) {
        String findQuery = String.format(
                this.insertPrepQuery + " WHERE %s",
                this.getFieldsAsString(fields, "%s = ? AND %s = ?"));
        List<E> result = new ArrayList<>();

        this.executePreparedStatement(findQuery, statement -> {
            this.addValueToPrepareStatement(statement, 1, fields);
            result.addAll(this.resultSetToEntities(statement.executeQuery()));
        });
        return result;
    }

    @Override
    public List<E> findByFieldsORFields(List<List<Field>> fieldOrCondition) {
        String findQuery = String.format(
                this.insertPrepQuery + " WHERE %s",
                fieldOrCondition.stream().map(fields -> this.getFieldsAsString(fields, "%s = ? AND %s = ?"))
                .reduce((leftCondition, rightCondition)->String.format("(%s = ? OR %s = ?)", leftCondition, rightCondition)));

        List<E> result = new ArrayList<>();

        this.executePreparedStatement(findQuery, statement -> {
            int fieldNumber = 1;

            for (List<Field> fields : fieldOrCondition) {
                this.addValueToPrepareStatement(statement, fieldNumber++, fields);
            }

            result.addAll(this.resultSetToEntities(statement.executeQuery()));
        });

        return result;
    }

    private int addValueToPrepareStatement(PreparedStatement preparedStatement,int valueStartIndex, List<Field> fields) throws SQLException {
        for (Field field : fields) {
            preparedStatement.setObject(valueStartIndex++, field.getValue());
        }
        return valueStartIndex;
    }

    private List<E> resultSetToEntities(ResultSet resultSet) throws SQLException {
        List<E> result = new LinkedList<>();
        while (resultSet.next()) {
            result.add(this.mapper.createFromFields(this.extractField(resultSet)));
        }
        return result;
    }

    private List<Field> extractField(ResultSet resultSet) throws SQLException {
        List<Field> fields = this.mapper.getAllFields(this.mapper.getEmptyElement());
        for (Field field : fields) {
            field.setValue(resultSet.getObject(field.getName(), field.getValueType()));
        }
        return fields;
    }

    protected final void executeStatement(Function<Statement> function) {
        try (Statement statement = this.getConnection().createStatement()) {
            function.execute(statement);
        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    protected final void executePreparedStatement(String preparedString, Function<PreparedStatement> fillingFunction) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(preparedString)) {

            fillingFunction.execute(preparedStatement);

        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    protected final void executeBatchPreparedStatement(String preparedString, Function<PreparedStatement> fillingFunction) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(preparedString)) {
            Connection connection = preparedStatement.getConnection();

            try {
                connection.setAutoCommit(false);

                fillingFunction.execute(preparedStatement);

                connection.commit();
            } catch (BatchUpdateException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new BatchUpdateException(e);
            }

        } catch (SQLException e) {
            throw new UncheckedSQLException(e);
        }
    }

    @FunctionalInterface
    protected interface Function<T> {
        void execute(T statement) throws SQLException;
    }
}
