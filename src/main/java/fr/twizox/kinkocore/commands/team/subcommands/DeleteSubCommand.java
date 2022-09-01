package fr.twizox.kinkocore.commands.team.subcommands;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeleteSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "delete";
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
        Team team = account.getTeam();

        if (!account.hasTeam()) return sendFormattedMessage(player, config.noTeam);
        if (!team.isOwner(uuid)) return sendFormattedMessage(player, config.notOwner);

        teamManager.deleteTeam(team);
        account.setTeam(null);
        accountManager.saveAccount(account);
        return sendFormattedMessage(player, config.teamDeleted);
    }
}
