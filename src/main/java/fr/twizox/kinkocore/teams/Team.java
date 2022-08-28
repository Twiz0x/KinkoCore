package fr.twizox.kinkocore.teams;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@DatabaseTable(tableName = "team")
public class Team {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private UUID owner;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<UUID> members;

    public Team() {
    }

    public Team(String name, UUID owner) {
        this(name, owner, new ArrayList<>());
    }

    /**
     * Team constructor, members shouldn't contain the owner UUID
     * */
    public Team(String name, UUID owner, ArrayList<UUID> members) {
        this.owner = owner;
        this.name = name;
        members.add(owner);
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
    }

    public boolean hasMember(UUID uuid) {
        return this.members.contains(uuid);
    }

    public boolean isOwner(UUID uuid) {
        return uuid.equals(owner);
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
    }

    public UUID getOwner() {
        return owner;
    }

    public Collection<UUID> getMembers() {
        return members;
    }

}
