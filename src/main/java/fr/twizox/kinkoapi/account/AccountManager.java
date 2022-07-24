package fr.twizox.kinkoapi.account;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {

    private final ConcurrentHashMap<UUID, Account> accounts = new ConcurrentHashMap<>();

    public AccountManager() {
    }

    public Account getAccount(UUID uuid) {
        return accounts.get(uuid);
    }

    public void addAccount(UUID uuid, Account account) {
        accounts.put(uuid, account);
    }

    public void removeAccount(UUID uuid) {
        accounts.remove(uuid);
    }

}
