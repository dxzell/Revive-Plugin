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
                 if(args[0].equalsIgnoreCase("settings"))
                 {
                    settings.openSettingsInv(player);
                 }else{
                     player.sendMessage(ChatColor.RED + "Wrong usage of the command /revive settings");
                 }
                }else{
                    player.sendMessage(ChatColor.RED + "Wrong usage of the command /revive settings");
                }
            }
        }
        return false;
    }

}
