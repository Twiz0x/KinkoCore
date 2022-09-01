package fr.twizox.kinkocore.teams;

import com.j256.ormlite.dao.Dao;
import fr.twizox.kinkocore.AbstractManager;
import fr.twizox.kinkocore.KinkoCore;
import fr.twizox.kinkocore.account.Account;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TeamManager extends AbstractManager<Team> {

    public TeamManager(Dao<Team, String> dao) {
        super(dao);
    }

    public ConcurrentHashMap<String, Team> getTeams() {
        return super.getEntities();
    }

    public Team getTeam(String id) {
        return super.getEntity(id);
    }

    public Optional<Team> getTeamOptional(String id) {
        return super.getOptionalEntity(id);
    }

    public CompletableFuture<Team> getDatabaseTeam(String id) {
        return super.getEntityFromDatabase(id);
    }

    public void loadTeam(String id) {
        super.loadEntity(id);
    }

    public void saveTeam(Team team) {
        super.saveEntity(team);
    }

    public void loadAndSaveTeam(Team team) {
        super.loadAndSave(team.getName(), team);
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
            super.deleteEntity(team);
        });
    }

    @Override
    public void cache(Team team) {
        super.cache(team.getName(), team);
    }

    public void uncache(String teamId) {
        super.uncache(teamId);
    }

    public Collection<String> getTeamsName() {
        return super.getEntities().keySet();
    }

    public boolean isExistingTeam(String teamName) {
        return super.isCached(teamName);
    }

}
