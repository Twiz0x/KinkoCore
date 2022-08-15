package fr.twizox.kinkocore;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.databases.H2Database;
import fr.twizox.kinkocore.events.PlayerLoadedEvent;
import fr.twizox.kinkocore.listeners.PlayerListeners;
import fr.twizox.kinkocore.placeholderapi.AccountExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class KinkoCore extends JavaPlugin {

    public static KinkoCore instance;
    private H2Database database;
    private AccountManager accountManager;

    @Override
    public void onEnable() {
        instance = this;
        database = new H2Database("kinkoapi", getDataFolder().getAbsolutePath(), getLogger());
        if (database.init()) {
            accountManager = new AccountManager(database.getDao(Account.class));
        } else {
            getLogger().severe("Failed to initialize database");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AccountExpansion(accountManager).register();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            accountManager.getAccountFromDatabase(uuid).whenComplete((account, throwable) -> {
                if (account == null) {
                    account = new Account(uuid);
                    accountManager.saveAccount(account);
                }
                accountManager.addAccount(uuid, account);
            });
        }
        getServer().getPluginManager().registerEvents(new PlayerListeners(accountManager), this);
        getLogger().info("On");
    }

    @Override
    public void onDisable() {
        database.close();
        getLogger().info("Off");
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

}
