package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;

public class DaoManger {

    private Map<Class<?>, Dao<?>> daoMap = new HashMap<>();
    private Database database;

    public DaoManger(Database database) {
        this.database = database;
    }

    public <T extends BaseEntity> void registerDao(Class<T> tClass, Dao<T> tDao) {
        daoMap.put(tClass, tDao);
    }

    public <T extends BaseEntity> Dao<T> findDao(Class<?> tClass) {
        if (!daoMap.containsKey(tClass)) {
            try {
                Dao<T> tDao = new Dao<>(database, (Class<T>) tClass);
                registerDao((Class<T>) tClass, tDao);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (Dao<T>) daoMap.get(tClass);
    }

}
