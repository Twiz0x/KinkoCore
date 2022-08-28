package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return config.kickUsage;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        Account account = accountManager.getAccount(uuid);
        Team team = account.getTeam();

        if (!account.hasTeam()) return sendFormattedMessage(player, config.noTeam);
        if (!team.isOwner(uuid)) return sendFormattedMessage(player, config.notOwner);
        if (args.length != 2) return sendFormattedMessage(player, getUsage());
        if (player.getName().equals(args[1])) return sendFormattedMessage(player, config.cannotKickSelf);

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (!target.hasPlayedBefore() && !target.isOnline())
            return sendFormattedMessage(player, String.format(config.playerNotFound, args[1]));

        if (!team.hasMember(target.getUniqueId()))
            return sendFormattedMessage(player, String.format(config.targetNotInTeam, target.getName()));

        team.removeMember(target.getUniqueId());
        teamManager.saveTeam(team);

        if (target.isOnline()) {
            sendFormattedMessage(target.getPlayer(), config.gotKicked);
        }
        sendFormattedMessage(player, String.format(config.kickDone, target.getName()));

        for (UUID loopUuid : team.getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(loopUuid);
            if (offlinePlayer.isOnline())
                sendFormattedMessage(offlinePlayer.getPlayer(), String.format(config.teamMemberKicked, target.getName()));
        }

        Account targetAccount = accountManager.getAccount(target.getUniqueId());
        if (targetAccount == null) {
            accountManager.getAccountFromDatabase(target.getUniqueId()).whenComplete(((targetAccountFound, throwable) -> {
                targetAccountFound.setTeam(null);
                accountManager.saveAccount(targetAccountFound);
            }));
        } else {
            targetAccount.setTeam(null);
            accountManager.saveAccount(targetAccount);
        }
        return true;
    }
}
