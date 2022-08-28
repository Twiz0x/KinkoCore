package fr.twizox.kinkocore.configurations;

import ru.xezard.configurations.Configuration;
import ru.xezard.configurations.ConfigurationComments;
import ru.xezard.configurations.ConfigurationField;

import java.io.File;
import java.util.HashMap;

public class PlaceholdersConfiguration extends Configuration {

    // TODO: Remove XConfiguration & create a standard config from bukkit.

    @ConfigurationComments("Quand le placeholders n'est pas valide/valeur introuvable")
    @ConfigurationField("Non-trouvé")
    public String notFound = "§6Chargement...";

    @ConfigurationComments("Configuration des couleurs des camps.")
    @ConfigurationField("Couleurs")
    public HashMap<String, String> colors = new HashMap<String, String>() {{
        put("CITOYEN", "§7");
        put("PIRATE", "§2");
        put("MARINE", "§9");
        put("REBELLE", "§5");
    }};

    public PlaceholdersConfiguration(String pathToFile) {
        super(pathToFile + File.separator + "placeholders.yml");
    }

}
