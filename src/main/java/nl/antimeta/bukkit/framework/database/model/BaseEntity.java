package nl.antimeta.bukkit.framework.database.model;

import java.sql.ResultSet;
import java.util.List;

public abstract class BaseEntity<T> {

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract List<T> buildResultSet(ResultSet resultSet);
}
