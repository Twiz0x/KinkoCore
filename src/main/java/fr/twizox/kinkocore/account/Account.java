package fr.twizox.kinkocore.account;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fr.twizox.kinkocore.Camp;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.teams.Team;
import org.bukkit.Bukkit;

import java.util.UUID;

@DatabaseTable(tableName = "player_account")
public class Account {

    @DatabaseField(id = true)
    private String uuid;
    @DatabaseField
    private Camp camp;
    @DatabaseField
    private int level;
    @DatabaseField
    private String team;

    public Account() {}

    public Account(UUID uuid) {
        this.uuid = uuid.toString();
        this.camp = Camp.CITOYEN;
        this.level = 0;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public int getLevel() {
        return level;
    }

    public String getRank() {
        return camp.getRank(level);
    }

    public String getName() {
        return Bukkit.getServer().getOfflinePlayer(uuid).getName();
    }

    public String getTeamName() {
        return team;
    }

    public Team getTeam() {
        return hasTeam() ? KinkoCore.teamManager.getTeam(team) : null;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean hasTeam() {
        return team != null;
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid=" + uuid +
                ", camp=" + camp +
                ", level=" + level +
                ", team=" + team +
                '}';
    }

}
