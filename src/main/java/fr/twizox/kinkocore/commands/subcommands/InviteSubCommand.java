package fr.twizox.kinkocore.commands.subcommands;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InviteSubCommand implements TeamSubCommand {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Invite a player to your team";
    }

    @Override
    public String getUsage() {
        return config.inviteUsage;
    }

    @Override
    public String getPermission() {
        return "kinkoteams.team.invite";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        Account account = accountManager.getAccount(uuid);
        if (!account.hasTeam()) return sendFormattedMessage(player, config.noTeam);
        Team team = account.getTeam();

        if (!team.isOwner(uuid)) return sendFormattedMessage(player, config.notOwner);
        if (args.length == 0) return sendFormattedMessage(player, getUsage());
        if (player.getName().equals(args[0])) sendFormattedMessage(player, config.cannotInviteSelf);
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) return sendFormattedMessage(player, String.format(config.playerNotFound, args[1]));

        if (team.hasMember(target.getUniqueId()))
            return sendFormattedMessage(player, String.format(config.targetAlreadyInTeam, target.getName()));

        if (accountManager.getAccount(target.getUniqueId()).hasTeam())
            return sendFormattedMessage(player, String.format(config.targetAlreadyInATeam, target.getName()));

        if (!inviteManager.addInvitation(player, target.getUniqueId(), team))
            return sendFormattedMessage(player, String.format(config.inviteDelay, inviteManager.getCooldown(player, target.getUniqueId())));

        sendFormattedMessage(player, String.format(config.inviteDone, target.getName()));

        TextComponent text = new TextComponent(String.format(config.inviteNotification, team.getName(), player.getName()));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(config.hoverInvite).create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "§e/equipage join " + player.getName()));
        target.spigot().sendMessage(text);
        return true;
    }

}
