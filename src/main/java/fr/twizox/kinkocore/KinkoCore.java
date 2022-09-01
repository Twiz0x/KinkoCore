package fr.twizox.kinkocore;

import dev.simplix.cirrus.spigot.CirrusSpigot;
import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.commands.CommandManager;
import fr.twizox.kinkocore.configurations.TeamConfiguration;
import fr.twizox.kinkocore.databases.H2Database;
import fr.twizox.kinkocore.invitations.InviteManager;
import fr.twizox.kinkocore.listeners.PlayerListeners;
import fr.twizox.kinkocore.listeners.PlayerLoadedListener;
import fr.twizox.kinkocore.placeholderapi.AccountExpansion;
import fr.twizox.kinkocore.placeholderapi.TeamExpansion;
import fr.twizox.kinkocore.prime.Prime;
import fr.twizox.kinkocore.prime.PrimeManager;
import fr.twizox.kinkocore.teams.Team;
import fr.twizox.kinkocore.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class KinkoCore extends JavaPlugin {

    private H2Database database;
    public AccountExpansion accountExpansion;
    public TeamExpansion teamExpansion;
    public static KinkoCore instance;
    public static TeamManager teamManager;
    public static AccountManager accountManager;
    public static InviteManager inviteManager;
    public static TeamConfiguration teamConfiguration;
    public static PrimeManager primeManager;

    @Override
    public void onEnable() {
        instance = this;
        //saveDefaultConfig();
        saveResource("config.yml", true);
        saveResource("menus/prime-list.json", false);

        CirrusSpigot.init(this);

        database = new H2Database("kinkoapi", getDataFolder().getAbsolutePath(), getLogger());
        teamConfiguration = new TeamConfiguration(this);
        teamConfiguration.load();
        if (database.init()) {
            teamManager = new TeamManager(database.getDao(Team.class));
            accountManager = new AccountManager(database.getDao(Account.class));
            primeManager = new PrimeManager(database.getDao(Prime.class));
        } else {
            getLogger().severe("Failed to initialize database, disabling");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            accountExpansion = new AccountExpansion(accountManager);
            accountExpansion.register();
            teamExpansion = new TeamExpansion();
            teamExpansion.register();
            getLogger().info("PlaceholderAPI found, account & team expansions enabled !");
        }
        inviteManager = new InviteManager(teamManager, accountManager, teamConfiguration);
        accountManager.cacheOnlinePlayers();

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerLoadedListener(), this);

        CommandManager.registerAllCommands(this, accountManager);

        getLogger().info("On");
    }

    @Override
    public void onDisable() {
        try {
            database.close();
            accountExpansion.unregister();
            teamExpansion.unregister();
        } catch (Exception ignored) {
        } finally {
            getLogger().info("Off");
        }
    }


}
