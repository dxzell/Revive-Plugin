package com.dxzell.revive.KnockoutMechanic;

import com.dxzell.revive.Revive;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
    private List<Player> revivalList = new ArrayList<>();

    public DownedPlayer(Revive main)
    {
        this.main = main;
    }


    public void downPlayer(Player player)
    {
        String[] text = new String[] {
                ChatColor.AQUA + "" + main.getConfigClass().getTime()/60 + (main.getConfigClass().getTime()%60 > 0 ? ChatColor.AQUA + "" +":30" : ChatColor.AQUA + "" +":00" ),
                ChatColor.GOLD + "Interact with " + ChatColor.GOLD + main.getConfigClass().getType(),
                ChatColor.GOLD + "to revive!"
        };
        Location loc = player.getLocation().add(0, 0.4,0);
        List<ArmorStand> stands = new ArrayList<>();

        ArmorStand playerStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().subtract(0,1.7,0), EntityType.ARMOR_STAND);

        playerStand.setInvisible(true);
        playerStand.setGravity(false);
        playerStand.setInvulnerable(true);
        playerStand.addPassenger(player);
        stands.add(playerStand);

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
        player.sendTitle(ChatColor.RED + "You got knocked out!", ChatColor.DARK_RED + "Get revived by someone or you bleed out.");

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
                 player.setHealth(0);
                 remainingTime.remove(player);
                 Bukkit.getScheduler().cancelTask(runnableMap.get(player));

             }else{
              remainingTime.put(player, remainingTime.get(player) - 1);
              player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.RED + "You will bleed out in " + ChatColor.DARK_RED + "" + remainingTime.get(player)/60 + ChatColor.DARK_RED + ":" + (remainingTime.get(player)%60 >= 10 ? "" + ChatColor.DARK_RED + remainingTime.get(player)%60 : ChatColor.DARK_RED + "0" + ChatColor.DARK_RED + ChatColor.DARK_RED + remainingTime.get(player)%60 )));
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

    public void resetMaps(Player player)
    {
        remainingTime.remove(player);
        removeStands(player);
        if(runnableMap.get(player) != null) {
            Bukkit.getScheduler().cancelTask(runnableMap.get(player));
        }
        runnableMap.remove(player);
        main.getDownedPlayer().removeFromRevival(player);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        removeFromRevival(player);
    }

    public void revivePlayer(Player player)
    {
        resetMaps(player);
        if(main.getConfigClass().getAnimation()) {
            player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1 ,0), 100);
        }
        player.teleport(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ()));
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
