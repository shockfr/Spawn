package com.shock.spawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public final class events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(main.getInstance().getConfig().getBoolean("settings.join-tp")) {
            utils.send("e", event.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    utils.teleport(event.getPlayer());
                    cancel();
                }
            }.runTaskTimer(main.getInstance(), 1L, 0L);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(main.getInstance().getConfig().getBoolean("settings.respawn-tp")) {
            utils.send("e", event.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    utils.teleport(event.getPlayer());
                    cancel();
                }
            }.runTaskTimer(main.getInstance(), 1L, 0L);
        }
    }
}
