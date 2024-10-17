package com.dxzell.revive;

import com.dxzell.revive.knockoutmechanic.DownedPlayer;
import com.dxzell.revive.knockoutmechanic.ReviveListener;
import com.dxzell.revive.settings.Settings;
import com.dxzell.revive.settings.SettingsCommand;
import com.dxzell.revive.settings.SettingsCommandTab;
import com.dxzell.revive.settings.SettingsListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Revive extends JavaPlugin {
    private DownedPlayer downedPlayer;
    private Settings settings;
    private static Revive mainInstance;

    @Override
    public void onEnable() {
        mainInstance = this;
        settings = new Settings(this);
        downedPlayer = new DownedPlayer(this);
        Bukkit.getPluginManager().registerEvents(new ReviveListener(downedPlayer, this), this);
        getCommand("revive").setExecutor(new SettingsCommand(settings));
        Bukkit.getPluginManager().registerEvents(new SettingsListener(settings, this), this);
        getCommand("revive").setTabCompleter(new SettingsCommandTab(settings));
    }

    @Override
    public void onDisable() {
        for(Player player : getServer().getOnlinePlayers()) {
            if(player.hasMetadata("ReviveStealing")) {
                player.closeInventory();
                player.removeMetadata("ReviveStealing", this);
            }
        }
    }

    public ItemStack buildItemStack(Material mat, String displayName, String lore, boolean enchantment) {
        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);

        //Splitting the lore
        String[] splitLore = lore.split("°");
        List<String> loreList = new ArrayList<>();
        for (String split : splitLore) {
            loreList.add(split);
        }
        meta.setLore(loreList);

        //End
        if (enchantment) meta.addEnchant(Enchantment.KNOCKBACK, 0, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);

        return stack;
    }

    public HashMap<Player, List<ArmorStand>> getStands() {
        return downedPlayer.getPlayerStands();
    }

    public DownedPlayer getDownedPlayer() {
        return downedPlayer;
    }

    public static Revive getMainInstance() {
        return mainInstance;
    }
}
