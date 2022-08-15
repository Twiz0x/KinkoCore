package fr.twizox.kinkocore.databases;

import com.j256.ormlite.support.ConnectionSource;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Database {

    protected final String name;
    protected final String path;
    private final Logger logger;
    protected ConnectionSource connectionSource;

    protected Database(String name, String path) {
        this(name, path, null);
    }

    protected Database(String name, String path, Logger logger) {
        this.name = name;
        this.path = path;
        this.logger = logger;
    }

    protected void log(Level level, String message, Throwable throwable) {
        if (logger == null) return;
        if (level == null) level = Level.INFO;
        logger.log(level, message, throwable);
    }

    protected void log(Level level, String message) {
        log(level, message, null);
    }

    protected void log(String message) {
        log(null, message, null);
    }

    public String getName() {
        return name;
    }

    public abstract boolean init();

    public abstract void close();

}
