package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.Camp;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.configurations.TeamConfiguration;
import fr.twizox.kinkocore.invitations.InviteManager;
import fr.twizox.kinkocore.teams.TeamManager;
import fr.twizox.kinkocore.utils.SenderType;
import fr.twizox.kinkocore.utils.SubCommand;
import org.bukkit.command.CommandSender;

public interface TeamSubCommand extends SubCommand {

    AccountManager accountManager = KinkoCore.accountManager;
    TeamManager teamManager = KinkoCore.teamManager;
    InviteManager inviteManager = KinkoCore.inviteManager;
    TeamConfiguration config = KinkoCore.teamConfiguration;

    @Override
    default SenderType getSenderType() {
        return SenderType.PLAYER;
    }

    @Override
    default String getDescription() {
        return "";
    }

    @Override
    default String getPermission() {
        return null;
    }

    default Camp getCamp() {
        return Camp.PIRATE;
    }

    default boolean canExecute(CommandSender sender) {
        if (getPermission() != null && !sender.hasPermission(getPermission())) return false;
        if (getSenderType().getSenderClass() == null) return true;
        return getSenderType().getSenderClass().isAssignableFrom(sender.getClass());
    }

    default String applyPrefix(String message) {
        return config.prefix + message;
    }

    default boolean sendFormattedMessage(CommandSender sender, String message) {
        message = applyPrefix(message);
        sender.sendMessage(message);
        return true;
    }

}
