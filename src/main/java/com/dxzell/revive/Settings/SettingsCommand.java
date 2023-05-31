package com.dxzell.revive.Settings;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {

    private Settings settings;

    public SettingsCommand(Settings settings)
    {
        this.settings = settings;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("revive"))
            {
                if(args.length == 1)
                {
                    if(args[0].equalsIgnoreCase("settings")) {
                        settings.openSettingsInv(player);
                    }else if(args[0].equalsIgnoreCase("message"))
                    {
                        if(player.hasPermission("revive.admin")) {
                            settings.getMain().getInv().openInv(player);
                        }else{
                            player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.RED + "Missing permission!");
                        }

                    }else{
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', settings.getMain().getConfigClass().getWrongUsage()));
                    }
                }else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', settings.getMain().getConfigClass().getWrongUsage()));
                }
            }
        }
        return false;
    }

}
