package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetOwnerSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "setowner";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        Account account = accountManager.getAccount(uuid);
        if (!account.hasTeam()) return sendFormattedMessage(player, config.noTeam);
        Team team = account.getTeam();

        if (!team.isOwner(uuid)) return sendFormattedMessage(player, config.notOwner);
        if (player.getName().equals(args[1])) return sendFormattedMessage(player, config.cannotOwnerSelf);
        if (args.length != 2) return sendFormattedMessage(player, config.setOwnerUsage);
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) return sendFormattedMessage(player, String.format(config.playerNotFound, args[1]));
        if (!team.hasMember(target.getUniqueId())) return sendFormattedMessage(player, String.format(config.targetNotInTeam, target.getName()));

        team.setOwner(target.getUniqueId());
        teamManager.saveTeam(team);
        return sendFormattedMessage(player, String.format(config.ownerSet, target.getName()));
    }
}
