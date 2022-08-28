package fr.twizox.kinkocore.invitations;

import fr.twizox.kinkocore.teams.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TeamInvitation {

    private final Team team;
    /**
     * guests is a {@link Map} collection that contains a target {@link UUID} as
     * key and the invitation time in milliseconds {@link Long} as value.
     */
    private final Map<UUID, Long> guests;

    public TeamInvitation(Team team, UUID guest) {
        this.team = team;
        this.guests = new HashMap<>();
        guests.put(guest, System.currentTimeMillis());
    }

    public Team getTeam() {
        return team;
    }

    public boolean isInvited(UUID guest) {
        return guests.containsKey(guest);
    }

    public Set<UUID> getGuests() {
        return guests.keySet();
    }

    public void addGuest(UUID guest) {
        guests.put(guest, System.currentTimeMillis());
    }

    public void removeGuest(UUID guest) {
        guests.remove(guest);
    }

    public int getTimeElapsed(UUID guest) {
        return (int) (System.currentTimeMillis() - guests.get(guest)) / 1000;
    }

}
