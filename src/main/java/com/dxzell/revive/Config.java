package com.dxzell.revive;

public class Config {

    private Revive main;

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


}
