package com.dxzell.revive.settings;

import com.dxzell.revive.Revive;
import com.dxzell.revive.configs.SettingsConfig;
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

    public Settings(Revive main) {
        this.main = main;

        //SETTINGS GUI
        settingsInv = Bukkit.createInventory(null, 45, ChatColor.WHITE + "Settings " + ChatColor.BLACK + ">>");
        for (int i = 0; i < settingsInv.getSize(); i++) {
            settingsInv.setItem(i, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ", false));
        }
        updateSettingsInv();
        //END

        //TIME GUI
        timeInv = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Length " + ChatColor.BLACK + ">>");
        for (int i = 0; i < 9; i++) {
            timeInv.setItem(i, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ", false));
        }
        updateTimeInv();
        //END

        //RANGE GUI
        detectionRangeInv = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Detection range " + ChatColor.BLACK + ">>");
        for (int i = 0; i < 9; i++) {
            detectionRangeInv.setItem(i, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ", false));
        }
        updateDetectionRangeInv();
    }

    //UPDATE
    public void updateSettingsInv() {
        settingsInv.setItem(12, main.buildItemStack(Material.CLOCK, ChatColor.AQUA + "Time", ChatColor.GRAY + "The player can be revived°" + ChatColor.GRAY + "in that time.°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + SettingsConfig.getInstance().getTime() / 60 + ChatColor.GOLD + ":" + (SettingsConfig.getInstance().getTime() % 60 >= 10 ? "" + ChatColor.GOLD + SettingsConfig.getInstance().getTime() % 60 : ChatColor.GOLD + "0" + ChatColor.GOLD + SettingsConfig.getInstance().getTime() % 60) + (SettingsConfig.getInstance().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(13, main.buildItemStack(Material.ZOMBIE_SPAWN_EGG, (SettingsConfig.getInstance().getMobDamage() ? ChatColor.GREEN : ChatColor.RED) + "Mob damage", (SettingsConfig.getInstance().getMobDamage() ? ChatColor.GRAY + "Mobs " + ChatColor.GREEN + "can " + ChatColor.GRAY + "finish/kill°" + ChatColor.GRAY + "knocked players°°" + ChatColor.YELLOW + "[Click to toggle]" : ChatColor.GRAY + "Mobs " + ChatColor.RED + "can't " + ChatColor.GRAY + "finish/kill°" + ChatColor.GRAY + "knocked players°°" + ChatColor.YELLOW + "[Click to toggle]"), true));
        settingsInv.setItem(14, main.buildItemStack(Material.getMaterial(SettingsConfig.getInstance().getType()), ChatColor.DARK_GRAY + "Item_Type", ChatColor.GRAY + "The player can only get revived°" + ChatColor.GRAY + "with the following item: °" + ChatColor.GOLD + SettingsConfig.getInstance().getType() + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(20, main.buildItemStack(Material.COMPASS, ChatColor.GOLD + "Detection range", ChatColor.GRAY + "This is the range in which a player°" + ChatColor.GRAY + "is being recognized as " + ChatColor.DARK_GRAY + "'nearby'°°" + ChatColor.GRAY + "Currently at: " + ChatColor.GOLD + SettingsConfig.getInstance().getDetectionRange() + ChatColor.GRAY + " blocks from°" + ChatColor.GRAY + "the knocked players location°" + ChatColor.GRAY + "into every direction.°°" + ChatColor.YELLOW + "[Click to edit]", true));
        settingsInv.setItem(21, main.buildItemStack(Material.ITEM_FRAME, (SettingsConfig.getInstance().getAnimation() ? ChatColor.GREEN + "Animation" : ChatColor.RED + "Animation"),   (SettingsConfig.getInstance().getAnimation() ? ChatColor.GREEN + "Show" : ChatColor.RED + "Don't show")+ChatColor.GRAY + " death and revival animation." + "°°" + ChatColor.YELLOW + "[Click to toggle]", true));
        settingsInv.setItem(22, main.buildItemStack(Material.APPLE, (SettingsConfig.getInstance().getNoItem() ? ChatColor.GREEN + "No Item" : ChatColor.RED + "No Item"), (SettingsConfig.getInstance().getNoItem() ? ChatColor.GREEN + "No item" : ChatColor.RED + "Item") + ChatColor.GRAY + " needed to°" + ChatColor.GRAY + "revive a knocked player.°" + ChatColor.GRAY + "Right clicking " + (SettingsConfig.getInstance().getNoItem() ? ChatColor.GREEN + "is" : ChatColor.RED + "isn't") + " enough."  + "°°" + ChatColor.YELLOW + "[Click to toggle]", true));
        settingsInv.setItem(23, main.buildItemStack(Material.PLAYER_HEAD, (SettingsConfig.getInstance().getTombstone() ? ChatColor.GREEN + "Tombstone" : ChatColor.RED + "Tombstone"), (SettingsConfig.getInstance().getTombstone() ? ChatColor.GREEN + "Spawn" : ChatColor.RED + "Don't spawn") + ChatColor.GRAY + " tombstone after death." + "°°" + ChatColor.YELLOW + "[Click to toggle]", true));
        settingsInv.setItem(24, main.buildItemStack(Material.TOTEM_OF_UNDYING, (SettingsConfig.getInstance().getTotem() ? ChatColor.GREEN + "Totem" : ChatColor.RED + "Totem"), (SettingsConfig.getInstance().getTotem() ? ChatColor.GREEN + "Still" : ChatColor.RED + "Don't") + ChatColor.GRAY + " get knocked with°" + ChatColor.GRAY + "a totem in your hand." + "°°" + ChatColor.YELLOW + "[Click to toggle]", true));
        settingsInv.setItem(30, main.buildItemStack(Material.IRON_BARS, (SettingsConfig.getInstance().getStealingAllowed() ? ChatColor.GREEN + "Stealing" : ChatColor.RED + "Stealing"), ChatColor.GRAY + "Other players " + (SettingsConfig.getInstance().getStealingAllowed() ? ChatColor.GREEN + "can" : ChatColor.RED + "can't") + " steal from°" + ChatColor.GRAY + "a knocked players inventory by°" + ChatColor.GRAY + "sneaking and right clicking°" + ChatColor.GRAY + "at the same time." + "°°" + ChatColor.YELLOW + "[Click to toggle]", false));
        settingsInv.setItem(31, main.buildItemStack(Material.NETHERITE_HELMET, (SettingsConfig.getInstance().getBlindness() ? ChatColor.GREEN + "Blindness" : ChatColor.RED + "Blindness"), ChatColor.GRAY + "Knocked players " + (SettingsConfig.getInstance().getBlindness() ? ChatColor.GREEN + "will " : ChatColor.RED + "won't ") + "°" + ChatColor.GRAY + "get the Blindness effect." + "°°" + ChatColor.YELLOW + "[Click to toggle]", false));
        settingsInv.setItem(32, main.buildItemStack(Material.COMMAND_BLOCK, (SettingsConfig.getInstance().getCanUseCommands() ? ChatColor.GREEN + "Commands" : ChatColor.RED + "Commands"), ChatColor.GRAY + "Knocked players " + (SettingsConfig.getInstance().getCanUseCommands() ? ChatColor.GREEN + "can " : ChatColor.RED + "can't") + "°" + ChatColor.GRAY + "use any commands." + "°°" + ChatColor.YELLOW + "[Click to toggle]", false));
    }

    public void updateTimeInv() {
        timeInv.setItem(1, main.buildItemStack(Material.REDSTONE_TORCH, ChatColor.RED + "-", ChatColor.GRAY + "Lower the time by°" + ChatColor.DARK_GRAY + "30 " + ChatColor.GRAY + "seconds°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + SettingsConfig.getInstance().getTime() / 60 + ChatColor.GOLD + ":" + (SettingsConfig.getInstance().getTime() % 60 >= 10 ? "" + ChatColor.GOLD + SettingsConfig.getInstance().getTime() % 60 : ChatColor.GOLD + "0" + ChatColor.GOLD + SettingsConfig.getInstance().getTime() % 60) + (SettingsConfig.getInstance().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to lower]", true));
        timeInv.setItem(4, main.buildItemStack(Material.EMERALD_BLOCK, ChatColor.GOLD + "Return to settings", "°" + ChatColor.YELLOW + "[Click to return]", true));
        timeInv.setItem(7, main.buildItemStack(Material.TORCH, ChatColor.GREEN + "+", ChatColor.GRAY + "Higher the time by°" + ChatColor.DARK_GRAY + "30 " + ChatColor.GRAY + "seconds°°" + ChatColor.GRAY + "Current time: " + ChatColor.GOLD + SettingsConfig.getInstance().getTime() / 60 + ChatColor.GOLD + ":" + (SettingsConfig.getInstance().getTime() % 60 >= 10 ? "" + ChatColor.GOLD + SettingsConfig.getInstance().getTime() % 60 : ChatColor.GOLD + "0" + ChatColor.GOLD + SettingsConfig.getInstance().getTime() % 60) + (SettingsConfig.getInstance().getTime() / 60 > 1 ? ChatColor.GOLD + " minutes" : ChatColor.GOLD + " minute") + "°°" + ChatColor.YELLOW + "[Click to higher]", true));
    }

    public void updateDetectionRangeInv() {
        detectionRangeInv.setItem(1, main.buildItemStack(Material.REDSTONE_TORCH, ChatColor.RED + "-", ChatColor.GRAY + "Lower the detection range by°" + ChatColor.DARK_GRAY + "5 " + ChatColor.GRAY + "blocks°°" + ChatColor.GRAY + "Current detection range: " + ChatColor.GOLD + SettingsConfig.getInstance().getDetectionRange() + ChatColor.GRAY + " blocks" + "°°" + ChatColor.YELLOW + "[Click to lower]", true));
        detectionRangeInv.setItem(4, main.buildItemStack(Material.EMERALD_BLOCK, ChatColor.GOLD + "Return to settings", "°" + ChatColor.YELLOW + "[Click to return]", true));
        detectionRangeInv.setItem(7, main.buildItemStack(Material.TORCH, ChatColor.GREEN + "+", ChatColor.GRAY + "Higher the detection range°" + ChatColor.DARK_GRAY + "5 " + ChatColor.GRAY + "blocks°°" + ChatColor.GRAY + "Current detection range: " + ChatColor.GOLD + SettingsConfig.getInstance().getDetectionRange() + ChatColor.GRAY + " blocks" + "°°" + ChatColor.YELLOW + "[Click to higher]", true));
    }
    //END

    public void openSettingsInv(Player player) {
        player.openInventory(settingsInv);
    }

    public void openTimeInv(Player player) {
        player.openInventory(timeInv);
    }

    public void openDetectionRangeInv(Player player) {
        player.openInventory(detectionRangeInv);
    }

    public Inventory getSettingsInv() {
        return settingsInv;
    }

    public Inventory getTimeInv() {
        return timeInv;
    }

    public Inventory getDetectionRangeInv() {
        return detectionRangeInv;
    }

    public Revive getMain() {
        return main;
    }
}
