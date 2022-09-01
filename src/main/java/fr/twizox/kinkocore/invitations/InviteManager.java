package fr.twizox.kinkocore.invitations;

import fr.twizox.kinkocore.account.Account;
import fr.twizox.kinkocore.account.AccountManager;
import fr.twizox.kinkocore.configurations.TeamConfiguration;
import fr.twizox.kinkocore.teams.Team;
import fr.twizox.kinkocore.teams.TeamManager;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.WeakHashMap;

public class InviteManager {

    private final WeakHashMap<Player, TeamInvitation> invitations;
    private final TeamConfiguration config;
    private final AccountManager accountManager;
    private final TeamManager teamManager;

    public InviteManager(TeamManager teamManager, AccountManager accountManager, TeamConfiguration teamConfiguration) {
        this.invitations = new WeakHashMap<>();
        this.config = teamConfiguration;
        this.teamManager = teamManager;
        this.accountManager = accountManager;
    }

    public boolean hasInvitations(Player inviter) {
        return invitations.containsKey(inviter);
    }

    public TeamInvitation getInvitations(Player inviter) {
        return invitations.get(inviter);
    }

    public int getCooldown(Player inviter, UUID targetUuid) {
        return config.invitationExpireSeconds - getInvitations(inviter).getTimeElapsed(targetUuid);
    }

    public boolean isInvitationExpired(TeamInvitation teamInvitation, UUID targetUuid) {
        return !teamInvitation.isInvited(targetUuid) || teamInvitation.getTimeElapsed(targetUuid) >= config.invitationExpireSeconds;
    }

    public boolean addInvitation(Player inviter, UUID targetUuid, Team team) {
        TeamInvitation teamInvitation = invitations.get(inviter);
        if (teamInvitation != null) {
            if (isInvitationExpired(teamInvitation, targetUuid)) {
                teamInvitation.addGuest(targetUuid);
                return true;
            }
            return false;
        }
        invitations.put(inviter, new TeamInvitation(team, targetUuid));
        return true;
    }

    public void deleteInvitations(Player inviter) {
        invitations.remove(inviter);
    }

    public boolean isInvitedBy(Player inviter, UUID guest) {
        return hasInvitations(inviter) && invitations.get(inviter).isInvited(guest);
    }

    public boolean canAcceptInvitation(Player inviter, UUID guest) {
        return isInvitedBy(inviter, guest) && !isInvitationExpired(invitations.get(inviter), guest);
    }

    public void acceptInvitation(Player inviter, UUID target) {
        TeamInvitation teamInvitation = invitations.get(inviter);
        Team team = teamInvitation.getTeam();

        teamInvitation.removeGuest(target);
        if (teamInvitation.getGuests().size() == 0) deleteInvitations(inviter);

        Account account = accountManager.getAccount(target);
        account.setTeam(team.getName());
        team.addMember(target);

        accountManager.saveAccount(account);
        teamManager.loadAndSaveTeam(team);
    }

}
