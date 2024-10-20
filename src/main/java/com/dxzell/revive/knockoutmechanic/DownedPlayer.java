package com.dxzell.revive.knockoutmechanic;

import com.dxzell.revive.Revive;
import com.dxzell.revive.configs.MessagesConfig;
import com.dxzell.revive.configs.SettingsConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class DownedPlayer {

  private static HashMap<UUID, List<ArmorStand>> playerStands = new HashMap<>();
  private static HashMap<UUID, Integer> remainingTime = new HashMap<>();
  private static HashMap<UUID, BukkitTask> runnableMap = new HashMap<>();
  private static List<UUID> knockedPlayers = new ArrayList<>();

  private static BossBar noPlayerAround =
      Bukkit.createBossBar(
          ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getPressSneak()),
          BarColor.GREEN,
          BarStyle.SOLID);
  private static BossBar playerAround =
      Bukkit.createBossBar(
          ChatColor.translateAlternateColorCodes(
              '&', MessagesConfig.getInstance().getCantPressSneak()),
          BarColor.RED,
          BarStyle.SOLID);

  public static void downPlayer(Player player) {
    createKnockedStands(player); // Creates armor stands
    setBossBar(player); // Sets the players bossbar
    startKnockedTimer(player); // Starts the knocked timer
    checkBlindness(player); // Checks the blindness option
    sendKnockedTitle(player); // Sends title
    checkAnimation(player); // Checks the animation option
    broadcastKnockedMessage(player); // Broadcasts knocked message to whole server
    checkMobDamage(player); // Checks mob damage option

    knockedPlayers.add(player.getUniqueId()); // Adds knocked player to list
  }

  // Checks the mob damage option and resets the mobs current targets if disabled
  private static void checkMobDamage(Player knockedPlayer) {
    if (!SettingsConfig.getInstance().getMobDamage()) {
      knockedPlayer
          .getNearbyEntities(100, 100, 100)
          .forEach(
              entity -> {
                if (entity instanceof Monster) {
                  Monster monster = (Monster) entity;
                  if (monster.getTarget() != null && monster.getTarget().equals(knockedPlayer))
                    monster.setTarget(null);
                }
              });
    }
  }

  // Broadcasts a message to the whole server that the given player has been knocked
  private static void broadcastKnockedMessage(Player knockedPlayer) {
    Bukkit.getServer()
        .getOnlinePlayers()
        .forEach(
            player -> {
              player.sendMessage(
                  ChatColor.translateAlternateColorCodes(
                      '&',
                      MessagesConfig.getInstance()
                          .getPlayerKnockedMessage(knockedPlayer.getName())));
            });
  }

  // Spawns particles if the option is enabled
  private static void checkAnimation(Player knockedPlayer) {
    if (SettingsConfig.getInstance().getAnimation()) {
      knockedPlayer
          .getWorld()
          .spawnParticle(Particle.CRIT, knockedPlayer.getLocation().add(0, 1, 0), 100);
    }
  }

  // Sends a title message to the knocked player
  private static void sendKnockedTitle(Player player) {
    player.sendTitle(
        ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getTitle(true)),
        ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getTitle(false)));
  }

  // Gives the player the blindness effect if the option is enabled
  private static void checkBlindness(Player knockedPlayer) {
    if (SettingsConfig.getInstance().getBlindness())
      knockedPlayer.addPotionEffect(
          new PotionEffect(
              PotionEffectType.BLINDNESS, SettingsConfig.getInstance().getTime() * 20, 1));
  }

  // Creates the armor stand for the knocked player
  private static void createKnockedStands(Player knockedPlayer) {
    String[] knockedText =
        new String[] {
          ChatColor.AQUA
              + ""
              + SettingsConfig.getInstance().getTime() / 60
              + (SettingsConfig.getInstance().getTime() % 60 > 0
                  ? ChatColor.AQUA + "" + ":30"
                  : ChatColor.AQUA + "" + ":00"),
          ChatColor.translateAlternateColorCodes(
              '&',
              (SettingsConfig.getInstance().getNoItem()
                      ? MessagesConfig.getInstance().getNoKnockedItem(0)
                      : MessagesConfig.getInstance().getKnockedItem(0))
                  .replace("[item]", SettingsConfig.getInstance().getType())),
          ChatColor.translateAlternateColorCodes(
              '&',
              (SettingsConfig.getInstance().getNoItem()
                      ? MessagesConfig.getInstance().getNoKnockedItem(1)
                      : MessagesConfig.getInstance().getKnockedItem(1))
                  .replace("[item]", SettingsConfig.getInstance().getType()))
        };
    Location loc = knockedPlayer.getLocation().clone().add(0, 0.7, 0);
    List<ArmorStand> stands = new ArrayList<>();

    ArmorStand playerStand =
        (ArmorStand)
            knockedPlayer
                .getWorld()
                .spawnEntity(
                    knockedPlayer.getLocation().subtract(0, 1.7, 0), EntityType.ARMOR_STAND);

    playerStand.setInvisible(true);
    playerStand.setGravity(false);
    playerStand.setInvulnerable(true);
    playerStand.addPassenger(knockedPlayer);
    stands.add(playerStand);

    for (String eachText : knockedText) {
      ArmorStand stand =
          (ArmorStand)
              knockedPlayer.getWorld().spawnEntity(loc.subtract(0, 0.3, 0), EntityType.ARMOR_STAND);
      stand.setInvisible(true);
      stand.setGravity(false);
      stand.setInvulnerable(true);
      stand.setCustomNameVisible(true);
      stand.setCustomName(eachText);

      stands.add(stand);
    }
    playerStands.put(knockedPlayer.getUniqueId(), stands);
    remainingTime.put(knockedPlayer.getUniqueId(), SettingsConfig.getInstance().getTime());
  }

  // Starts the timer for the knocked player with the pre-set timer settings
  public static void startKnockedTimer(Player knockedPlayer) {
    BukkitTask knockedTask =
        Bukkit.getScheduler()
            .runTaskTimer(
                Revive.getMainInstance(),
                    () -> {
                    int remaining = remainingTime.get(knockedPlayer.getUniqueId());
                        if (remaining == 1) {
                        if (knockedPlayer
                                .getInventory()
                                .getItemInMainHand()
                                .getType()
                                .equals(Material.TOTEM_OF_UNDYING)
                            || knockedPlayer
                                .getInventory()
                                .getItemInOffHand()
                                .getType()
                                .equals(Material.TOTEM_OF_UNDYING)) {
                            knockedPlayer.setHealth(1.0);
                            knockedPlayer
                              .getInventory()
                              .removeItem(
                                  new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                            knockedPlayer
                              .getWorld()
                              .playSound(
                                      knockedPlayer.getLocation(),
                                  Sound.ITEM_TOTEM_USE,
                                  1.0F,
                                  1.0F);
                        } else {
                          killPlayer(knockedPlayer);
                        }
                      } else {
                        toggleBlindness(knockedPlayer);
                        remainingTime.put(knockedPlayer.getUniqueId(), remaining - 1);
                          knockedPlayer
                            .spigot()
                            .sendMessage(
                                ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(
                                    net.md_5.bungee.api.ChatColor.translateAlternateColorCodes(
                                            '&', MessagesConfig.getInstance().getActionbar())
                                        + " "
                                        + ChatColor.DARK_RED
                                        + ""
                                        + remaining / 60
                                        + ChatColor.DARK_RED
                                        + ":"
                                        + (remaining % 60 >= 10
                                            ? "" + ChatColor.DARK_RED + remaining % 60
                                            : ChatColor.DARK_RED
                                                + "0"
                                                + ChatColor.DARK_RED
                                                + ChatColor.DARK_RED
                                                + remaining % 60)));
                        try {
                          ArmorStand stand = playerStands.get(knockedPlayer.getUniqueId()).get(1);
                          stand.setCustomName(
                              ChatColor.AQUA
                                  + ""
                                  + remaining / 60
                                  + ChatColor.AQUA
                                  + ":"
                                  + (remaining % 60 >= 10
                                      ? "" + ChatColor.AQUA + remaining % 60
                                      : ChatColor.AQUA
                                          + "0"
                                          + ChatColor.AQUA
                                          + ChatColor.AQUA
                                          + remaining % 60));
                        } catch (Exception e) {
                          runnableMap.get(knockedPlayer.getUniqueId()).cancel();
                        }
                      }
                    },
                0L,
                20L);
    runnableMap.put(knockedPlayer.getUniqueId(), knockedTask);
  }

  /////////////////////////////////////////////BIS HIER HABE ICH GEGUCKT, AB HIER WEITER....

  // Places a tombstone after a knocked player dies
  public static Block placeTombstone(Player player) {
    Block block = player.getLocation().add(0, 1, 0).getBlock();
    block.setType(Material.CHEST);
    if (block.getBlockData() instanceof Directional) {
      Directional dir = (Directional) block.getBlockData();
      dir.setFacing(player.getFacing());
      block.setBlockData(dir);
      block.getState().update();
    }
    BlockFace direction = player.getFacing();
    Location currentLoc = player.getLocation().clone().add(0, 1, 0);
    List<Location> locations = new ArrayList<>();
    locations.add(
        currentLoc.add(
            direction == BlockFace.WEST ? 1 : direction == BlockFace.EAST ? -1 : 0,
            0,
            direction == BlockFace.NORTH ? 1 : direction == BlockFace.SOUTH ? -1 : 0));
    locations.add(currentLoc.clone().add(0, 1, 0));
    locations.add(currentLoc.clone().add(0, 2, 0));
    locations.add(currentLoc.clone().add(0, 3, 0));
    locations.add(
        currentLoc
            .clone()
            .add(
                direction == BlockFace.NORTH ? 1 : direction == BlockFace.SOUTH ? -1 : 0,
                2,
                direction == BlockFace.WEST ? -1 : direction == BlockFace.EAST ? 1 : 0));
    locations.add(
        currentLoc
            .clone()
            .add(
                direction == BlockFace.NORTH ? -1 : direction == BlockFace.SOUTH ? 1 : 0,
                2,
                direction == BlockFace.WEST ? 1 : direction == BlockFace.EAST ? -1 : 0));
    locations.forEach(
        loc -> {
          loc.getBlock().setType(Material.STONE);
        });
    return block;
  }

  // Kills player
  public static void killPlayer(Player player) {
    player.setHealth(0);
  }

  // Removes knocked players armor stands
  public static void removeStands(UUID uuid) {
    if (playerStands.containsKey(uuid)) {
      for (ArmorStand stand : playerStands.get(uuid)) {
        stand.remove();
      }
      playerStands.remove(uuid);
    }
  }

  // Removes given players from all collections
  public static void resetMaps(UUID playerUUID) {
    Player player = Bukkit.getServer().getPlayer(playerUUID);
    knockedPlayers.remove(playerUUID);
    remainingTime.remove(playerUUID);
    removeStands(playerUUID);
    removeBossBar(player);
    if (runnableMap.get(playerUUID) != null) {
      runnableMap.get(playerUUID).cancel();
    }
    runnableMap.remove(playerUUID);
    if(player != null) player.removePotionEffect(PotionEffectType.BLINDNESS);
  }

  // Resets all collections / For a reload
  public static void resetAllMaps() {
    List<UUID> cloneList = new ArrayList<>();
    knockedPlayers.forEach(cloneList::add);
    cloneList.forEach(
        p -> {
          resetMaps(p);
          Player player = Bukkit.getServer().getPlayer(p);
          if(player != null) player.teleport(player.getLocation().clone().add(0, 1, 0));
        });
    Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "Reloaded Revive Plugin");
  }

  // Revives player
  public static void revivePlayer(Player player) {
    resetMaps(player.getUniqueId());
    removeBossBar(player);
    if (SettingsConfig.getInstance().getAnimation()) {
      player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 100);
    }
    player.teleport(
        new Location(
            player.getLocation().getWorld(),
            player.getLocation().getX(),
            player.getLocation().getY() + 1,
            player.getLocation().getZ()));
    removeStealingMetaData(player);
  }

  // Sets players bossbar
  public static void setBossBar(Player player) {
    removeBossBar(player);
    if (playerAround(player)) {
      playerAround.addPlayer(player);
    } else {
      noPlayerAround.addPlayer(player);
    }
  }

  // Removes players bossbar
  public static void removeBossBar(Player player) {
    if (noPlayerAround.getPlayers().contains(player)) {
      noPlayerAround.removePlayer(player);
    } else if (playerAround.getPlayers().contains(player)) {
      playerAround.removePlayer(player);
    }
  }

  // Checks whether player is around or not
  public static boolean playerAround(Player player) {
    for (Entity ent :
        player.getNearbyEntities(
            SettingsConfig.getInstance().getDetectionRange(),
            SettingsConfig.getInstance().getDetectionRange(),
            SettingsConfig.getInstance().getDetectionRange())) {
      if (ent instanceof Player) {
        return true;
      }
    }
    return false;
  }

  // Updates the bossbar texts
  public static void updateBossbars() { // updates bossbars to current text's
    noPlayerAround.setTitle(
        ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getPressSneak()));
    playerAround.setTitle(
        ChatColor.translateAlternateColorCodes(
            '&', MessagesConfig.getInstance().getCantPressSneak()));
  }

  // Removes the meta data from the stealing player
  public static void removeStealingMetaData(Player deadPlayer) {
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      if (player.hasMetadata("ReviveStealing")
          && ((UUID) player.getMetadata("ReviveStealing").get(0).value())
              .equals(deadPlayer.getUniqueId())) {
        player.removeMetadata("ReviveStealing", Revive.getMainInstance());
        player.closeInventory();
        break;
      }
    }
  }

  // Toggles the players blindness
  public static void toggleBlindness(Player player) {
    if (player.hasPotionEffect(PotionEffectType.BLINDNESS)
        && !SettingsConfig.getInstance().getBlindness()) {
      player.removePotionEffect(PotionEffectType.BLINDNESS);
    } else if (!player.hasPotionEffect(PotionEffectType.BLINDNESS)
        && SettingsConfig.getInstance().getBlindness()) {
      player.addPotionEffect(
          new PotionEffect(PotionEffectType.BLINDNESS, (remainingTime.get(player) * 20), 1));
    }
  }

  public static HashMap<UUID, List<ArmorStand>> getPlayerStands() {
    return playerStands;
  }

  public static List<UUID> getKnockedList() {
    return knockedPlayers;
  }

  public static void addKnockedPlayer(UUID uuid) {
    knockedPlayers.add(uuid);
  }
}
