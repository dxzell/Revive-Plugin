package com.dxzell.revive;

import com.dxzell.revive.CustomMessages.CustomInventory;
import com.dxzell.revive.CustomMessages.InventoryListener;
import com.dxzell.revive.KnockoutMechanic.DownedPlayer;
import com.dxzell.revive.KnockoutMechanic.ReviveListener;
import com.dxzell.revive.Settings.Settings;
import com.dxzell.revive.Settings.SettingsCommand;
import com.dxzell.revive.Settings.SettingsCommandTab;
import com.dxzell.revive.Settings.SettingsListener;
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

    private Config config = new Config(this);
    private DownedPlayer downedPlayer = new DownedPlayer(this);

    private Settings settings = new Settings(this);

    private CustomInventory customInv = new CustomInventory(this);



    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new ReviveListener(downedPlayer, this), this);
        getCommand("revive").setExecutor(new SettingsCommand(settings));
        Bukkit.getPluginManager().registerEvents(new SettingsListener(settings, this), this);
        getCommand("revive").setTabCompleter(new SettingsCommandTab(settings));
        Bukkit.getPluginManager().registerEvents(new InventoryListener(customInv, this), this);
    }

    public ItemStack buildItemStack(Material mat, String displayName, String lore, boolean enchantment)
    {
        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);

        //Splitting the lore

        String[] splitLore = lore.split("°");
        List<String> loreList = new ArrayList<>();
        for(String split : splitLore)
        {
            loreList.add(split);
        }
        meta.setLore(loreList);

        //End

        if(enchantment) meta.addEnchant(Enchantment.KNOCKBACK, 0, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);

        return stack;

    }

    public Config getConfigClass()
    {
        return config;
    }

    public HashMap<Player, List<ArmorStand>> getStands()
    {
        return downedPlayer.getPlayerStands();
    }

    public DownedPlayer getDownedPlayer()
    {
        return downedPlayer;
    }

    public CustomInventory getInv()
    {
        return customInv;
    }


}
