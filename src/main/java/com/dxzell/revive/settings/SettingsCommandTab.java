package com.dxzell.revive.settings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsCommandTab implements TabCompleter {

    private Settings settings;

    public SettingsCommandTab(Settings settings) {
        this.settings = settings;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> liste = new ArrayList<>();

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(new String[]{"settings", "reload"}), new ArrayList<>());
        }
        return liste;
    }
}
