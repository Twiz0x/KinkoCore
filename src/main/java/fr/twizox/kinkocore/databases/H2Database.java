package fr.twizox.kinkocore.databases;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

public class H2Database extends Database {

    public H2Database(String name, String absolutePath, Logger logger) {
        super(name, absolutePath, logger);
    }

    public boolean init() {
        String connectionUrl = "jdbc:h2:" + path + File.separator + name;
        try {
            connectionSource = new JdbcConnectionSource(connectionUrl);
            logger.info("Successfully connected to database: " + name);
            return true;
        } catch (SQLException e) {
            logger.severe("Could not connect the database" + name + "!" + "\n" + e);
            return false;
        }
    }

    public void close() {
        try {
            connectionSource.close();
            logger.fine("Successfully disconnected from the database " + name);
        } catch (Exception e) {
            logger.severe("Could not disconnect from the database " + name + "!" + "\n" + e);
        }
    }

    public <T> Dao<T, String> getDao(Class<T> tClass) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, tClass);
            return DaoManager.createDao(connectionSource, tClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}