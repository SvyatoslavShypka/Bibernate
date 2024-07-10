package dar3.eu.bibernate.util;

import com.google.common.base.CaseFormat;
import dar3.eu.bibernate.annotation.Column;
import dar3.eu.bibernate.annotation.Entity;
import dar3.eu.bibernate.annotation.Id;
import dar3.eu.bibernate.annotation.Table;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

public class EntityUtils {
    @SneakyThrows
    public static Object getFieldValue(Field field, ResultSet rs) {
        var columnName = resolveColumnName(field);
        var columnValue = rs.getObject(columnName);
        if (columnValue instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

        return columnValue;
    }

    public static String resolveIdColumnName(Class<?> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findAny()
                .map(EntityUtils::resolveColumnName)
                .orElseThrow(() -> new RuntimeException("Entity "
                        + entityType.getSimpleName() + " must have an @Id"));
    }

    public static String resolveColumnName(Field field) {
        return Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::value)
                .orElseGet(() -> underscore(field.getName()));
    }

    public static void verifyEntity(Class<?> entityType) {
        if (!entityType.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException(entityType.getSimpleName() + " is not an @Entity");
        }
    }

    public static String resolveTableName(Class<?> entityType) {
        return Optional.ofNullable(entityType.getAnnotation(Table.class))
                .map(Table::value)
                .orElseGet(() -> underscore(entityType.getSimpleName()));
    }

    public static String underscore(String value) {
        return CaseFormat.LOWER_CAMEL
                .converterTo(CaseFormat.LOWER_UNDERSCORE)
                .convert(value);
    }

    @SneakyThrows
    public static <T> T createEntityFromResultSet(Class<T> entityType, ResultSet rs) {
        var entity = entityType.getConstructor().newInstance();
        for (var field : entityType.getDeclaredFields()) {
            var columnValue = getFieldValue(field, rs);
            field.setAccessible(true);
            field.set(entity, columnValue);
        }
        return entity;
    }
}
