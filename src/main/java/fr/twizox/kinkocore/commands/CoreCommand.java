package fr.twizox.kinkocore.commands;

import fr.twizox.kinkocore.Camp;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static fr.twizox.kinkocore.KinkoCore.accountManager;

public class CoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kinko.admin")) return true;

        FileConfiguration config = KinkoCore.instance.getConfig();

        if (args.length == 1) {
            if (!(sender instanceof Player)) return true;
            Player player = (Player) sender;

            Account account = accountManager.getAccount(player.getUniqueId());
            Camp camp;
            String message;
            try {
                camp = Camp.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException exception) {
                message = config.getString("messages.invalid-camp", "Camp non trouvé !");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return true;
            }
            account.setCamp(camp);
            accountManager.saveAccount(account);

            message = config.getString("messages.camp-changed", "Vous venez de rejoindre le camp %s");
            message = ChatColor.translateAlternateColorCodes('&', message);
            player.sendMessage(String.format(message, camp.getName()));
        }

        return true;
    }
}
