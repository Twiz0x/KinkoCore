package fr.twizox.kinkocore.listeners;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.events.PlayerLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static fr.twizox.kinkocore.KinkoCore.teamManager;

public class PlayerLoadedListener implements Listener {

    @EventHandler
    public void onPlayerLoad(PlayerLoadedEvent event) {
        Account account = event.getAccount();
        if (account.hasTeam()) teamManager.loadDatabaseTeam(account.getTeamName());
    }

}
