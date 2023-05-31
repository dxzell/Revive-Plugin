package com.dxzell.revive.CustomMessages;

import com.dxzell.revive.Config;
import com.dxzell.revive.Revive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.function.Function;

public class CustomInventory {

    private Revive main;

    private Inventory inv = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Customize messages " + ChatColor.BLACK + ">>");

    private Function<String, String> cutString;

    public CustomInventory(Revive main)
    {
        this.main = main;

        cutString = text -> { //cuts the string so it fits into the item lore
            ChatColor color = ChatColor.WHITE;
            StringBuilder builder = new StringBuilder();
            int counter = 0;
            for(String string : text.split(" ")) {
                counter += string.length();
                if (!string.contains("&")) {
                    if(counter > 20) {
                        builder.append(color + string + "°");
                        counter = 0;
                    }else{
                        builder.append(color + string + " ");
                    }
                }else{
                    if(counter > 20) {
                        builder.append(string + "°");
                        counter = 0;
                    }else{
                        builder.append(string + " ");
                    }
                }

                for (int i = 0; i < string.length(); i++) {
                    if (string.charAt(i) == '&') {
                        if (i + 1 <= string.length()) {
                            color = ChatColor.getByChar(string.charAt(i + 1));
                        }
                    }
                }
            }
            return builder.toString();
        };

        inv.setItem(0, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ",false));
        inv.setItem(8, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Player not nearby", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "that appears in the°" + ChatColor.GRAY + "bossbar if no player°" + ChatColor.GRAY + "is nearby.°°"+ ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(Config.getPressSneak())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(7, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Player nearby", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "that appears in the°" + ChatColor.GRAY + "bossbar if a player°" + ChatColor.GRAY + "is nearby.°°"+ ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(Config.getCantPressSneak())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(2, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Message above player", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY +"that appears above the player°" + ChatColor.GRAY + "after he gets knocked down.°°" + ChatColor.GRAY +  "Current:°" + (main.getConfigClass().getArmorStand(0).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(0)) : ChatColor.WHITE + main.getConfigClass().getArmorStand(0)) + "°" + (main.getConfigClass().getArmorStand(1).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(1)) : ChatColor.WHITE + main.getConfigClass().getArmorStand(1)) + "°°" + ChatColor.YELLOW + "[Click to edit]",true));
        inv.setItem(3, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Title and Subtitle", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "the player sees after dying.°°" + ChatColor.GRAY + "Current:°" + (main.getConfigClass().getTitle(true).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getTitle(true)) : ChatColor.WHITE + main.getConfigClass().getTitle(true)) + "°" + (main.getConfigClass().getTitle(false).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getTitle(false)) : ChatColor.WHITE + main.getConfigClass().getTitle(false)) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(4, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Actionbar", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "the knocked player sees°" + ChatColor.GRAY + "in the actionbar. The Timer°" + ChatColor.GRAY + "will be at the end.°" + ChatColor.GRAY + "of the message°°" + ChatColor.GRAY + "Current:°" + (main.getConfigClass().getActionbar().contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getActionbar()) : ChatColor.WHITE + main.getConfigClass().getActionbar()) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(5, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Wrong command usage", ChatColor.GRAY + "This message appears in the chat°" + ChatColor.GRAY + "when players use the Revive°" + ChatColor.GRAY + "command incorrectly.°°" + ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(main.getConfigClass().getWrongUsage())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(6, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "New revive item message", ChatColor.GRAY + "This message appears in the chat°" + ChatColor.GRAY + "when the player sets a°" + ChatColor.GRAY + "new revive item in the settings.°°" + ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(main.getConfigClass().getNewReviveItemMessage())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(1, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Player bled out", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "that appears in the°" + ChatColor.GRAY + "chat after a player°" + ChatColor.GRAY + "bled out.°°"+ ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(Config.getPlayerDiedMessage())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));

    }

    public void update()
    {
        inv.setItem(0, main.buildItemStack(Material.GRAY_STAINED_GLASS_PANE, " ", " ",false));
        inv.setItem(8, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Player not nearby", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "that appears in the°" + ChatColor.GRAY + "bossbar if no player°" + ChatColor.GRAY + "is nearby.°°"+ ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(Config.getPressSneak())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(7, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Player nearby", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "that appears in the°" + ChatColor.GRAY + "bossbar if a player°" + ChatColor.GRAY + "is nearby.°°"+ ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(Config.getCantPressSneak())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(2, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Message above player", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY +"that appears above the player°" + ChatColor.GRAY + "after he gets knocked down.°°" + ChatColor.GRAY +  "Current:°" + (main.getConfigClass().getArmorStand(0).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(0)) : ChatColor.WHITE + main.getConfigClass().getArmorStand(0)) + "°" + (main.getConfigClass().getArmorStand(1).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(1)) : ChatColor.WHITE + main.getConfigClass().getArmorStand(1)) + "°°" + ChatColor.YELLOW + "[Click to edit]",true));
        inv.setItem(3, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Title and Subtitle", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "the player sees after dying.°°" + ChatColor.GRAY + "Current:°" + (main.getConfigClass().getTitle(true).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getTitle(true)) : ChatColor.WHITE + main.getConfigClass().getTitle(true)) + "°" + (main.getConfigClass().getTitle(false).contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getTitle(false)) : ChatColor.WHITE + main.getConfigClass().getTitle(false)) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(4, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Actionbar", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "the knocked player sees°" + ChatColor.GRAY + "in the actionbar. The Timer°" + ChatColor.GRAY + "will be at the end.°" + ChatColor.GRAY + "of the message°°" + ChatColor.GRAY + "Current:°" + (main.getConfigClass().getActionbar().contains("&") ? ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getActionbar()) : ChatColor.WHITE + main.getConfigClass().getActionbar()) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(5, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Wrong command usage", ChatColor.GRAY + "This message appears in the chat°" + ChatColor.GRAY + "when players use the Revive°" + ChatColor.GRAY + "command incorrectly.°°" + ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(main.getConfigClass().getWrongUsage())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(6, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "New revive item message", ChatColor.GRAY + "This message appears in the chat°" + ChatColor.GRAY + "when the player sets a°" + ChatColor.GRAY + "new revive item in the settings.°°" + ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(main.getConfigClass().getNewReviveItemMessage())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
        inv.setItem(1, main.buildItemStack(Material.PAPER, ChatColor.GOLD + "Player bled out", ChatColor.GRAY + "This is the message°" + ChatColor.GRAY + "that appears in the°" + ChatColor.GRAY + "chat after a player°" + ChatColor.GRAY + "bled out.°°"+ ChatColor.GRAY + "Current:°" + ChatColor.translateAlternateColorCodes('&', cutString.apply(Config.getPlayerDiedMessage())) + "°°" + ChatColor.YELLOW + "[Click to edit]", true));
    }
    public void openInv(Player player)
    {
        player.openInventory(inv);
    }

    public Inventory getInv()
    {
        return inv;
    }


}
