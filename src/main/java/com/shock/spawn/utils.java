package com.shock.spawn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public final class utils {
    @SuppressWarnings("FieldMayBeFinal")
    private static String prefix = "&7[&6Spawn&7]&r ";
    
    public static void playSound(String path, Player player) {
        String sound = main.getInstance().getConfig().getString(path);
        if(sound == null) return;
        Key key = Key.key(sound);
        player.playSound(
            Sound.sound(
                key,
                Sound.Source.MASTER,
                1.0f,
                1.0f
            ),
            player
        );
    }

    public static void log(String msg, Object... object) {
        String log = String.format(msg, object);
        Bukkit.getConsoleSender().sendMessage(translate(ChatColor
            .translateAlternateColorCodes('&', prefix + log)));
    }

    public static void send(String msg, CommandSender player) {
        player.sendMessage(translate(ChatColor
            .translateAlternateColorCodes('&', prefix + msg)));
    }

    public static String getMsg(String path) {
        return translate(ChatColor
            .translateAlternateColorCodes('&', main
            .getInstance().getConfig().getString(path)));
    }

    public static void initTeleport(Player player) {

        Sound.sound();
        File file = new File(main.getInstance().getDataFolder(), "spawn.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if(!(cfg.contains("x"))) {
            utils.send(utils.getMsg("messages.no-spawn"), player);
            return;
        }

        Location initLocation = player.getLocation();

        new BukkitRunnable() {
            private int checks = main.getInstance()
                .getConfig().getInt("settings.delay");
            private final Location startLocation = initLocation;

            @Override
            public void run() {
                try {
                    if (!(player.isOnline())) {
                        cancel();
                        return;
                    }

                    Location currentLocation = player.getLocation();
                    double movedDistance = startLocation.distance(currentLocation);

                    if (movedDistance >= 0.5D) {
                        utils.send(utils.getMsg("messages.tp-cancel"), player);
                        utils.playSound("sounds.tp-cancel", player);
                        cancel();
                        return;
                    }

                    if (checks <= 0) {
                        teleport(player);

                        utils.playSound("sounds.teleport", player);
                        String successText = utils.getMsg("messages.teleport");

                        player.sendActionBar(Component.text(successText));

                        utils.send(successText, player);

                        cancel();
                        return;
                    }

                    utils.playSound("sounds.teleporting", player);
                    String teleportingText = utils.getMsg("messages.teleporting")
                        .replace("{time}", Integer.toString(checks));

                    player.sendActionBar(Component.text(teleportingText));

                    checks--;
                } catch (IllegalStateException e) {
                    utils.log("Error during teleport countdown for %s: %s", player.getName(), e.getMessage());
                    cancel();
                }
            }
        }.runTaskTimer(main.getInstance(), 0L, 20L);
    }

    public static void teleport(Player player) {
        FileConfiguration file = YamlConfiguration.loadConfiguration(spawnFile());
        try {
            String worldName = file.getString("world");
            if(worldName == null) {
                utils.send("An error occurred while teleporting you to spawn.", player);
                return;
            }

            World world = Bukkit.getWorld(worldName);
            if(world == null) {
                utils.send("The world for spawn could not be found.", player);
                return;
            }

            double x = file.getDouble("x");
            double y = file.getDouble("y");
            double z = file.getDouble("z");
            double yaw = file.getDouble("yaw");
            double pitch = file.getDouble("pitch");
            Location loc = new Location(world, x, y, z);
            loc.setRotation((float) yaw, (float) pitch);
            player.teleport(loc);
        } catch(Exception e) {
            utils.log("Failed to teleport " + player.getName() + ": " + e.getMessage());
            utils.send("An error occurred while teleporting to spawn.", player);
        }
    }

    //This is basically a copy of LPC's translator (Without bukkit codes)
    public static String translate(String msg) {
        final char colorchar = ChatColor.COLOR_CHAR;
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(msg);
        StringBuffer buffer = new StringBuffer(msg.length() + 4 * 8);
        while(matcher.find()) {
            final String group = matcher.group(1);
            matcher.appendReplacement(buffer, colorchar + "x"
                + colorchar + group.charAt(0) + colorchar + group.charAt(1)
                + colorchar + group.charAt(2) + colorchar + group.charAt(3)
                + colorchar + group.charAt(4) + colorchar + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

    public static List<String> completions(List<String> completions, String arg) {
        List<String> matches = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, completions, matches);
        Collections.sort(matches);
        return matches;
    }
    
    public static File spawnFile() {
        File file = new File(main.getInstance().getDataFolder(), "spawn.yml");
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new IOException("Could not create plugin data folder");
            }
            if (file.createNewFile()) {
                utils.log("Created spawn.yml");
            }
        } catch (IOException e) {
            utils.log("Failed to create spawn.yml: %s", e.getMessage());
        }
        return file;
    }

    public static double round(double num, int e) {
        double factor = Math.pow(10, e);
        return Math.round(num * factor) / factor;
    }
}
