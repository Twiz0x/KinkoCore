package fr.twizox.kinkocore.commands;

import fr.twizox.kinkocore.Camp;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.commands.prime.PrimeCommand;
import fr.twizox.kinkocore.commands.team.TeamCommand;

public class CommandManager {

    public static void registerAllCommands(KinkoCore kinkoCore, AccountManager accountManager) {
        kinkoCore.getCommand("prime").setExecutor(new PrimeCommand(accountManager, Camp.MARINE));
        kinkoCore.getCommand("equipage").setExecutor(new TeamCommand(KinkoCore.teamConfiguration, KinkoCore.teamManager, accountManager, KinkoCore.inviteManager));
        kinkoCore.getCommand("kinko").setExecutor(new CoreCommand());
    }

}
