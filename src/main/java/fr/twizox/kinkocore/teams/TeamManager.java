package fr.twizox.kinkocore.teams;

import com.j256.ormlite.dao.Dao;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TeamManager {

    private final Dao<Team, String> dao;
    private final ConcurrentHashMap<String, Team> teams;

    public TeamManager(Dao<Team, String> dao) {
        this.teams = new ConcurrentHashMap<>();
        this.dao = dao;
    }

    public ConcurrentHashMap<String, Team> getTeams() {
        return teams;
    }

    public Map<String, Team> getPlayerDataMap() {
        return teams;
    }

    public Team getTeam(String teamId) {
        return teams.get(teamId);
    }

    public Optional<Team> getTeamOptional(String teamId) {
        return Optional.ofNullable(teams.get(teamId));
    }

    public CompletableFuture<Team> getDatabaseTeam(String teamName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return dao.queryForId(teamName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loadDatabaseTeam(String teamName) {
        if (teams.containsKey(teamName)) return;
        getDatabaseTeam(teamName).thenApply(team -> teams.put(teamName, team));
    }

    public void saveTeam(Team team) {
        cache(team);
        CompletableFuture.runAsync(() -> {
            try {
                dao.createOrUpdate(team);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void deleteTeam(Team team) {
        uncache(team.getName());

        CompletableFuture.runAsync(() -> {
            for (UUID uuid : team.getMembers()) {
                Account account = KinkoCore.accountManager.getAccount(uuid);
                if (account == null) {
                    KinkoCore.accountManager.getAccountFromDatabase(uuid).thenAccept((loadedAccount) -> {
                        loadedAccount.setTeam(null);
                        KinkoCore.accountManager.saveAccount(loadedAccount);
                    });
                } else {
                    account.setTeam(null);
                    KinkoCore.accountManager.saveAccount(account);
                }
            }
            try {
                dao.delete(team);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void cache(Team team) {
        teams.put(team.getName(), team);
    }

    public void uncache(String teamId) {
        teams.remove(teamId);
    }

    public Collection<String> getTeamsName() {
        return teams.keySet();
    }

    public boolean isExistingTeam(String teamName) {
        return teams.containsKey(teamName);
    }

}
