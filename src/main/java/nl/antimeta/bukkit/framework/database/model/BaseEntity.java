package nl.antimeta.bukkit.framework.database.model;

import java.sql.ResultSet;
import java.util.List;

public abstract class BaseEntity<T extends Identifiable> implements Identifiable {

    public abstract List<T> buildResultSet(ResultSet resultSet);
}
