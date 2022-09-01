package fr.twizox.kinkocore.commands;

import fr.twizox.kinkocore.Camp;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CampCommandAbstract implements CommandExecutor {

    private final Camp camp;
    private final AccountManager accountManager;

    public CampCommandAbstract(AccountManager accountManager, Camp camp) {
        this.camp = camp;
        this.accountManager = accountManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (accountManager.getAccount(player.getUniqueId()).getCamp() != camp) {
                sender.sendMessage(String.format(KinkoCore.instance.getConfig().getString("messages.wrong-camp"), camp.getName()));
                return true;
            }
        }
        return execute(sender, command, label, args);
    }

    public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);

}
