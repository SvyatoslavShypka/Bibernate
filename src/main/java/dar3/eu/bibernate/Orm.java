package dar3.eu.bibernate;

import dar3.eu.demo.entity.Participant;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

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
        try (var connection = dataSource.getConnection()) {
            var tableName = "participants";
            var idColumnName = "id";
            String selectSQL = "SELECT * FROM %s WHERE %s = ?".formatted(tableName, idColumnName);
            try (var selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setObject(1, id);
                var rs = selectStatement.executeQuery();
                if (rs.next()) {
                    var entity = new Participant();
                    entity.setFirstName(rs.getString("first_name"));
                    entity.setLastName(rs.getString("last_name"));
                    return (T) entity;
                }
            }
            throw new RuntimeException("Entity not found by id = " + id);
        }
    }
}
