package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.util.LogUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Dao<T extends BaseEntity> extends BaseDao<T> {

    private T entityObject;

    public Dao(Database database, Class<T> tClass) throws Exception {
        super(database, tClass);
        database.getDaoManger().registerDao(tClass, this);
    }

    public ResultSet execute(String sql) throws SQLException {
        LogUtil.info(sql);
        PreparedStatement stmt = database.getConnection().prepareStatement(sql);
        return stmt.executeQuery();
    }

    public boolean executeNoResult(String sql) throws SQLException {
        LogUtil.info(sql);
        PreparedStatement stmt = database.getConnection().prepareStatement(sql);
        return stmt.execute();
    }

    public void createTableIfNeeded() throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, database.getDatabaseType());
        String sql = sqlBuilder.build(StatementType.CREATE_TABLE_IF_NEEDED);
        executeNoResult(sql);
    }

    public List<T> find(Number id) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);
        sqlBuilder.setId(id.intValue());

        String sql = sqlBuilder.build(StatementType.SELECT);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> find(String field, Object value) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);
        sqlBuilder.addParameter(field, value);

        String sql = sqlBuilder.build(StatementType.SELECT);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> find(Map<String, Object> parameters) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);
        sqlBuilder.addParameters(parameters);

        String sql = sqlBuilder.build(StatementType.SELECT);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    public List<T> findAll() throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);

        String sql = sqlBuilder.build(StatementType.SELECT);
        ResultSet resultSet = execute(sql);
        return processResultSet(resultSet);
    }

    private boolean create() throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, entityObject);

        String sql = sqlBuilder.build(StatementType.INSERT);
        return executeNoResult(sql);
    }

    private boolean update() throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig, entityObject);

        String sql = sqlBuilder.build(StatementType.UPDATE);
        return executeNoResult(sql);
    }

    public boolean save(T entity) throws SQLException {
        this.entityObject = entity;
        if (entity.getId() == null) {
            return create();
        } else {
            return update();
        }
    }

    public boolean delete(T entity) throws SQLException {
        return delete(entity.getId());
    }

    public boolean delete(Number id) throws SQLException {
        if (id != null) {
            SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);
            sqlBuilder.setId(id.intValue());

            String sql = sqlBuilder.build(StatementType.DELETE);
            return executeNoResult(sql);
        }

        return false;
    }

    public boolean delete(String field, Object value) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);
        sqlBuilder.addParameter(field, value);

        String sql = sqlBuilder.build(StatementType.DELETE);
        return executeNoResult(sql);
    }

    public boolean delete(Map<String, Object> parameters) throws SQLException {
        SqlBuilder<T> sqlBuilder = new SqlBuilder<>(entity, tableConfig);
        sqlBuilder.addParameters(parameters);

        String sql = sqlBuilder.build(StatementType.DELETE);
        return executeNoResult(sql);
    }
}
