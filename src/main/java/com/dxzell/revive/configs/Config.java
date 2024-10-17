package com.dxzell.revive.configs;

import com.dxzell.revive.Revive;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class Config {

    protected File file;
    protected YamlConfiguration config;

    public Config(String configName) {
        this.load(configName);
    }

    private void load(String configName) {
        file = new File(Revive.getMainInstance().getDataFolder(), configName);

        if (!file.exists()) {
            Revive.getMainInstance().saveResource(configName, true);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        updateConfig(configName);
    }

    private void updateConfig(String configName) {
        try (Reader defConfigStream = new InputStreamReader(Revive.getMainInstance().getResource(configName))) {
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

                boolean changed = false;
                for (String key : defConfig.getKeys(true)) {
                    if (!config.contains(key)) {
                        config.set(key, defConfig.get(key));
                        changed = true;
                    }
                }

                if (changed) {
                    save();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void save() {
        try {
            config.save(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
