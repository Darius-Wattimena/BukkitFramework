package nl.antimeta.bukkit.framework.database;

import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.test.TestDao;
import nl.antimeta.bukkit.framework.test.TestEntity;
import nl.antimeta.bukkit.framework.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class DaoManger {

    private final Map<Class<?>, Dao<?>> daoMap = new HashMap<>();
    private Database database;

    public DaoManger(Database database) {
        this.database = database;
    }

    public <T extends BaseEntity> void registerDao(Class<T> tClass, Dao<T> tDao) {
        if (!daoMap.containsKey(tClass)) {
            LogUtil.info("Register Dao " + tClass.getSimpleName());
            daoMap.put(tClass, tDao);
        }
    }

    public <T extends BaseEntity> boolean checkIfDaoExists(Class<T> tClass) {
        return daoMap.containsKey(tClass);
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
