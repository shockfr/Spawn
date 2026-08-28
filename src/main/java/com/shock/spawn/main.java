package com.shock.spawn;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    private static main instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("setspawn").setExecutor(new setspawn());
        spawn spawnCommand = new spawn();
        getCommand("spawn").setExecutor(spawnCommand);
        getCommand("spawn").setTabCompleter(spawnCommand);
        getServer().getPluginManager().registerEvents(new events(), this);
        utils.log("==============================");
        utils.log(" ");
        utils.log("Thank you for using Spawn v%s!", this.getPluginMeta().getVersion());
        utils.log("https:/github.com/shockfr/Spawn");
        utils.log(" ");
        utils.log("==============================");
        saveDefaultConfig();
        utils.spawnFile();
    }

    public static main getInstance() {
        return instance;
    }
}
