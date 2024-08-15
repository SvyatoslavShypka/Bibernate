package dar3.eu.bibernate;

import com.google.common.base.CaseFormat;
import dar3.eu.bibernate.annotation.Entity;
import dar3.eu.bibernate.annotation.Id;
import dar3.eu.bibernate.annotation.Table;
import dar3.eu.demo.entity.Participant;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
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
        try(var connection = dataSource.getConnection()) {
            var tableName = resolveTableName(entityType);
//            var idColumnName = "id";
            var idColumnName = resolveIdColumnName(entityType);
            var selectSql = "SELECT * FROM participants WHERE id = ?";
            try (var selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setObject(1, id);
                var rs = selectStatement.executeQuery();
                if (rs.next()) {
                    var entity = new Participant();
                    entity.setFirstName(rs.getString("first_name"));
                    entity.setLastName(rs.getString("last_name"));
                    return (T) entity;
                }
            }
        }
        throw new RuntimeException("Entity was not found by id = " + id);
    }

    private String resolveIdColumnName(Class<?> entityType) {
//        TODO to finish 45:48
         return "";
    }


    private <T> String resolveTableName(Class<T> entityType) {
        return Optional.ofNullable(entityType.getAnnotation(Table.class))
                .map(Table::value)
                .orElseGet(() -> underScore(entityType.getSimpleName()));
    }

    private String underScore(String value) {
        return CaseFormat.LOWER_CAMEL
                .converterTo(CaseFormat.LOWER_UNDERSCORE)
                .convert(value);
    }

    private void verifyEntity(Class<?> entityType) {
        if (!entityType.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException(entityType.getSimpleName() + " is not an @Entity");
        }
    }
}
