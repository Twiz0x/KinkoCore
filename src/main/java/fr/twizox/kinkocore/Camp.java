package fr.twizox.kinkocore;

import org.apache.commons.lang.StringUtils;

public enum Camp {

    CITOYEN("Civil", "Baron", "Compte", "Duc", "Roi"),
    PIRATE("Matelot", "Pirate", "Supernova", "Corsaire", "Empereur"),
    MARINE("Soldat", "Sergent", "Lieutenant", "Vice-Amiral", "Amiral"),
    REBELLE("Rebelle", "Révolutionnaire", "Officier", "Commandant", "Capitaine");

    private final String[] ranks;

    Camp(String... ranks) {
        this.ranks = ranks;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(name().toLowerCase());
    }

    public String getRank(int level) {
        return ranks[level];
    }

}
