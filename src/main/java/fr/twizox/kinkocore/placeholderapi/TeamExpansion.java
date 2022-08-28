package fr.twizox.kinkocore.placeholderapi;

import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.teams.Team;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamExpansion extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "Twiz0x";
    }

    @Override
    public String getIdentifier() {
        return "equipage";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        final Account account = KinkoCore.accountManager.getAccount(player.getUniqueId());

        if (!account.hasTeam()) return null;

        final Team team = account.getTeam();

        switch (identifier.toLowerCase()) {
            case "name":
                return account.getTeam().getName();
            case "owner": {
                final UUID owner = account.getTeam().getOwner();
                return Bukkit.getOfflinePlayer(owner).getName();
            }
            case "members": {
                return Integer.toString(team.getMembers().size());
            }
            default:
                return "";
        }
    }

}