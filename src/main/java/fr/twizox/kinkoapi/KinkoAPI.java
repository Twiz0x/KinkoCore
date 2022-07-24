package fr.twizox.kinkoapi;

import org.bukkit.plugin.java.JavaPlugin;

public final class KinkoAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("On");
    }

    @Override
    public void onDisable() {
        getLogger().info("Off");
    }
}
