package com.dxzell.revive.settings;

import com.dxzell.revive.Revive;
import com.dxzell.revive.configs.MessagesConfig;
import com.dxzell.revive.configs.SettingsConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class SettingsListener implements Listener {

    private Settings settings;
    private Revive main;
    private List<Player> pickingItemType = new ArrayList<>();

    public SettingsListener(Settings settings, Revive main) {
        this.settings = settings;
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().equals(settings.getSettingsInv())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Time")) {
                settings.openTimeInv((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Item_Type")) {
                if (!pickingItemType.contains((Player) e.getWhoClicked())) {
                    pickingItemType.add((Player) e.getWhoClicked());
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getNewReviveItemMessage()));
                }
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Animation") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Animation")) {
                SettingsConfig.getInstance().setAnimation();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Mob damage") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Mob damage")) {
                SettingsConfig.getInstance().setMobDamage();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Detection range")) {
                settings.openDetectionRangeInv((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Tombstone") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Tombstone")) {
                SettingsConfig.getInstance().toggleTombstone();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Totem") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Totem")) {
                SettingsConfig.getInstance().toggleTotem();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "No Item") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "No Item")) {
                SettingsConfig.getInstance().toggleNoItem();
                updateKnockedTexts();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Stealing") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Stealing")) {
                SettingsConfig.getInstance().toggleStealingAllowed();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Blindness") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Blindness")) {
                SettingsConfig.getInstance().toggleBlindness();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Commands") || e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Commands")) {
                SettingsConfig.getInstance().toggleCanUseCommands();
                settings.updateSettingsInv();
            }
        } else if (e.getInventory().equals(settings.getTimeInv())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "-")) {
                SettingsConfig.getInstance().setTime(false);
                settings.updateTimeInv();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Return to settings")) {
                settings.openSettingsInv((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "+")) {
                SettingsConfig.getInstance().setTime(true);
                settings.updateTimeInv();
                settings.updateSettingsInv();
            }
        } else if (e.getInventory().equals(settings.getDetectionRangeInv())) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "-")) {
                SettingsConfig.getInstance().setDetectionRange(false);
                settings.updateDetectionRangeInv();
                settings.updateSettingsInv();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Return to settings")) {
                settings.openSettingsInv((Player) e.getWhoClicked());
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "+")) {
                SettingsConfig.getInstance().setDetectionRange(true);
                settings.updateDetectionRangeInv();
                settings.updateSettingsInv();
            }

            // When changing item revive type
        } else {
            if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) {
                if (pickingItemType.contains(e.getWhoClicked())) {
                    e.setCancelled(true);
                    SettingsConfig.getInstance().setType(e.getCurrentItem().getType().toString());
                    settings.updateSettingsInv();
                    settings.openSettingsInv((Player) e.getWhoClicked());
                    pickingItemType.remove(e.getWhoClicked());
                    updateKnockedTexts();
                }
            }
        }
    }

    // Updates the text of every knocked players armorstand
    private void updateKnockedTexts() {
        for (List<ArmorStand> stands : main.getStands().values()) {
            ArmorStand stand = stands.get(2);
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', (SettingsConfig.getInstance().getNoItem() ? MessagesConfig.getInstance().getNoKnockedItem(0) : MessagesConfig.getInstance().getKnockedItem(0))).replace("[item]", SettingsConfig.getInstance().getType()));
            stand = stands.get(3);
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', (SettingsConfig.getInstance().getNoItem() ? MessagesConfig.getInstance().getNoKnockedItem(1) : MessagesConfig.getInstance().getKnockedItem(1))).replace("[item]", SettingsConfig.getInstance().getType()));
        }
    }
}
