package fr.twizox.kinkoapi.account;

import fr.twizox.kinkoapi.Role;

import java.util.UUID;

public class Account {

    private final UUID uuid;
    private final Role role;

    public Account(UUID uuid, Role role) {
        this.uuid = uuid;
        this.role = role;
    }
}
