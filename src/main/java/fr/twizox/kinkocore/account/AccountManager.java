package fr.twizox.kinkocore.account;

import com.j256.ormlite.dao.Dao;
import fr.twizox.kinkocore.AbstractManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountManager extends AbstractManager<Account> {

    public AccountManager(Dao<Account, String> dao) {
        super(dao);
    }

    @Override
    protected void cache(Account account) {
        super.cache(account.getUuid().toString(), account);
    }

    public Account getAccount(UUID uuid) {
        return getEntity(uuid.toString());
    }

    public Optional<Account> getOptionalAccount(UUID uuid) {
        return super.getOptionalEntity(uuid.toString());
    }

    public CompletableFuture<Account> getAccountFromDatabase(UUID uuid) {
        return super.getEntityFromDatabase(uuid.toString());
    }

    public CompletableFuture<Account> loadAndGetAccount(UUID uuid) {
        return getEntityFromDatabase(uuid.toString()).thenApply(account -> {
           cache(account);
           return account;
        });
    }

    public void removeAccount(UUID uuid) {
        super.uncache(uuid.toString());
    }

    public void saveAccount(Account account) {
        super.saveEntity(account);
    }

    public void deleteAccount(Account account) {
        super.deleteEntity(account);
        uncache(account.getUuid().toString());
    }

    public void cacheOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            this.getAccountFromDatabase(uuid).whenComplete((account, throwable) -> {
                if (account == null) {
                    account = new Account(uuid);
                    this.saveAccount(account);
                }
                this.cache(account);
            });
        }
    }

}
