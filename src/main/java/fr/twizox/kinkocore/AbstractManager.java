package fr.twizox.kinkocore;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractManager<T> {

    private final Dao<T, String> dao;
    private final ConcurrentHashMap<String, T> entities = new ConcurrentHashMap<>();

    protected AbstractManager(Dao<T, String> dao) {
        this.dao = dao;
    }

    protected abstract void cache(T entity);

    protected void cache(String id, T entity) {
        entities.put(id, entity);
    }

    protected T getEntity(String id) {
        return entities.get(id);
    }

    protected CompletableFuture<List<T>> getEntitiesFromDatabase() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    protected CompletableFuture<Void> loadEntities() {
        return getEntitiesFromDatabase().thenApply(entities -> {
            for (T entity : entities) {
                cache(entity);
            }
            return null;
        });
    }

    protected void uncache(String id) {
        entities.remove(id);
    }

    protected boolean isCached(String id) {
        return entities.containsKey(id);
    }

    protected Optional<T> getOptionalEntity(String id) {
        return Optional.ofNullable(entities.get(id));
    }

    protected CompletableFuture<T> getEntityFromDatabase(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryForId(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected void saveEntity(T entity) {
        CompletableFuture.runAsync(() -> {
            try {
                dao.createOrUpdate(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected void deleteEntity(T entity) {
        CompletableFuture.runAsync(() -> {
            try {
                dao.delete(entity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected void loadEntity(String id) {
        if (entities.containsKey(id)) return;
        getEntityFromDatabase(id).thenApply(team -> entities.put(id, team));
    }

    protected void loadAndSave(String id, T entity) {
        cache(id, entity);
        saveEntity(entity);
    }

    protected ConcurrentHashMap<String, T> getEntities() {
        return entities;
    }

    protected List<T> getEntitiesList() {
        return new ArrayList<>(entities.values());
    }
}
