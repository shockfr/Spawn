package com.shock.spawn;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public final class setspawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            FileConfiguration file = YamlConfiguration.loadConfiguration(utils.spawnFile());
            Location location = player.getLocation();
            file.set("x", utils.round(location.getX(), 2));
            file.set("y", utils.round(location.getY(), 2));
            file.set("z", utils.round(location.getZ(), 2));
            file.set("yaw", utils.round(location.getYaw(), 2));
            file.set("pitch", utils.round(location.getPitch(), 2));
            file.set("world", location.getWorld().getName());
            try {
                file.save(utils.spawnFile());
            } catch (IOException e) {
                utils.log("Failed to save spawn: %s", e.getMessage());
                utils.send("An error occurred while setting spawn.", player);
                return true;
            }
            utils.send(utils.getMsg("messages.set-spawn"), player);
            utils.playSound("sounds.success", player);
        } else {
            utils.send(utils.getMsg("messages.non-player"), sender);
        }
        return true;
    }
}
