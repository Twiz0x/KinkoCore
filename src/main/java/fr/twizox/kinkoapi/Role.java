package fr.twizox.kinkoapi;

public enum Role {

    CITOYEN("Citoyen"),
    REBELLE("Rebelle"),
    SOLDAT("Soldat"),
    PIRATE("Pirate"),;

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }
}
