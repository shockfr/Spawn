package com.shock.spawn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


public final class spawn implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0 && sender.hasPermission("spawn.admin")) {
            if(args[0].equalsIgnoreCase("reload")) {
                main.getInstance().reloadConfig();
                utils.send(utils.getMsg("messages.reload"), sender);
                return true;
            } else if(args[0].equalsIgnoreCase("tp") && args.length > 1) {
                Player arg = Bukkit.getPlayer(args[1]);
                if(sender instanceof Player player) {
                    if(arg != null) {
                        utils.playSound("sounds.success", player);
                    } else {
                        utils.playSound("sounds.fail", player);
                    }
                }
                if(arg != null) {
                    utils.teleport(arg);
                    utils.send(utils.getMsg("messages.tp-other")
                        .replace("{player}", args[1]), sender);

                } else {
                    utils.send(utils.getMsg("messages.tp-other-fail")
                        .replace("{player}", args[1]), sender);
                }
                return true;
            }
        }
        if(sender instanceof Player player) {
            utils.initTeleport(player);
        } else {
            utils.send(utils.getMsg("label"), sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("spawn.admin")) {
            if(args.length == 1) {
                return utils.completions(List.of("reload", "tp"), args[0]);
            }

            if(args.length == 2 && args[0].equalsIgnoreCase("tp")
                    && sender.hasPermission("spawn.reload")) {
                return utils.completions(Bukkit.getOnlinePlayers()
                    .stream().map(Player::getName)
                    .collect(Collectors.toList()), args[1]);
            }
        }
        return new ArrayList<>();
    }
}
