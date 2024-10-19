package com.dxzell.revive.knockoutmechanic;

import com.dxzell.revive.Revive;
import com.dxzell.revive.configs.MessagesConfig;
import com.dxzell.revive.configs.SettingsConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class ReviveListener implements Listener {

  private HashMap<Block, ItemStack[]> chestContents = new HashMap<>();

  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    if (e.getEntity() instanceof Player) {
      Player player = e.getEntity();
      if (DownedPlayer.getKnockedList().contains(player.getUniqueId())) {
        e.setDeathMessage(
            ChatColor.translateAlternateColorCodes(
                '&', MessagesConfig.getInstance().getPlayerDiedMessage(player.getName())));
        DownedPlayer.resetMaps(player.getUniqueId());
        DownedPlayer.removeStealingMetaData(player);
        // place tombstone
        if (SettingsConfig.getInstance().getTombstone()) {
          chestContents.put(
              DownedPlayer.placeTombstone(player), player.getInventory().getContents().clone());
          e.getDrops().clear();
        }
      }
    }
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    if (DownedPlayer.getPlayerStands().containsKey(e.getPlayer().getUniqueId())) {
      e.setCancelled(true);
    }
    if (e.getClickedBlock() != null && chestContents.containsKey(e.getClickedBlock())) {
      for (ItemStack stack : chestContents.get(e.getClickedBlock())) {
        if (stack != null) {
          e.getClickedBlock()
              .getLocation()
              .getWorld()
              .dropItemNaturally(e.getClickedBlock().getLocation(), stack);
        }
      }
      chestContents.remove(e.getClickedBlock());
      e.getClickedBlock().setType(Material.AIR);
    }
  }

  @EventHandler
  public void onDismount(EntityDismountEvent e) {
    if (e.getEntity() instanceof Player
        && DownedPlayer.getPlayerStands().containsKey(((Player) e.getEntity()).getUniqueId())) {
      Player player = (Player) e.getEntity();

      if (DownedPlayer.getKnockedList().contains(player.getUniqueId())) {

        if (!DownedPlayer.playerAround(player)) {
          if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
              || player
                  .getInventory()
                  .getItemInOffHand()
                  .getType()
                  .equals(Material.TOTEM_OF_UNDYING)) {
            player.setHealth(1.0);
            player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
            DownedPlayer.revivePlayer(player);
          } else {
            DownedPlayer.killPlayer(player);
          }
        }
      }
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent e) {
    if (DownedPlayer.getPlayerStands().containsKey(e.getPlayer().getUniqueId())) {
      e.getPlayer()
          .sendMessage(
              ChatColor.translateAlternateColorCodes(
                  '&', MessagesConfig.getInstance().getKnockedTeleported()));
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent e) {
    if (DownedPlayer.getPlayerStands().containsKey(e.getPlayer().getUniqueId())
        && !SettingsConfig.getInstance().getCanUseCommands()) {
      e.setCancelled(true);
      e.getPlayer()
          .sendMessage(
              ChatColor.translateAlternateColorCodes(
                  '&', MessagesConfig.getInstance().getKnockedCommand()));
    }
  }

  @EventHandler
  public void onDestroyBlocks(BlockBreakEvent e) {
    if (DownedPlayer.getPlayerStands().containsKey(e.getPlayer().getUniqueId())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onDamageOtherEntity(EntityDamageByEntityEvent e) {
    if (DownedPlayer.getPlayerStands().containsKey(e.getDamager().getUniqueId())) {
      e.setCancelled(true);
    } else if (e.getEntity() instanceof Player) {
      Player player = (Player) e.getEntity();

      if (DownedPlayer.getKnockedList().contains(player.getUniqueId())) {

        if (!(e.getDamager() instanceof Player)) {

          if (!SettingsConfig.getInstance().getMobDamage()) {

            e.setCancelled(true);

          } else {
            if (player.getHealth() - e.getDamage() <= 0) {
              if (player
                      .getInventory()
                      .getItemInMainHand()
                      .getType()
                      .equals(Material.TOTEM_OF_UNDYING)
                  || player
                      .getInventory()
                      .getItemInOffHand()
                      .getType()
                      .equals(Material.TOTEM_OF_UNDYING)) {
                e.setCancelled(true);
                player.setHealth(1.0);
                player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
                DownedPlayer.revivePlayer(player);
              }
            }
          }
        } else {
          if (player.getHealth() - e.getDamage() <= 0) {
            if (player
                    .getInventory()
                    .getItemInMainHand()
                    .getType()
                    .equals(Material.TOTEM_OF_UNDYING)
                || player
                    .getInventory()
                    .getItemInOffHand()
                    .getType()
                    .equals(Material.TOTEM_OF_UNDYING)) {
              e.setCancelled(true);
              player.setHealth(1.0);
              player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
              player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
                DownedPlayer.revivePlayer(player);
            }
          }
        }
      } else {
        if (!DownedPlayer.getKnockedList().contains(player.getUniqueId())) {
          if (player.getHealth() - e.getDamage() <= 0) {

            if ((player
                        .getInventory()
                        .getItemInMainHand()
                        .getType()
                        .equals(Material.TOTEM_OF_UNDYING)
                    || player
                        .getInventory()
                        .getItemInOffHand()
                        .getType()
                        .equals(Material.TOTEM_OF_UNDYING))
                && SettingsConfig.getInstance().getTotem()) {
              e.setCancelled(true);
              DownedPlayer.addKnockedPlayer(player.getUniqueId());
                DownedPlayer.downPlayer(player);
              player.setHealth(1);
            } else if (!player
                    .getInventory()
                    .getItemInMainHand()
                    .getType()
                    .equals(Material.TOTEM_OF_UNDYING)
                && !player
                    .getInventory()
                    .getItemInOffHand()
                    .getType()
                    .equals(Material.TOTEM_OF_UNDYING)) {
              e.setCancelled(true);
                DownedPlayer.addKnockedPlayer(player.getUniqueId());
                DownedPlayer.downPlayer(player);
              player.setHealth(1);
            }
          }
        }
      }
    }
  }

  @EventHandler()
  public void onEntityTarget(EntityTargetLivingEntityEvent e) {
    if (e.getTarget() instanceof Player && e.getEntity() instanceof Monster) {
      if (DownedPlayer.getPlayerStands().containsKey(((Player) e.getTarget()).getUniqueId())
          && !SettingsConfig.getInstance().getMobDamage()) {
        e.setTarget(null);
        e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onDam(EntityDamageEvent e) {
    if (e.getEntity() instanceof Player) {
      Player player = (Player) e.getEntity();
      if (Arrays.stream(EntityDamageEvent.DamageCause.values()).toList().contains(e.getCause())) {

        if (DownedPlayer.getKnockedList().contains(player.getUniqueId())) { // knocked + totem
          if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
              || player
                  .getInventory()
                  .getItemInOffHand()
                  .getType()
                  .equals(Material.TOTEM_OF_UNDYING)) {
            e.setCancelled(true);
            player.setHealth(1.0);
            player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
            DownedPlayer.revivePlayer(player);
          }
        } else { // not knocked but will now be knocked
          if (!DownedPlayer.getKnockedList().contains(player.getUniqueId())) {
            if (player.getHealth() - e.getDamage() <= 0) {
              if ((player
                          .getInventory()
                          .getItemInMainHand()
                          .getType()
                          .equals(Material.TOTEM_OF_UNDYING)
                      || player
                          .getInventory()
                          .getItemInOffHand()
                          .getType()
                          .equals(Material.TOTEM_OF_UNDYING))
                  && SettingsConfig.getInstance().getTotem()) {
                e.setCancelled(true);
                DownedPlayer.addKnockedPlayer(player.getUniqueId());
                DownedPlayer.downPlayer(player);
                player.setHealth(1);
              } else if (!player
                      .getInventory()
                      .getItemInMainHand()
                      .getType()
                      .equals(Material.TOTEM_OF_UNDYING)
                  && !player
                      .getInventory()
                      .getItemInOffHand()
                      .getType()
                      .equals(Material.TOTEM_OF_UNDYING)) {
                e.setCancelled(true);
                DownedPlayer.addKnockedPlayer(player.getUniqueId());
                DownedPlayer.downPlayer(player);
                player.setHealth(1);
              }
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onRegenerate(EntityRegainHealthEvent e) {
    if (e.getEntity() instanceof Player) {
      Player player = (Player) e.getEntity();
      if (DownedPlayer.getKnockedList().contains(player.getUniqueId())) {
        e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onInteractWithEntity(PlayerInteractAtEntityEvent e) {
    if (e.getRightClicked() instanceof Player) {
      Player player = (Player) e.getRightClicked();
      if (DownedPlayer.getPlayerStands()
          .containsKey(e.getRightClicked().getUniqueId())) { // If clicked on knocked player
        if (e.getPlayer().isSneaking()
            && SettingsConfig.getInstance()
                .getStealingAllowed()) { // Player is sneaking + stealing allowed -> Steal from
                                         // inventory
          e.getPlayer().openInventory(player.getInventory());
          e.getPlayer()
              .setMetadata(
                  "ReviveStealing",
                  new FixedMetadataValue(Revive.getMainInstance(), player.getUniqueId()));
        } else { // Player is not sneaking -> normal revive
          if (SettingsConfig.getInstance().getNoItem()) { // If no item needed to revive
            DownedPlayer.revivePlayer(player);
          } else if (e.getPlayer()
              .getInventory()
              .getItemInMainHand()
              .getType()
              .equals(
                  Material.getMaterial(
                      SettingsConfig.getInstance().getType()))) { // If item needed to revive
            DownedPlayer.revivePlayer(player);
            e.getPlayer()
                .getInventory()
                .getItemInMainHand()
                .setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
          }
        }
      }
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    if (DownedPlayer.getKnockedList().contains(e.getPlayer().getUniqueId())) {
      e.getPlayer().setHealth(0);
      DownedPlayer.resetMaps(e.getPlayer().getUniqueId());
    }

    if (e.getPlayer().hasMetadata("ReviveStealing"))
      e.getPlayer().removeMetadata("ReviveStealing", Revive.getMainInstance());
  }

  @EventHandler
  public void onClose(InventoryCloseEvent e) {
    if (e.getPlayer().hasMetadata("ReviveStealing")) {
      e.getPlayer().removeMetadata("ReviveStealing", Revive.getMainInstance());
    }
  }

  @EventHandler
  public void onMove(PlayerMoveEvent e) {
    if (!DownedPlayer.getPlayerStands().containsKey(e.getPlayer().getUniqueId())) {
      for (UUID uuid : DownedPlayer.getPlayerStands().keySet()) {
        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player != null) DownedPlayer.setBossBar(player);
      }
    }
  }
}
