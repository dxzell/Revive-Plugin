package com.dxzell.revive.CustomMessages;

import com.dxzell.revive.Config;
import com.dxzell.revive.Revive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;

public class InventoryListener implements Listener {

    private Revive main;
    private CustomInventory inv;

    private HashMap<Player, String> cause = new HashMap<Player, String>();


    public InventoryListener(CustomInventory inv, Revive main)
    {
        this.main = main;
        this.inv = inv;



    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(e.getView().getTitle().equals(ChatColor.WHITE + "Customize messages " + ChatColor.BLACK + ">>"))
        {
            e.setCancelled(true);
         if(e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName())
         {
             return;
         }
         Player player = (Player) e.getWhoClicked();
         if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Message above player"))
         {
            cause.put((Player) e.getWhoClicked(), "above0");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type the first line of the message in the chat. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Title and Subtitle"))
         {
             cause.put((Player) e.getWhoClicked(), "title");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type the upper title in the chat. If you wanna use the standard item name (f.e. GOLDEN_APPLE) in the title or subtitle, use [item]. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Actionbar"))
         {
             cause.put((Player) e.getWhoClicked(), "actionbar");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type the actionbar message in the chat. The timer will always be at the end of the message. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Wrong command usage"))
         {
             cause.put((Player) e.getWhoClicked(), "wrong");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type the wrong command usage message in the chat. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "New revive item message"))
         {
             cause.put((Player) e.getWhoClicked(), "new");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type new revive item message in the chat. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Player bled out"))
         {
             cause.put((Player) e.getWhoClicked(), "bledOut");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type a new player bled out message in the chat. To implement the players name, write " + ChatColor.GOLD + "[player]" + ChatColor.GRAY + " where you want the player name to be. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();

         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Player nearby"))
         {
             cause.put((Player) e.getWhoClicked(), "nearby");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type a new player is nearby message. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Player not nearby"))
         {
             cause.put((Player) e.getWhoClicked(), "notNearby");
             player.sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type a new player is not nearby message. You should tell the player that he can die by sneaking. Use ColorCodes with '&' (&4Test).");
             player.closeInventory();
         }
        }
    }
    @EventHandler
    public void onChat(PlayerChatEvent e)
    {
        if(cause.containsKey(e.getPlayer())) {
            e.setCancelled(true);
            switch (cause.get(e.getPlayer())) {
                case "above0":
                    main.getConfigClass().setArmorStand(e.getMessage(), 0);
                    cause.put(e.getPlayer(), "above1");
                    e.getPlayer().sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type the second line in the chat.");
                    inv.update();
                    break;
                case "above1":
                    main.getConfigClass().setArmorStand(e.getMessage(), 1);
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "title":
                    main.getConfigClass().setTitle(e.getMessage(), true);
                    cause.put(e.getPlayer(), "subtitle");
                    e.getPlayer().sendMessage(ChatColor.GOLD + "[Revive] " + ChatColor.GRAY + "Now type the subtitle in the chat.");
                    inv.update();
                    break;
                case "subtitle":
                    main.getConfigClass().setTitle(e.getMessage(), false);
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "actionbar":
                    main.getConfigClass().setActionbar(e.getMessage());
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "wrong":
                    main.getConfigClass().setWrongUsage(e.getMessage());
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "new":
                    main.getConfigClass().setNewReviveItemMessage(e.getMessage());
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "bledOut":
                    main.getConfigClass().setPlayerDiedMessage(e.getMessage());
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "nearby":
                    main.getConfigClass().setCantPressSneak(e.getMessage());
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                case "notNearby":
                    main.getConfigClass().setPressSneak(e.getMessage());
                    cause.remove(e.getPlayer());
                    inv.update();
                    inv.openInv(e.getPlayer());
                    break;
                default:
                    break;
            }
        }
    }








}
