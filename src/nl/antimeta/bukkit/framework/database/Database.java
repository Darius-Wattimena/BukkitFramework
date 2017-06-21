package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldType;
import nl.antimeta.bukkit.framework.database.model.Resource;
import nl.antimeta.bukkit.framework.database.type.DatabaseType;

import java.lang.annotation.Annotation;
import java.sql.*;
import java.util.List;

public class Database {

    private Connection connection;

    private DatabaseType databaseType;
    private final Resource resource;

    public Database(DatabaseType databaseType, Resource resource) {
        this.databaseType = databaseType;
        this.resource = resource;
    }

    public <T extends BaseEntity> void createDatabase(List<T> baseEntities) {
        try {
            for (T baseEntity : baseEntities) {
                createTable(baseEntity.getClass());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private <T extends BaseEntity> void createTable(Class<T> baseEntity) throws SQLException {
        String sql = buildTableSql(baseEntity);
        PreparedStatement stmt = openConnection().prepareStatement(sql);
        stmt.execute();
    }

    /**
     * @return
     *      Create table sql.
     */
    private <T extends BaseEntity> String buildTableSql(Class<T> baseEntity) {
        Entity entity = baseEntity.getAnnotation(Entity.class);

        String primaryKeyName = null;

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS `").append(entity.tableName()).append("` (");

        Annotation[] annotations = getClass().getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof Field) {
                Field field = (Field) annotation;
                sql.append("`").append(field.name()).append("` ");
                sql.append(getType(field));

                if (!field.nullable()) {
                    sql.append(" NOT NULL");
                }

                if (field.primary()) {
                    sql.append(" AUTO_INCREMENT");
                    primaryKeyName = field.name();
                }

                sql.append(", ");
            }
        }

        sql.append("PRIMARY KEY (`").append(primaryKeyName).append("`)) ");
        sql.append("ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTOINCREMENT=0");

        return sql.toString();
    }

    /**
     * @param field
     *      Annotation that contains the config of a field.
     * @return
     *      Database type of a field eg 'INT(5)' or 'VARCHAR(255)'
     */
    private String getType(Field field) {
        FieldType type = field.fieldType();
        String typeName = databaseType.getType(type);
        String size = null;
        if (type.isDigitSizeNeeded()) {
            if (field.size() != 0) {
                type.setSize(field.size());
            }

            if (field.digitSize() != 0) {
                type.setDigitsSize(field.digitSize());
            }
            size = "(" + type.getSize() + ", "+ type.getDigitsSize() +")";
        } else if (type.isSizeNeeded()) {
            if (field.size() != 0) {
                type.setSize(field.size());
            }
            size = "(" + type.getSize() + ")";
        }

        return typeName + size;
    }

    Connection openConnection() throws SQLException {
        if(checkConnection()) {
            return connection;
        } else {
            try {
                Class.forName(databaseType.getDriverClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection("jdbc:" + databaseType.getUrlPortion() + "://" + resource.getHostname() + ':' + resource.getPort() + '/' + resource.getDatabase() + "?useSSL=false", resource.getUser(), resource.getPassword());
            return connection;
        }
    }

    private boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }
}
