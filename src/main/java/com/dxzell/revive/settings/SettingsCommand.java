package com.dxzell.revive.settings;

import com.dxzell.revive.configs.MessagesConfig;
import com.dxzell.revive.configs.SettingsConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {

    private Settings settings;

    public SettingsCommand(Settings settings) {
        this.settings = settings;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("revive")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("settings")) {
                        if (player.hasPermission(SettingsConfig.getInstance().getPermission())) {
                            settings.openSettingsInv(player);
                        } else {
                            player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.RED + "Missing permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission(SettingsConfig.getInstance().getPermission())) {
                            settings.getMain().getDownedPlayer().resetAllMaps();
                        } else {
                            player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.RED + "Missing permission!");
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getWrongUsage()));
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getWrongUsage()));
                }
            }
        }
        return false;
    }


}
