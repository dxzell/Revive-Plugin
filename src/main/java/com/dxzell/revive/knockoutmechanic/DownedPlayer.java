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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DownedPlayer {

    private HashMap<Player, List<ArmorStand>> playerStands = new HashMap<>();
    private HashMap<Player, Integer> remainingTime = new HashMap<>();
    private HashMap<Player, Integer> runnableMap = new HashMap<>();
    private Revive main;
    private BossBar noPlayerAround;
    private BossBar playerAround;
    private List<Player> revivalList = new ArrayList<>();

    public DownedPlayer(Revive main) {
        this.main = main;
        noPlayerAround = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getPressSneak()), BarColor.GREEN, BarStyle.SOLID);
        playerAround = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getCantPressSneak()), BarColor.RED, BarStyle.SOLID);
    }

    public void downPlayer(Player player) {
        String[] text = new String[]{
                ChatColor.AQUA + "" + SettingsConfig.getInstance().getTime() / 60 + (SettingsConfig.getInstance().getTime() % 60 > 0 ? ChatColor.AQUA + "" + ":30" : ChatColor.AQUA + "" + ":00"),
                ChatColor.translateAlternateColorCodes('&', (SettingsConfig.getInstance().getNoItem() ? MessagesConfig.getInstance().getNoKnockedItem(0) : MessagesConfig.getInstance().getKnockedItem(0)).replace("[item]", SettingsConfig.getInstance().getType())),
                ChatColor.translateAlternateColorCodes('&', (SettingsConfig.getInstance().getNoItem() ? MessagesConfig.getInstance().getNoKnockedItem(1) : MessagesConfig.getInstance().getKnockedItem(1)).replace("[item]", SettingsConfig.getInstance().getType()))
        };
        Location loc = player.getLocation().clone().add(0, 0.7, 0);
        List<ArmorStand> stands = new ArrayList<>();

        ArmorStand playerStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().subtract(0, 1.7, 0), EntityType.ARMOR_STAND);

        playerStand.setInvisible(true);
        playerStand.setGravity(false);
        playerStand.setInvulnerable(true);
        playerStand.addPassenger(player);
        stands.add(playerStand);

        setBossBar(player); //Bossbar

        for (String eachText : text) {
            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(loc.subtract(0, 0.3, 0), EntityType.ARMOR_STAND);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName(eachText);

            stands.add(stand);


        }
        playerStands.put(player, stands);
        remainingTime.put(player, SettingsConfig.getInstance().getTime());

        if (SettingsConfig.getInstance().getBlindness()) // Blindness Setting on
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, SettingsConfig.getInstance().getTime() * 20, 1));

        updateTime(player);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getTitle(true)), ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getTitle(false)));

        if (SettingsConfig.getInstance().getAnimation()) {
            player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1, 0), 100);
        }

        //gives other players a message that the given player has been knocked
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getPlayerKnockedMessage(player.getName())));
        });

        // If mobdamage off -> remove target from mobs when getting knocked
        if(!SettingsConfig.getInstance().getMobDamage()) {
            player.getNearbyEntities(100, 100, 100).forEach(entity -> {
                if (entity instanceof Monster) {
                    Monster monster = (Monster) entity;
                    if (monster.getTarget() != null && monster.getTarget().equals(player))
                        monster.setTarget(null);
                }
            });
        }
    }

    public void updateTime(Player player) {
        int runnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                if (remainingTime.get(player) == 1) {
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)
                            || player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
                        player.setHealth(1.0); // Setzt die Gesundheit des Spielers auf 1
                        player.getInventory().removeItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1)); // Entfernt ein Totem
                        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0F, 1.0F); // Spielt den Totem-Soundeffekt ab
                    } else {
                        killPlayer(player);
                    }
                } else {
                    toggleBlindness(player);
                    remainingTime.put(player, remainingTime.get(player) - 1);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getActionbar()) + " " + ChatColor.DARK_RED + "" + remainingTime.get(player) / 60 + ChatColor.DARK_RED + ":" + (remainingTime.get(player) % 60 >= 10 ? "" + ChatColor.DARK_RED + remainingTime.get(player) % 60 : ChatColor.DARK_RED + "0" + ChatColor.DARK_RED + ChatColor.DARK_RED + remainingTime.get(player) % 60)));
                    try {
                        ArmorStand stand = playerStands.get(player).get(1);
                        stand.setCustomName(ChatColor.AQUA + "" + remainingTime.get(player) / 60 + ChatColor.AQUA + ":" + (remainingTime.get(player) % 60 >= 10 ? "" + ChatColor.AQUA + remainingTime.get(player) % 60 : ChatColor.AQUA + "0" + ChatColor.AQUA + ChatColor.AQUA + remainingTime.get(player) % 60));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Bukkit.getScheduler().cancelTask(runnableMap.get(player));
                    }
                }
            }
        }, 0L, 20L);
        runnableMap.put(player, runnable);
    }

    public Block placeTombstone(Player player) {
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
        locations.add(currentLoc.add(direction == BlockFace.WEST ? 1 : direction == BlockFace.EAST ? -1 : 0, 0, direction == BlockFace.NORTH ? 1 : direction == BlockFace.SOUTH ? -1 : 0));
        locations.add(currentLoc.clone().add(0, 1, 0));
        locations.add(currentLoc.clone().add(0, 2, 0));
        locations.add(currentLoc.clone().add(0, 3, 0));
        locations.add(currentLoc.clone().add(direction == BlockFace.NORTH ? 1 : direction == BlockFace.SOUTH ? -1 : 0, 2, direction == BlockFace.WEST ? -1 : direction == BlockFace.EAST ? 1 : 0));
        locations.add(currentLoc.clone().add(direction == BlockFace.NORTH ? -1 : direction == BlockFace.SOUTH ? 1 : 0, 2, direction == BlockFace.WEST ? 1 : direction == BlockFace.EAST ? -1 : 0));
        locations.forEach(loc -> {
            loc.getBlock().setType(Material.STONE);
        });
        return block;
    }

    public void removeStands(Player player) {
        if (playerStands.containsKey(player)) {
            for (ArmorStand stand : playerStands.get(player)) {
                stand.remove();
            }
            playerStands.remove(player);
        }
    }

    public void killPlayer(Player player) {
        player.setHealth(0);
    }

    public void resetMaps(Player player) {
        revivalList.remove(player);
        remainingTime.remove(player);
        removeStands(player);
        removeBossBar(player);
        if (runnableMap.get(player) != null) {
            Bukkit.getScheduler().cancelTask(runnableMap.get(player));
        }
        runnableMap.remove(player);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        removeFromRevival(player);
    }

    public void resetAllMaps() {
        List<Player> cloneList = new ArrayList<>();
        revivalList.forEach(p -> cloneList.add(p));
        cloneList.forEach(p -> {
            resetMaps(p);
            p.teleport(p.getLocation().clone().add(0, 1, 0));
        });
        Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "Reloaded Revive Plugin");
    }

    public void revivePlayer(Player player) {
        resetMaps(player);
        removeBossBar(player);
        if (SettingsConfig.getInstance().getAnimation()) {
            player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 100);
        }
        player.teleport(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ()));
        removeStealingMetaData(player);
    }

    public void setBossBar(Player player) {
        removeBossBar(player);
        if (playerAround(player)) {
            playerAround.addPlayer(player);
        } else {
            noPlayerAround.addPlayer(player);
        }
    }

    public void removeBossBar(Player player) {
        if (noPlayerAround.getPlayers().contains(player)) {
            noPlayerAround.removePlayer(player);
        } else if (playerAround.getPlayers().contains(player)) {
            playerAround.removePlayer(player);
        }
    }

    public boolean playerAround(Player player) {
        for (Entity ent : player.getNearbyEntities(SettingsConfig.getInstance().getDetectionRange(), SettingsConfig.getInstance().getDetectionRange(), SettingsConfig.getInstance().getDetectionRange())) {
            if (ent instanceof Player) {
                return true;
            }
        }
        return false;
    }

    public void updateBossbars() { //updates bossbars to current text's
        noPlayerAround.setTitle(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getPressSneak()));
        playerAround.setTitle(ChatColor.translateAlternateColorCodes('&', MessagesConfig.getInstance().getCantPressSneak()));
    }

    public void removeStealingMetaData(Player deadPlayer) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.hasMetadata("ReviveStealing") && ((UUID) player.getMetadata("ReviveStealing").get(0).value()).equals(deadPlayer.getUniqueId())) {
                player.removeMetadata("ReviveStealing", Revive.getMainInstance());
                player.closeInventory();
                break;
            }
        }
    }

    public void toggleBlindness(Player player) {
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS) && !SettingsConfig.getInstance().getBlindness()) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        } else if (!player.hasPotionEffect(PotionEffectType.BLINDNESS) && SettingsConfig.getInstance().getBlindness()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (remainingTime.get(player) * 20), 1));
        }
    }

    public HashMap<Player, List<ArmorStand>> getPlayerStands() {
        return playerStands;
    }

    public List<Player> getRevivalList() {
        return revivalList;
    }

    public void addRevivelList(Player player) {
        revivalList.add(player);
    }

    public void removeFromRevival(Player player) {
        revivalList.remove(player);
    }
}
