package com.dxzell.revive;

public class Config {

    private static Revive main;

    public Config(Revive main)
    {
        this.main = main;
        main.getConfig().options().copyDefaults();
        main.saveDefaultConfig();
    }

    //TIME
    public int getTime()
    {
        return main.getConfig().getInt("time");
    }
    public void setTime(boolean higherLower)
    {
        if(higherLower)
        {
            if(getTime() < 600)
            {
                main.getConfig().set("time", getTime() + 30);
                main.saveConfig();
            }
        }else{
            if(getTime() > 30)
            {
                main.getConfig().set("time", getTime() - 30);
                main.saveConfig();
            }
        }
    }
    //END

    //ITEM TYPE
    public String getType()
    {
        return main.getConfig().getString("item");
    }
    public void setType(String type)
    {
        main.getConfig().set("item", type);
        main.saveConfig();
    }
    //END

    //ANIMATION
    public boolean getAnimation()
    {
        return main.getConfig().getBoolean("animation");
    }
    public void setAnimation()
    {
        if(getAnimation())
        {
            main.getConfig().set("animation", false);
            main.saveConfig();
        }else{
            main.getConfig().set("animation", true);
            main.saveConfig();
        }
    }
    //END

    //CUSTOM TEXT
    //ARMORSTAND
    public String getArmorStand(int index)
    {
        return main.getConfig().getString("messages.armorstand.stand" + index);
    }
    public void setArmorStand(String message, int index)
    {
        main.getConfig().set("messages.armorstand.stand" + index, message);
        main.saveConfig();
    }

    //Title
    public String getTitle(boolean titleOrSub)
    {
        if(titleOrSub)
        {
            return main.getConfig().getString("messages.title.title");
        }else{
            return main.getConfig().getString("messages.title.subtitle");
        }
    }
    public void setTitle(String message, boolean titleOrSub)
    {
        if(titleOrSub)
        {
            main.getConfig().set("messages.title.title", message);
            main.saveConfig();
        }else{
            main.getConfig().set("messages.title.subtitle", message);
            main.saveConfig();
        }
    }

    //ActionBar
    public String getActionbar()
    {
        return main.getConfig().getString("messages.actionbar");
    }
    public void setActionbar(String message)
    {
        main.getConfig().set("messages.actionbar", message);
        main.saveConfig();
    }

    //WRONG USAGE
    public String getWrongUsage()
    {
        return main.getConfig().getString("messages.wrongUsage");
    }
    public void setWrongUsage(String message)
    {
        main.getConfig().set("messages.wrongUsage", message);
        main.saveConfig();
    }

    //NEW REVIVE ITEM
    public String getNewReviveItemMessage()
    {
        return main.getConfig().getString("messages.selectNewReviveItem");
    }
    public void setNewReviveItemMessage(String message)
    {
        main.getConfig().set("messages.selectNewReviveItem", message);
        main.saveConfig();
    }

    //PRESS SNEAK (No player close)
    public static String getPressSneak()
    {
        return main.getConfig().getString("messages.pressSneak");
    }

    public void setPressSneak(String message)
    {
        main.getConfig().set("messages.pressSneak", message);
        main.saveConfig();
        main.getDownedPlayer().updateBossbars();
    }

    //CANT PRESS SNEAK (Player is close)
    public static String getCantPressSneak()
    {
        return main.getConfig().getString("messages.cantPressSneak");
    }

    public void setCantPressSneak(String message)
    {
        main.getConfig().set("messages.cantPressSneak", message);
        main.saveConfig();
        main.getDownedPlayer().updateBossbars();
    }

    //DETECTION RANGE
    public int getDetectionRange()
    {
       return main.getConfig().getInt("detectionRange");
    }
    public void setDetectionRange(boolean higherLower)
    {
        if(higherLower)
        {
           main.getConfig().set("detectionRange", getDetectionRange()+5);
           main.saveConfig();
        }else{
           if(getDetectionRange() > 5)
           {
               main.getConfig().set("detectionRange", getDetectionRange()-5);
               main.saveConfig();
           }
        }
    }

    //GET MOB DAMAGE
    public boolean getMobDamage()
    {
       return main.getConfig().getBoolean("mobDamage");
    }
    public void setMobDamage()
    {
        main.getConfig().set("mobDamage", (getMobDamage() ? false : true));
        main.saveConfig();
    }


    //PLAYER DIED MESSAGE
    public static String getPlayerDiedMessage()
    {
       return  main.getConfig().getString("messages.playerDiedMessage");
    }
    public void setPlayerDiedMessage(String message)
    {
        main.getConfig().set("messages.playerDiedMessage", message);
        main.saveConfig();
    }



}
