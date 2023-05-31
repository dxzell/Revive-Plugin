package com.dxzell.revive.Settings;

import com.dxzell.revive.Revive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Settings {

    private Revive main;
    private Inventory settingsInv;
    private Inventory timeInv;

    private Inventory detectionRangeInv;
    public Settings (Revive main)
    {

        this.main = main;

        //SETTINGS GUI
        settingsInv = Bukkit.createInventory(null, 27, ChatColor.WHITE + "Settings " + ChatColor.BLACK + ">>");
        for(int i = 0; i < 27; i++)
        {
            settingsInv.setItem(i, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ", false));
        }
        settingsInv.setItem(11, main.buildItemStack(Material.CLOCK, ChatColor.AQUA + "Time", ChatColor.GRAY + "The player can be revived°" + ChatColor.GRAY + "in that time.°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + main.getConfigClass().getTime()/60 + ChatColor.GOLD + ":" + (main.getConfigClass().getTime()%60 >= 10 ? "" + ChatColor.GOLD + main.getConfigClass().getTime()%60 : ChatColor.GOLD+ "0" +  ChatColor.GOLD + main.getConfigClass().getTime()%60) + (main.getConfigClass().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") +"°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(12, main.buildItemStack((main.getConfigClass().getMobDamage() ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK), ChatColor.RED + "Mob damage",  (main.getConfigClass().getMobDamage() ? ChatColor.GRAY + "Mobs " + ChatColor.RED + "can " + ChatColor.GRAY + "finish/kill°" + ChatColor.GRAY + "knocked players°°" + ChatColor.YELLOW + "[Click to toggle]" : ChatColor.GRAY + "Mobs " + ChatColor.GREEN + "cannot " + ChatColor.GRAY + "finish/kill°" + ChatColor.GRAY + "knocked players°°" + ChatColor.YELLOW + "[Click to toggle]"), true));
        settingsInv.setItem(13, main.buildItemStack(Material.getMaterial(main.getConfigClass().getType()), ChatColor.DARK_GRAY + "Item_Type", ChatColor.GRAY + "The player can only get revived°" + ChatColor.GRAY + "with the following item: °" + ChatColor.GOLD + main.getConfigClass().getType() + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(14, main.buildItemStack(Material.COMPASS, ChatColor.GOLD + "Detection range", ChatColor.GRAY + "This is the range in which a player°" + ChatColor.GRAY + "is being recognized as " + ChatColor.DARK_GRAY + "'nearby'°°" + ChatColor.GRAY + "Currently at: " + ChatColor.GOLD + main.getConfigClass().getDetectionRange() + ChatColor.GRAY + " blocks from°" + ChatColor.GRAY + "the knocked players location°" + ChatColor.GRAY + "into every direction.°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(15, main.buildItemStack((main.getConfigClass().getAnimation() ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA), (main.getConfigClass().getAnimation() ?ChatColor.GREEN + "Animation" : ChatColor.RED + "Animation"), ChatColor.GRAY + "Show death and revival animation: °" + (main.getConfigClass().getAnimation() ? ChatColor.GREEN + "true" : ChatColor.RED + "false") + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        //END

        //TIME GUI
        timeInv = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Length " + ChatColor.BLACK + ">>");
        for(int i = 0; i < 9; i++)
        {
            timeInv.setItem(i, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ", false));
        }
        timeInv.setItem(1, main.buildItemStack(Material.REDSTONE_TORCH, ChatColor.RED + "-", ChatColor.GRAY + "Lower the time by°" + ChatColor.DARK_GRAY + "30 " + ChatColor.GRAY + "seconds°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD +  main.getConfigClass().getTime()/60 + ChatColor.GOLD + ":" + (main.getConfigClass().getTime()%60 >= 10 ? "" + ChatColor.GOLD + main.getConfigClass().getTime()%60 : ChatColor.GOLD + "0" +  ChatColor.GOLD + main.getConfigClass().getTime()%60)+ (main.getConfigClass().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to lower]", true));
        timeInv.setItem(4, main.buildItemStack(Material.EMERALD_BLOCK, ChatColor.GOLD + "Return to settings", "°" + ChatColor.YELLOW + "[Click to return]", true));
        timeInv.setItem(7, main.buildItemStack(Material.TORCH, ChatColor.GREEN + "+", ChatColor.GRAY + "Higher the time by°" + ChatColor.DARK_GRAY + "30 " + ChatColor.GRAY + "seconds°°" + ChatColor.GRAY + "Current time: "  + ChatColor.GOLD + main.getConfigClass().getTime()/60 + ChatColor.GOLD + ":" + (main.getConfigClass().getTime()%60 >= 10 ? "" + ChatColor.GOLD + main.getConfigClass().getTime()%60 : ChatColor.GOLD+ "0" +  ChatColor.GOLD + main.getConfigClass().getTime()%60) + (main.getConfigClass().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to higher]", true));
        //END

        //RANGE GUI
        detectionRangeInv = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Detection range " + ChatColor.BLACK + ">>");
        for(int i = 0; i < 9; i++)
        {
            detectionRangeInv.setItem(i, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ", false));
        }
        detectionRangeInv.setItem(1, main.buildItemStack(Material.REDSTONE_TORCH, ChatColor.RED + "-", ChatColor.GRAY + "Lower the detection range by°" + ChatColor.DARK_GRAY + "5 " + ChatColor.GRAY + "blocks°°" + ChatColor.GRAY + "Current detection range: " + ChatColor.GOLD + main.getConfigClass().getDetectionRange() + ChatColor.GRAY + " blocks" + "°°" + ChatColor.YELLOW + "[Click to lower]", true));
        detectionRangeInv.setItem(4, main.buildItemStack(Material.EMERALD_BLOCK, ChatColor.GOLD + "Return to settings", "°" + ChatColor.YELLOW + "[Click to return]", true));
        detectionRangeInv.setItem(7, main.buildItemStack(Material.TORCH, ChatColor.GREEN + "+", ChatColor.GRAY + "Higher the detection range°" + ChatColor.DARK_GRAY + "5 " + ChatColor.GRAY + "blocks°°" + ChatColor.GRAY + "Current detection range: "  + ChatColor.GOLD + main.getConfigClass().getDetectionRange() + ChatColor.GRAY + " blocks" + "°°" +  ChatColor.YELLOW + "[Click to higher]", true));

    }

    //UPDATE
    public void updateSettingsInv()
    {
        settingsInv.setItem(11, main.buildItemStack(Material.CLOCK, ChatColor.AQUA + "Time", ChatColor.GRAY + "The player can be revived°" + ChatColor.GRAY + "in that time.°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + main.getConfigClass().getTime()/60 + ChatColor.GOLD + ":" + (main.getConfigClass().getTime()%60 >= 10 ? "" + ChatColor.GOLD + main.getConfigClass().getTime()%60 : ChatColor.GOLD+ "0" +  ChatColor.GOLD + main.getConfigClass().getTime()%60) + (main.getConfigClass().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") +"°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(12, main.buildItemStack((main.getConfigClass().getMobDamage() ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK), ChatColor.RED + "Mob damage",  (main.getConfigClass().getMobDamage() ? ChatColor.GRAY + "Mobs " + ChatColor.RED + "can " + ChatColor.GRAY + "finish/kill°" + ChatColor.GRAY + "knocked players°°" + ChatColor.YELLOW + "[Click to toggle]" : ChatColor.GRAY + "Mobs " + ChatColor.GREEN + "cannot " + ChatColor.GRAY + "finish/kill°" + ChatColor.GRAY + "knocked players°°" + ChatColor.YELLOW + "[Click to toggle]"), true));
        settingsInv.setItem(13, main.buildItemStack(Material.getMaterial(main.getConfigClass().getType()), ChatColor.DARK_GRAY + "Item_Type", ChatColor.GRAY + "The player can only get revived°" + ChatColor.GRAY + "with the following item: °" + ChatColor.GOLD + main.getConfigClass().getType() + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(14, main.buildItemStack(Material.COMPASS, ChatColor.GOLD + "Detection range", ChatColor.GRAY + "This is the range in which a player°" + ChatColor.GRAY + "is being recognized as " + ChatColor.DARK_GRAY + "'nearby'°°" + ChatColor.GRAY + "Currently at: " + ChatColor.GOLD + main.getConfigClass().getDetectionRange() + ChatColor.GRAY + " blocks from°" +  ChatColor.GRAY + "the knocked players location°" + ChatColor.GRAY + "into every direction.°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(15, main.buildItemStack((main.getConfigClass().getAnimation() ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA), (main.getConfigClass().getAnimation() ? ChatColor.GREEN + "Animation" : ChatColor.RED + "Animation"), ChatColor.GRAY + "Show death and revival animation: °" + (main.getConfigClass().getAnimation() ? ChatColor.GREEN + "true" : ChatColor.RED + "false") + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
    }

    public void updateTimeInv()
    {
        timeInv.setItem(1, main.buildItemStack(Material.REDSTONE_TORCH, ChatColor.RED + "-", ChatColor.GRAY + "Lower the time by°" + ChatColor.DARK_GRAY + "30 " + ChatColor.GRAY + "seconds°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + main.getConfigClass().getTime()/60 + ChatColor.GOLD + ":" + (main.getConfigClass().getTime()%60 >= 10 ? "" + ChatColor.GOLD + main.getConfigClass().getTime()%60 : ChatColor.GOLD+ "0" +  ChatColor.GOLD + main.getConfigClass().getTime()%60) + (main.getConfigClass().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to lower]", true));
        timeInv.setItem(4, main.buildItemStack(Material.EMERALD_BLOCK, ChatColor.GOLD + "Return to settings", "°" + ChatColor.YELLOW + "[Click to return]", true));
        timeInv.setItem(7, main.buildItemStack(Material.TORCH, ChatColor.GREEN + "+", ChatColor.GRAY + "Higher the time by°" + ChatColor.DARK_GRAY + "30 " + ChatColor.GRAY + "seconds°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + main.getConfigClass().getTime()/60 + ChatColor.GOLD + ":" + (main.getConfigClass().getTime()%60 >= 10 ? "" + ChatColor.GOLD + main.getConfigClass().getTime()%60 : ChatColor.GOLD+ "0" +  ChatColor.GOLD + main.getConfigClass().getTime()%60) + (main.getConfigClass().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to higher]", true));
    }

    public void updateDetectionRangeInv()
    {
        detectionRangeInv.setItem(1, main.buildItemStack(Material.REDSTONE_TORCH, ChatColor.RED + "-", ChatColor.GRAY + "Lower the detection range by°" + ChatColor.DARK_GRAY + "5 " + ChatColor.GRAY + "blocks°°" + ChatColor.GRAY + "Current detection range: " + ChatColor.GOLD + main.getConfigClass().getDetectionRange() + ChatColor.GRAY + " blocks" + "°°" + ChatColor.YELLOW + "[Click to lower]", true));
        detectionRangeInv.setItem(4, main.buildItemStack(Material.EMERALD_BLOCK, ChatColor.GOLD + "Return to settings", "°" + ChatColor.YELLOW + "[Click to return]", true));
        detectionRangeInv.setItem(7, main.buildItemStack(Material.TORCH, ChatColor.GREEN + "+", ChatColor.GRAY + "Higher the detection range°" + ChatColor.DARK_GRAY + "5 " + ChatColor.GRAY + "blocks°°" + ChatColor.GRAY + "Current detection range: "  + ChatColor.GOLD + main.getConfigClass().getDetectionRange() + ChatColor.GRAY + " blocks" + "°°" +  ChatColor.YELLOW + "[Click to higher]", true));
    }
    //END

    public void openSettingsInv(Player player)
    {
        player.openInventory(settingsInv);
    }
    public void openTimeInv(Player player)
    {
        player.openInventory(timeInv);
    }

    public void openDetectionRangeInv(Player player){player.openInventory(detectionRangeInv);}
    public Inventory getSettingsInv()
    {
        return settingsInv;
    }
    public Inventory getTimeInv()
    {
        return timeInv;
    }

    public Inventory getDetectionRangeInv(){return detectionRangeInv;}
    public Revive getMain()
    {
        return main;
    }

}
