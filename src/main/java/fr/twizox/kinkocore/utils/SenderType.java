package fr.twizox.kinkocore.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public enum SenderType {

    ANY(null),
    PLAYER(Player.class),
    CONSOLE(ConsoleCommandSender.class);

    private final Class<? extends CommandSender> senderClass;

    SenderType(Class<? extends CommandSender> senderClass) {
         this.senderClass = senderClass;
    }

    public Class<? extends CommandSender> getSenderClass() {
        return senderClass;
    }

}
