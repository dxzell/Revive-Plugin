package com.dxzell.revive.KnockoutMechanic;

import com.dxzell.revive.KnockoutMechanic.DownedPlayer;
import com.dxzell.revive.Revive;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.List;

public class ReviveListener implements Listener {

    private DownedPlayer downedPlayer;
    private Revive main;

    public ReviveListener(DownedPlayer downedPlayer, Revive main) {
        this.main = main;
        this.downedPlayer = downedPlayer;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        Player player = (Player) e.getEntity();
        if(downedPlayer.getRevivalList().contains(player))
        {
            downedPlayer.resetMaps(player);
        }

    }

    @EventHandler
    public void onDismount(EntityDismountEvent e)
    {
        if(e.getEntity() instanceof Player && downedPlayer.getPlayerStands().containsKey((Player) e.getEntity()))
        {
           e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();

            if(downedPlayer.getPlayerStands().containsKey(player))
            {
                player.setHealth(0);
            }else {
                if (!downedPlayer.getRevivalList().contains(player)) {
                    if (player.getHealth() - e.getDamage() <= 0) {
                        e.setCancelled(true);
                        downedPlayer.addRevivelList(player);
                        downedPlayer.downPlayer(player);
                        player.setHealth(20);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        if(downedPlayer.getPlayerStands().containsKey(e.getPlayer()))
        {
            e.getPlayer().setHealth(0);
            downedPlayer.resetMaps(e.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if(downedPlayer.getPlayerStands().containsKey(e.getPlayer()))
        {
            e.setCancelled(true);
        }

    }
    @EventHandler
    public void onDestroyBlocks(BlockBreakEvent e)
    {
        if(downedPlayer.getPlayerStands().containsKey(e.getPlayer()))
        {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamageOtherEntity(EntityDamageByEntityEvent e)
    {
        if(downedPlayer.getPlayerStands().containsKey(e.getDamager()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractAtEntityEvent e)
    {
        if(e.getRightClicked() instanceof Player) {

         Player player = (Player) e.getRightClicked();
         if (downedPlayer.getPlayerStands().containsKey(e.getRightClicked()) && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(main.getConfigClass().getType())))
         {
            downedPlayer.revivePlayer(player);
            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);


         }

        }
    }



}
