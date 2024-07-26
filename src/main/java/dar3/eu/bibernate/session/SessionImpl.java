package dar3.eu.bibernate.session;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;

import static dar3.eu.bibernate.util.EntityUtils.createEntityFromResultSet;
import static dar3.eu.bibernate.util.EntityUtils.resolveIdColumnName;
import static dar3.eu.bibernate.util.EntityUtils.resolveTableName;
import static dar3.eu.bibernate.util.EntityUtils.verifyEntity;

@RequiredArgsConstructor
public class SessionImpl implements Session {
    private final DataSource dataSource;
    private boolean closed;

   @Override
    @SneakyThrows
    public <T> T findById(Class<T> entityType, Object id) {
       checkOpen();
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
                }
            }
            throw new RuntimeException("Entity not found by id = " + id);
        }
    }

    private void checkOpen() {


    }

    @Override
    public void close() {
        this.closed = true;
    }
}
