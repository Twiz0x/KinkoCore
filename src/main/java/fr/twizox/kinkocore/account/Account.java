package fr.twizox.kinkocore.account;

import com.j256.ormlite.field.DatabaseField;
import fr.twizox.kinkocore.Camp;
import org.bukkit.Bukkit;

import java.util.UUID;

public class Account {

    @DatabaseField(id = true)
    private UUID uuid;
    @DatabaseField
    private Camp camp;
    @DatabaseField
    private int level;

    public Account() {}

    public Account(UUID uuid) {
        this.uuid = uuid;
        this.camp = Camp.CITOYEN;
        this.level = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Camp getCamp() {
        return camp;
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

}
