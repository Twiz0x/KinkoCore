package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.account.Account;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return config.joinUsage;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        Account account = accountManager.getAccount(uuid);

        if (account.hasTeam()) return sendFormattedMessage(player, config.alreadyInTeam);
        if (args.length < 2) return sendFormattedMessage(player, getUsage());

        Player inviter = Bukkit.getPlayer(args[1]);

        if (!inviteManager.canAcceptInvitation(inviter, uuid))
            return sendFormattedMessage(player, config.inviteExpired);

        inviteManager.acceptInvitation(inviter, uuid);
        sendFormattedMessage(player, String.format(config.inviteAccepted, inviter.getName()));
        String joinMessage = String.format(config.teamMemberJoined, player.getName());

        for (UUID loopUuid : account.getTeam().getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(loopUuid);
            if (offlinePlayer.isOnline()) {
                offlinePlayer.getPlayer().sendMessage(joinMessage);
            }
        }
        return true;
    }
}
