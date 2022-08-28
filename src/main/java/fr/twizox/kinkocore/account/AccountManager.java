package fr.twizox.kinkocore.account;

import com.j256.ormlite.dao.Dao;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {

    private final ConcurrentHashMap<UUID, Account> accounts = new ConcurrentHashMap<>();
    private final Dao<Account, String> dao;

    public AccountManager(Dao<Account, String> dao) {
        this.dao = dao;
    }

    public Account getAccount(UUID uuid) {
        return accounts.get(uuid);
    }

    public Optional<Account> getOptionalAccount(UUID uuid) {
        return Optional.ofNullable(accounts.get(uuid));
    }

    public CompletableFuture<Account> getAccountFromDatabase(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryForId(uuid.toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Account> loadAndGetAccount(UUID uuid) {
        return getAccountFromDatabase(uuid).whenComplete((account, throwable) -> {
            if (account == null) {
                account = new Account(uuid);
                saveAccount(account);
            }
            accounts.put(uuid, account);
        });
    }

    public void addAccount(UUID uuid, Account account) {
        accounts.put(uuid, account);
    }

    public void removeAccount(UUID uuid) {
        accounts.remove(uuid);
    }

    public void saveAccount(Account account) {
        CompletableFuture.runAsync(() -> {
            try {
                dao.createOrUpdate(account);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteAccount(Account account) {
        CompletableFuture.runAsync(() -> {
            try {
                dao.delete(account);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void cacheOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            this.getAccountFromDatabase(uuid).whenComplete((account, throwable) -> {
                if (account == null) {
                    account = new Account(uuid);
                    this.saveAccount(account);
                }
                this.addAccount(uuid, account);
            });
        }
    }

}
