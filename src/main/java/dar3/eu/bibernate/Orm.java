package dar3.eu.bibernate;

import com.google.common.base.CaseFormat;
import dar3.eu.bibernate.annotation.Entity;
import dar3.eu.bibernate.annotation.Id;
import dar3.eu.bibernate.annotation.Table;
import dar3.eu.bibernate.annotation.Column;
import dar3.eu.demo.entity.Participant;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

public class Orm {
    private final DataSource dataSource;

    public Orm(String jdbcUrl, String username, String password) {
        var pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(jdbcUrl);
        pgSimpleDataSource.setUser(username);
        pgSimpleDataSource.setPassword(password);
        this.dataSource = pgSimpleDataSource;
    }

    @SneakyThrows
    public <T> T findById(Class<T> entityType, Object id) {
        verifyEntity(entityType);
        try (var connection = dataSource.getConnection()) {
            var tableName = resolveTableName(entityType);
            var idColumnName = resolveIdColumnName(entityType);
            String selectSQL = "SELECT * FROM %s WHERE %s = ?".formatted(tableName, idColumnName);
            System.out.println("SQL: " + selectSQL);
            try (var selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setObject(1, id);
                var rs = selectStatement.executeQuery();
                if (rs.next()) {
                    return createEntityFromResultSet(entityType, rs);
//                    var entity = new Participant();
//                    entity.setFirstName(rs.getString("first_name"));
//                    entity.setLastName(rs.getString("last_name"));
//                    return (T) entity;
                }
            }
            throw new RuntimeException("Entity not found by id = " + id);
        }
    }
    @SneakyThrows
    private <T> T createEntityFromResultSet(Class<T> entityType, ResultSet rs) {
        var entity = entityType.getConstructor().newInstance();
        for (var field : entityType.getDeclaredFields()) {
            var columnValue = getFieldValue(field, rs);
            field.setAccessible(true);
            field.set(entity, columnValue);
        }
        return entity;
    }

    @SneakyThrows
    private Object getFieldValue(Field field, ResultSet rs) {
        var columnName = resolveColumnName(field);
        var columnValue = rs.getObject(columnName);
        if (columnValue instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

        return columnValue;
    }

    private String resolveIdColumnName(Class<?> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findAny()
                .map(this::resolveColumnName)
                .orElseThrow(() -> new RuntimeException("Entity "
                        + entityType.getSimpleName() + " must have an @Id"));
    }

    private String resolveColumnName(Field field) {
        return Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::value)
                .orElseGet(() -> underscore(field.getName()));
    }

    private void verifyEntity(Class<?> entityType) {
        if (!entityType.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException(entityType.getSimpleName() + " is not an @Entity");
        }
    }

    private String resolveTableName(Class<?> entityType) {
        return Optional.ofNullable(entityType.getAnnotation(Table.class))
                .map(Table::value)
                .orElseGet(() -> underscore(entityType.getSimpleName()));
    }

    private String underscore(String value) {
        return CaseFormat.LOWER_CAMEL
                .converterTo(CaseFormat.LOWER_UNDERSCORE)
                .convert(value);
    }
}
