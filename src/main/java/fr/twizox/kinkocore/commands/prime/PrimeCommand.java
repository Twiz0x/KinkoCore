package fr.twizox.kinkocore.commands.prime;

import dev.simplix.cirrus.common.Cirrus;
import dev.simplix.cirrus.common.business.PlayerWrapper;
import dev.simplix.cirrus.common.configuration.MultiPageMenuConfiguration;
import dev.simplix.cirrus.common.converter.Converters;
import fr.twizox.kinkocore.Camp;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.commands.CampCommandAbstract;
import fr.twizox.kinkocore.menus.PrimeMenu;
import fr.twizox.kinkocore.prime.Prime;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrimeCommand extends CampCommandAbstract {

    public PrimeCommand(AccountManager accountManager, Camp camp) {
        super(accountManager, Camp.MARINE);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.prime.usage"));
            return true;
        }

        String firstArg = args[0].toLowerCase();
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if ("list".equals(firstArg)) {
                final String menuConfigPath = KinkoCore.instance.getDataFolder().getAbsolutePath() + "/menus/prime-list.json";
                MultiPageMenuConfiguration menuConfig = Cirrus.configurationFactory().loadFileForMultiPageMenu(menuConfigPath);
                final PlayerWrapper wrapper = Converters.getConverter(Player.class, PlayerWrapper.class).convert(player);
                new PrimeMenu(wrapper, menuConfig).open();
                return true;
            }
        }

        if ("add".equals(firstArg)) {
            if (args.length != 3) {
                sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.prime.add-usage"));
                return true;
            }
            Player target = Bukkit.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.player-not-found"));
                return true;
            }

            Account account = KinkoCore.accountManager.getAccount(target.getUniqueId());
            if (account.getCamp() != Camp.REBELLE && account.getCamp() != Camp.PIRATE) {
                sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.prime.add-not-rebelle-or-pirate"));
                return true;
            }
            if (KinkoCore.primeManager.hasPrime(target.getUniqueId())) {
                sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.prime.add-already-has-prime"));
                return true;
            }
            int amount = 0;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.prime.add-usage"));
            }

            KinkoCore.primeManager.cacheAndSave(new Prime(amount, target.getUniqueId()));

            sender.sendMessage(KinkoCore.instance.getConfig().getString("messages.prime.added"));
            return true;
        }

        return false;
    }

}
