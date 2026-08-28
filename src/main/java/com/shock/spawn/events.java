package com.shock.spawn;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class events implements Listener {
    private final FileConfiguration file = YamlConfiguration.loadConfiguration(utils.spawnFile());

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(file.getBoolean("settings.join-tp")) {
            utils.teleport(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(file.getBoolean("settings.respawn-tp")) {
            utils.teleport(event.getPlayer());
        }
    }
}
