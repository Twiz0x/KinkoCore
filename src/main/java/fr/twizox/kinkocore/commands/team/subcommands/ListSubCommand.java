package fr.twizox.kinkocore.commands.team.subcommands;

import org.bukkit.command.CommandSender;

public class ListSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List all teams";
    }

    @Override
    public String getUsage() {
        return "/equipage list";
    }

    @Override
    public String getPermission() {
        return "kinkoteams.admin";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        sender.sendMessage("Teams :");
        for (String loopTeam : teamManager.getTeamsName()) {
            sender.sendMessage(" -> " + loopTeam);
        }
        sender.sendMessage("End of teams");
        return true;

    }
}
