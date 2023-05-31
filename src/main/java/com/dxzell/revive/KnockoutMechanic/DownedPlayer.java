package com.dxzell.revive.KnockoutMechanic;

import com.dxzell.revive.Revive;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownedPlayer {

    private HashMap<Player, List<ArmorStand>> playerStands = new HashMap<>();
    private HashMap<Player, Integer> remainingTime = new HashMap<>();
    private HashMap<Player, Integer> runnableMap = new HashMap<>();
    private Revive main;

    private BossBar noPlayerAround;

    private BossBar playerAround;

    private List<Player> revivalList = new ArrayList<>();

    public DownedPlayer(Revive main)
    {
        this.main = main;
        noPlayerAround = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getPressSneak()), BarColor.GREEN, BarStyle.SOLID);
        playerAround = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getCantPressSneak()), BarColor.RED, BarStyle.SOLID);
    }


    public void downPlayer(Player player)
    {
        String[] text = new String[] {
                ChatColor.AQUA + "" + main.getConfigClass().getTime()/60 + (main.getConfigClass().getTime()%60 > 0 ? ChatColor.AQUA + "" +":30" : ChatColor.AQUA + "" +":00" ),
                ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(0).replace("[item]", main.getConfigClass().getType())),
                ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getArmorStand(1).replace("[item]", main.getConfigClass().getType()))
        };
        Location loc = player.getLocation().add(0, 0.4,0);
        List<ArmorStand> stands = new ArrayList<>();

        ArmorStand playerStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().subtract(0,1.7,0), EntityType.ARMOR_STAND);

        playerStand.setInvisible(true);
        playerStand.setGravity(false);
        playerStand.setInvulnerable(true);
        playerStand.addPassenger(player);
        stands.add(playerStand);

        setBossBar(player); //Bossbar

        for(String eachText : text)
        {
            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(loc.subtract(0,0.3,0), EntityType.ARMOR_STAND);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName(eachText);

            stands.add(stand);



        }
        playerStands.put(player, stands);
        remainingTime.put(player, main.getConfigClass().getTime());
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, main.getConfigClass().getTime()*20, 1));
        updateTime(player);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&',  main.getConfigClass().getTitle(true)),ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getTitle(false)));

        if(main.getConfigClass().getAnimation())
        {
            player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1, 0), 100);
        }
    }

    public void updateTime(Player player)
    {
        int runnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                if(remainingTime.get(player) == 1)
                {
                    killPlayer(player);

                }else{
                    remainingTime.put(player, remainingTime.get(player) - 1);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getActionbar())+ " " + ChatColor.DARK_RED + "" + remainingTime.get(player)/60 + ChatColor.DARK_RED + ":" + (remainingTime.get(player)%60 >= 10 ? "" + ChatColor.DARK_RED + remainingTime.get(player)%60 : ChatColor.DARK_RED + "0" + ChatColor.DARK_RED + ChatColor.DARK_RED + remainingTime.get(player)%60)));
                    try{
                        ArmorStand stand = playerStands.get(player).get(1);
                        stand.setCustomName(ChatColor.AQUA + "" + remainingTime.get(player)/60 + ChatColor.AQUA + ":" + (remainingTime.get(player)%60 >= 10 ? "" + ChatColor.AQUA + remainingTime.get(player)%60 : ChatColor.AQUA + "0" + ChatColor.AQUA + ChatColor.AQUA + remainingTime.get(player)%60 ));
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        Bukkit.getScheduler().cancelTask(runnableMap.get(player));
                    }


                }
            }
        }, 0L, 20L);
        runnableMap.put(player, runnable);


    }



    public void removeStands(Player player)
    {
        if(playerStands.containsKey(player))
        {
            for(ArmorStand stand : playerStands.get(player))
            {
                stand.remove();
            }
            playerStands.remove(player);
        }
    }

    public void killPlayer(Player player)
    {
        player.setHealth(0);
    }

    public void resetMaps(Player player)
    {
        revivalList.remove(player);
        remainingTime.remove(player);
        removeStands(player);
        removeBossBar(player);
        if(runnableMap.get(player) != null) {
            Bukkit.getScheduler().cancelTask(runnableMap.get(player));
        }
        runnableMap.remove(player);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        removeFromRevival(player);

    }

    public void revivePlayer(Player player)
    {
        resetMaps(player);
        removeBossBar(player);
        if(main.getConfigClass().getAnimation()) {
            player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1 ,0), 100);
        }
        player.teleport(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ()));
    }

    public void setBossBar(Player player)
    {
        removeBossBar(player);
      if(playerAround(player)) {
          playerAround.addPlayer(player);
      }else{
          noPlayerAround.addPlayer(player);
      }
    }
    public void removeBossBar(Player player)
    {
        if(noPlayerAround.getPlayers().contains(player))
        {
            noPlayerAround.removePlayer(player);
        }else if(playerAround.getPlayers().contains(player))
        {
            playerAround.removePlayer(player);
        }
    }

    public boolean playerAround(Player player) {

        for (Entity ent : player.getNearbyEntities(main.getConfigClass().getDetectionRange(), main.getConfigClass().getDetectionRange(), main.getConfigClass().getDetectionRange())) {
            if (ent instanceof Player)
            {
                return true;
            }
        }
        return false;
    }

    public void updateBossbars() //updates bossbars to current text's
    {
        noPlayerAround.setTitle(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getPressSneak()));
        playerAround.setTitle(ChatColor.translateAlternateColorCodes('&', main.getConfigClass().getCantPressSneak()));
    }



    public HashMap<Player, List<ArmorStand>> getPlayerStands()
    {
        return playerStands;
    }

    public List<Player> getRevivalList()
    {
        return revivalList;
    }
    public void addRevivelList(Player player)
    {
        revivalList.add(player);
    }
    public void removeFromRevival(Player player)
    {
        revivalList.remove(player);
    }

}
