package com.dxzell.revive.KnockoutMechanic;

import com.dxzell.revive.Config;
import com.dxzell.revive.KnockoutMechanic.DownedPlayer;
import com.dxzell.revive.Revive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReviveListener implements Listener {

    private DownedPlayer downedPlayer;
    private Revive main;





    public ReviveListener(DownedPlayer downedPlayer, Revive main) {
        this.main = main;
        this.downedPlayer = downedPlayer;
    }



    @EventHandler
    public void onTot(PlayerDeathEvent e)
    {


        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (downedPlayer.getRevivalList().contains(player)) {

               e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', Config.getPlayerDiedMessage().replace("[player]", player.getName())));
                downedPlayer.resetMaps(player);





            }
        }
    }



    @EventHandler
    public void onDismount(EntityDismountEvent e)
    {
        if(e.getEntity() instanceof Player && downedPlayer.getPlayerStands().containsKey((Player) e.getEntity()))
        {
            Player player = (Player) e.getEntity();

            if (downedPlayer.getRevivalList().contains(player)) {

                    if (!downedPlayer.playerAround(player)) {
                        downedPlayer.killPlayer(player);
                    } else {

                    }
                }


            e.setCancelled(true);
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
        }else
        if(e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();

            if(downedPlayer.getRevivalList().contains((Player) e.getEntity()))
            {

                if(!(e.getDamager() instanceof Player))
                {

                    if(!main.getConfigClass().getMobDamage())
                    {

                        e.setCancelled(true);

                    }else{

                    }
                }
            }else{
                if (!downedPlayer.getRevivalList().contains(player)) {
                    if (player.getHealth() - e.getDamage() <= 0) {
                        e.setCancelled(true);
                        downedPlayer.addRevivelList(player);
                        downedPlayer.downPlayer(player);
                        player.setHealth(1);
                    }
                }
            }
        }
    }


    /*
    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {



        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (!downedPlayer.getRevivalList().contains(player)) {
                if (player.getHealth() - e.getDamage() <= 0) {
                    e.setCancelled(true);
                    downedPlayer.addRevivelList(player);
                    downedPlayer.downPlayer(player);
                    player.setHealth(1);
                }
            }
        }

    }

     */

    @EventHandler
    public void onDam(EntityDamageEvent e)
    {
      if(e.getEntity() instanceof Player)
      {

          Player player = (Player) e.getEntity();
          if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA) ||
                  e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
          || e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
          || e.getCause().equals(EntityDamageEvent.DamageCause.SUICIDE))
          {
              if(downedPlayer.getRevivalList().contains((Player) e.getEntity()))
              {





              }else{
                  if (!downedPlayer.getRevivalList().contains(player)) {
                      if (player.getHealth() - e.getDamage() <= 0) {
                          e.setCancelled(true);
                          downedPlayer.addRevivelList(player);
                          downedPlayer.downPlayer(player);
                          player.setHealth(1);
                      }
                  }
              }
          }
      }
    }



    @EventHandler
    public void onRegenerate(EntityRegainHealthEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();

            if(downedPlayer.getRevivalList().contains(player))
            {
                e.setCancelled(true);
            }
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


    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if(!downedPlayer.getPlayerStands().containsKey(e.getPlayer()))
        {
           for(Player player : downedPlayer.getPlayerStands().keySet())
           {

               downedPlayer.setBossBar(player);


           }
        }
    }



}
