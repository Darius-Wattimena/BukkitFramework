package nl.antimeta.bukkit.framework.database.model;

import java.sql.ResultSet;
import java.util.List;

public abstract class BaseEntity<T> {

    public abstract Number getId();
    public abstract void setId(Number id);

    public abstract List<T> buildResultSet(ResultSet resultSet);
}
