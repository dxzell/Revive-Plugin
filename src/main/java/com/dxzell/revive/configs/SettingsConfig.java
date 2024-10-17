package com.dxzell.revive.configs;

import com.dxzell.revive.Revive;
import com.dxzell.revive.knockoutmechanic.DownedPlayer;
import org.bukkit.entity.Monster;

public class SettingsConfig extends Config {

    private static SettingsConfig settingsConfig = new SettingsConfig();

    private SettingsConfig() {
        super("settings.yml");
    }

    public int getTime() {
        return config.getInt("time");
    }

    public void setTime(boolean higherLower) {
        if (higherLower) {
            if (getTime() < 600) {
                config.set("time", getTime() + 30);
                save();
            }
        } else {
            if (getTime() > 30) {
                config.set("time", getTime() - 30);
                save();
            }
        }
    }

    public String getType() {
        return config.getString("item");
    }

    public void setType(String type) {
        config.set("item", type);
        save();
    }

    public boolean getAnimation() {
        return config.getBoolean("animation");
    }

    public void setAnimation() {
        if (getAnimation()) {
            config.set("animation", false);
            save();
        } else {
            config.set("animation", true);
            save();
        }
    }

    public int getDetectionRange() {
        return config.getInt("detection-range");
    }

    public void setDetectionRange(boolean higherLower) {
        if (higherLower) {
            config.set("detection-range", getDetectionRange() + 5);
            save();
        } else if (getDetectionRange() > 5) {
            config.set("detection-range", getDetectionRange() - 5);
            save();
        }
    }

    public boolean getMobDamage() {
        return config.getBoolean("mob-damage");
    }

    public void setMobDamage() {
        config.set("mob-damage", (getMobDamage() ? false : true));
        save();

        if (!getMobDamage()) {
            Revive.getMainInstance().getDownedPlayer().getPlayerStands().keySet().forEach(player -> {
                player.getNearbyEntities(100, 100, 100).forEach(entity -> {
                    if (entity instanceof Monster) {
                        Monster monster = (Monster) entity;
                        if (monster.getTarget() != null && monster.getTarget().equals(player))
                            monster.setTarget(null);
                    }
                });
            });
        }
    }

    public boolean getTombstone() {
        return config.getBoolean("tombstone");
    }

    public void toggleTombstone() {
        config.set("tombstone", !getTombstone());
        save();
    }

    public boolean getTotem() {
        return config.getBoolean("totem");
    }

    public void toggleTotem() {
        config.set("totem", !getTotem());
        save();
    }

    public String getPermission() {
        return config.getString("permission");
    }

    public boolean getNoItem() {
        return config.getBoolean("no-item");
    }

    public void toggleNoItem() {
        config.set("no-item", !getNoItem());
        save();
    }

    public boolean getStealingAllowed() {
        return config.getBoolean("stealing-allowed");
    }

    public void toggleStealingAllowed() {
        config.set("stealing-allowed", !getStealingAllowed());
        save();
    }

    public boolean getBlindness() {
        return config.getBoolean("blindness");
    }

    public void toggleBlindness() {
        config.set("blindness", !getBlindness());
        save();
    }

    public boolean getCanUseCommands() {
        return config.getBoolean("can-use-commands");
    }

    public void toggleCanUseCommands() {
        config.set("can-use-commands", !getCanUseCommands());
        save();
    }

    public static SettingsConfig getInstance() {
        return settingsConfig;
    }
}
