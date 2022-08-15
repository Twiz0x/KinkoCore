package fr.twizox.kinkocore.events;

import fr.twizox.kinkocore.account.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLoadedEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Account account;

    public PlayerLoadedEvent(Player player, Account account) {
        super(player);
        this.account = account;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public static HandlerList getHandlerList() {
        return null;
    }

    public Account getAccount() {
        return account;
    }
}
