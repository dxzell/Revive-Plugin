package com.dxzell.revive.Settings;

import com.dxzell.revive.Revive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class SettingsListener implements Listener {

    private Settings settings;
    private Revive main;
    private List<Player> pickingItemType = new ArrayList<>();

    public SettingsListener(Settings settings, Revive main)
    {
        this.settings = settings;
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(e.getInventory().equals(settings.getSettingsInv()))
        {
            e.setCancelled(true);
            if(e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName())
            {
                return;
            }
            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Time"))
            {
                settings.openTimeInv((Player) e.getWhoClicked());
            }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Item_Type"))
            {
                if(!pickingItemType.contains((Player) e.getWhoClicked()))
                {
                    pickingItemType.add((Player) e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getNewReviveItemMessage()));
                }
            }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Animation") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Animation"))
            {
                main.getConfigClass().setAnimation();
                settings.updateSettingsInv();
            }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Mob damage"))
            {
                main.getConfigClass().setMobDamage();
                settings.updateSettingsInv();
            }else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Detection range"))
            {
                 settings.openDetectionRangeInv((Player) e.getWhoClicked());
            }
        }else if(e.getInventory().equals(settings.getTimeInv())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "-")) {
                main.getConfigClass().setTime(false);
                settings.updateTimeInv();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Return to settings")) {
                settings.openSettingsInv((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "+")) {
                main.getConfigClass().setTime(true);
                settings.updateTimeInv();
                settings.updateSettingsInv();
            }
        }else if(e.getInventory().equals(settings.getDetectionRangeInv()))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "-")) {
                main.getConfigClass().setDetectionRange(false);
                settings.updateDetectionRangeInv();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Return to settings")) {
                settings.openSettingsInv((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "+")) {
                main.getConfigClass().setDetectionRange(true);
                settings.updateDetectionRangeInv();
                settings.updateSettingsInv();
            }


        }else{
            if(e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR))
            {
                if(pickingItemType.contains(e.getWhoClicked())) {
                    e.setCancelled(true);
                    main.getConfigClass().setType(e.getCurrentItem().getType().toString());
                    settings.updateSettingsInv();
                    settings.openSettingsInv((Player) e.getWhoClicked());
                    pickingItemType.remove(e.getWhoClicked());
                    for(List<ArmorStand> stands : main.getStands().values())
                    {
                        ArmorStand stand = stands.get(2);
                        stand.setCustomName(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(0)).replace("[item]", main.getConfigClass().getType()));
                        stand = stands.get(3);
                        stand.setCustomName(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(1)).replace("[item]", main.getConfigClass().getType()));
                    }
                }
            }
        }
    }



}
