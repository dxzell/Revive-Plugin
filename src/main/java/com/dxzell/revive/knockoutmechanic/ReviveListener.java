package com.dxzell.revive.knockoutmechanic;

import com.dxzell.revive.Revive;
import com.dxzell.revive.configs.MessagesConfig;
import com.dxzell.revive.configs.SettingsConfig;
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
                e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getPlayerDiedMessage(player.getName())));
                downedPlayer.resetMaps(player);
                downedPlayer.removeStealingMetaData(player);
                //place tombstone
                if (SettingsConfig.getInstance().getTombstone()) {
                    chestContents.put(downedPlayer.placeTombstone(player), player.getInventory().getContents().clone());
                    e.getDrops().clear();
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (downedPlayer.getPlayerStands().containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
        if (e.getClickedBlock() != null && chestContents.containsKey(e.getClickedBlock())) {
            for (ItemStack stack : chestContents.get(e.getClickedBlock())) {
                if (stack != null) {
                    e.getClickedBlock().getLocation().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), stack);
                }
            }
            chestContents.remove(e.getClickedBlock());
            e.getClickedBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player && downedPlayer.getPlayerStands().containsKey((Player) e.getEntity())) {
            Player player = (Player) e.getEntity();

            if (downedPlayer.getRevivalList().contains(player)) {

                if (!downedPlayer.playerAround(player)) {
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                            || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                        player.setHealth(1.0);
                        player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
                        downedPlayer.revivePlayer(player);
                    } else {
                        downedPlayer.killPlayer(player);
                    }
                } else {

                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(Revive.getMainInstance().getDownedPlayer().getPlayerStands().containsKey(e.getPlayer())) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getKnockedTeleported()));
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(downedPlayer.getPlayerStands().containsKey(e.getPlayer()) && !SettingsConfig.getInstance().getCanUseCommands()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getKnockedCommand()));
        }
    }


    @EventHandler
    public void onDestroyBlocks(BlockBreakEvent e) {
        if (downedPlayer.getPlayerStands().containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageOtherEntity(EntityDamageByEntityEvent e) {
        if (downedPlayer.getPlayerStands().containsKey(e.getDamager())) {
            e.setCancelled(true);
        } else if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (downedPlayer.getRevivalList().contains((Player) e.getEntity())) {

                if (!(e.getDamager() instanceof Player)) {

                    if (!SettingsConfig.getInstance().getMobDamage()) {

                        e.setCancelled(true);

                    } else {
                        if (player.getHealth() - e.getDamage() <= 0) {
                            if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                                    || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                                e.setCancelled(true);
                                player.setHealth(1.0);
                                player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                                player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
                                downedPlayer.revivePlayer(player);
                            }
                        }
                    }
                } else {
                    if (player.getHealth() - e.getDamage() <= 0) {
                        if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                                || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                            e.setCancelled(true);
                            player.setHealth(1.0);
                            player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
                            downedPlayer.revivePlayer(player);
                        }
                    }
                }
            } else {
                if (!downedPlayer.getRevivalList().contains(player)) {
                    if (player.getHealth() - e.getDamage() <= 0) {

                        if ((player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                                || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) && SettingsConfig.getInstance().getTotem()) {
                            e.setCancelled(true);
                            downedPlayer.addRevivelList(player);
                            downedPlayer.downPlayer(player);
                            player.setHealth(1);
                        } else if (!player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                                && !player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
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

    @EventHandler()
    public void onEntityTarget(EntityTargetLivingEntityEvent e) {
        if(e.getTarget() instanceof Player && e.getEntity() instanceof Monster) {
            if(Revive.getMainInstance().getStands().containsKey((Player) e.getTarget()) &&  !SettingsConfig.getInstance().getMobDamage()) {
                e.setTarget(null);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDam(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            /*
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) || e.getCause().equals(EntityDamageEvent.DamageCause.LAVA) ||
                    e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.SUICIDE)) {

             */
            if (Arrays.stream(EntityDamageEvent.DamageCause.values()).toList().contains(e.getCause())) {

                if (downedPlayer.getRevivalList().contains((Player) e.getEntity())) { //knocked + totem
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                            || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                        e.setCancelled(true);
                        player.setHealth(1.0);
                        player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F);
                        downedPlayer.revivePlayer(player);
                    }
                } else { //not knocked but will now be knocked
                    if (!downedPlayer.getRevivalList().contains(player)) {
                        if (player.getHealth() - e.getDamage() <= 0) {
                            if ((player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                                    || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) && SettingsConfig.getInstance().getTotem()) {
                                e.setCancelled(true);
                                downedPlayer.addRevivelList(player);
                                downedPlayer.downPlayer(player);
                                player.setHealth(1);
                            } else if (!player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                                    && !player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
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
    }


    @EventHandler
    public void onRegenerate(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (downedPlayer.getRevivalList().contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            Player player = (Player) e.getRightClicked();
            if (downedPlayer.getPlayerStands().containsKey(e.getRightClicked())) { // If clicked on knocked player
                if (e.getPlayer().isSneaking() && SettingsConfig.getInstance().getStealingAllowed()) { // Player is sneaking + stealing allowed -> Steal from inventory
                    e.getPlayer().openInventory(player.getInventory());
                    e.getPlayer().setMetadata("ReviveStealing", new FixedMetadataValue(Revive.getMainInstance(), player.getUniqueId()));
                } else { // Player is not sneaking -> normal revive
                    if (SettingsConfig.getInstance().getNoItem()) { // If no item needed to revive
                        downedPlayer.revivePlayer(player);
                    } else if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.getMaterial(SettingsConfig.getInstance().getType()))) { // If item needed to revive
                        downedPlayer.revivePlayer(player);
                        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
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
        if (!downedPlayer.getPlayerStands().containsKey(e.getPlayer())) {
            for (Player player : downedPlayer.getPlayerStands().keySet()) {
                downedPlayer.setBossBar(player);
            }
        }
    }
}
