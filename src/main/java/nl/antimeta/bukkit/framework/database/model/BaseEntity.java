package nl.antimeta.bukkit.framework.database.model;

import java.sql.ResultSet;

public abstract class BaseEntity<T> {

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract T buildResultSet(ResultSet resultSet);
}
