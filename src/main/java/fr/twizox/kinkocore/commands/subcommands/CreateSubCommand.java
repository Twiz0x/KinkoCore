package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreateSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a team";
    }

    @Override
    public String getUsage() {
        return config.createTeamUsage;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        Account account = accountManager.getAccount(uuid);

        if (account.hasTeam()) return sendFormattedMessage(player, config.alreadyInTeam);

        if (args.length != 2) return sendFormattedMessage(player, config.createTeamUsage);
        if (teamManager.isExistingTeam(args[1])) return sendFormattedMessage(player, config.teamAlreadyExists);

        teamManager.saveTeam(new Team(args[1], uuid));
        account.setTeam(args[1]);
        accountManager.saveAccount(account);
        return sendFormattedMessage(player, config.teamCreated);
    }

}
