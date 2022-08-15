package fr.twizox.kinkocore.listeners;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.events.PlayerLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListeners implements Listener {

    private final AccountManager accountManager;

    public PlayerListeners(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        accountManager.getAccountFromDatabase(uuid).whenComplete((account, throwable) -> {
            if (account == null) {
                account = new Account(uuid);
                accountManager.saveAccount(account);
            }
            accountManager.addAccount(uuid, account);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerLoadedEvent(player, account));
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Account account = accountManager.getAccount(uuid);
        if (account != null) {
            accountManager.saveAccount(account);
            accountManager.removeAccount(uuid);
        }
    }

}
