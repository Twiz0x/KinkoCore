package fr.twizox.kinkocore.commands;

import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.commands.subcommands.*;
import fr.twizox.kinkocore.configurations.TeamConfiguration;
import fr.twizox.kinkocore.invitations.InviteManager;
import fr.twizox.kinkocore.teams.Team;
import fr.twizox.kinkocore.teams.TeamManager;
import fr.twizox.kinkocore.utils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    private final TeamConfiguration config;
    private final TeamManager teamManager;
    private final AccountManager accountManager;
    private final InviteManager inviteManager;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public TeamCommand(TeamConfiguration config, TeamManager teamManager, AccountManager accountManager, InviteManager inviteManager) {
        this.config = config;
        this.teamManager = teamManager;
        this.accountManager = accountManager;
        this.inviteManager = inviteManager;
        registerSubsCommand();
    }

    private void registerSubsCommand() {
        subCommands.put("invite", new InviteSubCommand());

        subCommands.put("accept", new AcceptSubCommand());

        subCommands.put("create", new CreateSubCommand());
        subCommands.put("delete", new DeleteSubCommand());
        subCommands.put("setowner", new SetOwnerSubCommand());
        subCommands.put("list", new ListSubCommand());

        subCommands.put("leave", new LeaveSubCommand());
        subCommands.put("kick", new KickSubCommand());
    }

    private String applyPrefix(String message) {
        return config.prefix + message;
    }

    private String getPlayerPrefix(Team team, UUID uuid) {
        if (team.getOwner().equals(uuid)) {
            return "§b";
        } else {
            return "§7";
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Vous devez être un joueur pour éxecuter cette commande !");
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Account account = KinkoCore.accountManager.getAccount(uuid);
        if (account == null) return true;
        Team team = account.getTeam();

        if (args.length == 0) {
            if (team == null) player.sendMessage(applyPrefix(config.noTeam));
            else listMembers(sender, team);
            return true;
        }

        args[0] = args[0].toLowerCase();
        if (!subCommands.containsKey(args[0])) return sendHelp(player);
        TeamSubCommand subCommand = (TeamSubCommand) subCommands.get(args[0]);
        if (!subCommand.canExecute(sender)) {
            sender.sendMessage(applyPrefix(config.noPermission));
            return true;
        }

        if (subCommand.getCamp() != null) {
            if (account.getCamp() != subCommand.getCamp()) {
                sender.sendMessage(applyPrefix(String.format(config.notRequieredCamp, subCommand.getCamp().toString())));
                return true;
            }
        }
        subCommand.execute(player, Arrays.stream(args).skip(1).toArray(String[]::new));
        return true;
    }

    private boolean sendHelp(CommandSender sender) {
        sender.sendMessage(applyPrefix(config.help));
        return true;
    }

    private void listMembers(CommandSender commandSender, Team team) {
        StringBuilder msg = new StringBuilder("§f§l" + team.getName()).append("\n \n");
        msg.append("§aEn ligne (%s):\n §7- ");
        StringBuilder offline = new StringBuilder("§cHors ligne (%s):\n §7- ");
        int onlines = 0, offlines = 0;
        for (UUID loopUuid : team.getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(loopUuid);
            if (offlinePlayer.isOnline()) {
                onlines++;
                msg.append(getPlayerPrefix(team, loopUuid)).append(offlinePlayer.getName()).append("§7, ");
            } else {
                offlines++;
                offline.append(getPlayerPrefix(team, loopUuid)).append(offlinePlayer.getName()).append("§7, ");
            }
        }
        msg.delete(msg.length() - 2, msg.length());
        if (offlines > 0) {
            offline.delete(offline.length() - 2, offline.length());
        } else {
            offline.delete(offline.length() - 7, offline.length());
        }

        msg.append("\n \n").append(offline).append("\n \n").append("§8➥ §7Besoin d'aide ? /equipage help");
        commandSender.sendMessage(applyPrefix(String.format(msg.toString(), onlines, offlines)));
    }

}
