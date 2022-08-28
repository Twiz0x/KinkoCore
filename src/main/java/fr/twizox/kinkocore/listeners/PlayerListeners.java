package fr.twizox.kinkocore.listeners;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.events.PlayerLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static fr.twizox.kinkocore.KinkoCore.accountManager;
import static fr.twizox.kinkocore.KinkoCore.inviteManager;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        accountManager.loadAndGetAccount(uuid).thenAccept(account ->
                Bukkit.getServer().getPluginManager().callEvent(new PlayerLoadedEvent(player, account)));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Account account = accountManager.getAccount(uuid);
        if (account != null) {
            accountManager.removeAccount(uuid);
            accountManager.saveAccount(account);
        }
        inviteManager.deleteInvitations(player);
    }

}
