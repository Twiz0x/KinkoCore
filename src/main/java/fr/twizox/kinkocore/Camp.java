package fr.twizox.kinkocore;

public enum Camp {

    CITOYEN("Civil", "Baron", "Compte", "Duc", "Roi"),
    PIRATE("Matelot", "Pirate", "Supernova", "Corsaire", "Empereur"),
    MARINE("Soldat", "Sergent", "Lieutenant", "Vice-Amiral", "Amiral"),
    REBELLE("Rebelle", "Révolutionnaire", "Officier", "Commandant", "Capitaine");

    private final String[] ranks;
    private final String name;

    Camp(String name, String... ranks) {
        this.name = name;
        this.ranks = ranks;
    }

    public String getName() {
        return name;
    }

    public String getRank(int level) {
        return ranks[level];
    }

}
