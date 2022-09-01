package fr.twizox.kinkocore.prime;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@DatabaseTable(tableName = "prime")
public class Prime implements Comparable<Prime> {

    @DatabaseField(id = true)
    private String target;
    @DatabaseField
    private int amount;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDateTime localDateTime;


    public Prime() {
    }

    public Prime(int amount, UUID target) {
        this.amount = amount;
        this.target = target.toString();
        this.localDateTime = ZonedDateTime.now(ZoneId.of("Europe/Paris")).toLocalDateTime();
    }

    public int getAmount() {
        return amount;
    }

    public UUID getTarget() {
        return UUID.fromString(target);
    }

    public String getTargetName() {
        return Bukkit.getOfflinePlayer(getTarget()).getName();
    }

    public LocalDateTime getLocalDate() {
        return localDateTime;
    }

    @Override
    public int compareTo(Prime prime) {
        return this.amount - prime.amount;
    }
}
