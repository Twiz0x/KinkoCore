package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaveSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "leave";
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
        if (team.isOwner(uuid)) {
            if (team.getMembers().size() != 1) {
                return sendFormattedMessage(player, config.changeOwnerBefore);
            }
        }

        team.removeMember(uuid);

        for (UUID loopUuid : team.getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(loopUuid);
            if (offlinePlayer.isOnline()) {
                offlinePlayer.getPlayer().sendMessage(applyPrefix(String.format(config.teamMemberQuit, player.getName())));
            }
        }

        teamManager.saveTeam(team);
        account.setTeam(null);
        accountManager.saveAccount(account);

        return sendFormattedMessage(player, config.teamLeft);
    }
}
