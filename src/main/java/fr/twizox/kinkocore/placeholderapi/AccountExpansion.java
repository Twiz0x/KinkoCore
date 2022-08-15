package fr.twizox.kinkocore.placeholderapi;

import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.configurations.PlaceholdersConfiguration;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class AccountExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "account";
    }

    @Override
    public String getAuthor() {
        return "Twiz0x";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }

    public PlaceholdersConfiguration config;
    public final AccountManager accountManager;

    public AccountExpansion(AccountManager accountManager) {
        this.config = new PlaceholdersConfiguration(KinkoCore.instance.getDataFolder().getAbsolutePath());
        this.config.load();
        this.accountManager = accountManager;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        Account account = accountManager.getAccount(player.getUniqueId());
        if (account != null) {
            identifier = identifier.toLowerCase();
            switch (identifier) {
                case "camp": return account.getCamp().toString();
                case "rank": return account.getRank();
                case "color": return config.colors.get(account.getCamp().name());
            }
        }

        return config.notFound;
    }
}
