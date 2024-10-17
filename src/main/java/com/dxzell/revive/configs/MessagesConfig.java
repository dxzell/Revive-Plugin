package com.dxzell.revive.configs;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesConfig extends Config {

  private static MessagesConfig messagesConfig = new MessagesConfig();

  private MessagesConfig() {
    super("messages.yml");
  }

  public String getKnockedItem(int index) {
    return config.getString("messages.knocked-item.line" + index);
  }

  public String getNoKnockedItem(int index) {
    return config.getString("messages.knocked-no-item.line" + index);
  }

  public String getTitle(boolean titleOrSub) {
    if (titleOrSub) {
      return config.getString("messages.title.title");
    } else {
      return config.getString("messages.title.sub-title");
    }
  }

  public String getActionbar() {
    return config.getString("messages.action-bar");
  }

  public String getWrongUsage() {
    return config.getString("messages.wrong-usage");
  }

  public String getNewReviveItemMessage() {
    return config.getString("messages.select-new-revive-item");
  }

  public String getPressSneak() {
    return config.getString("messages.press-sneak");
  }

  public String getCantPressSneak() {
    return config.getString("messages.cant-press-sneak");
  }

  public String getPlayerDiedMessage(String playerName) {
    List<String> messages =
        config.getStringList("messages.player-died").stream()
            .map(message -> message.replace("[player]", playerName))
            .collect(Collectors.toList());

    return (messages != null && !messages.isEmpty()
        ? messages.get(getRandomNum(messages.size()))
        : "");
  }

  public String getPlayerKnockedMessage(String playerName) {
    List<String> messages =
        config.getStringList("messages.player-knocked").stream()
            .map(message -> message.replace("[player]", playerName))
            .collect(Collectors.toList());

    return (messages != null && !messages.isEmpty()
        ? messages.get(getRandomNum(messages.size()))
        : "");
  }

  private int getRandomNum(int max) {
    return (int) (Math.random() * max);
  }

  public String getKnockedTeleported() {
    return config.getString("messages.knocked-teleport");
  }

  public String getKnockedCommand() {
    return config.getString("messages.knocked-command");
  }

  public static MessagesConfig getInstance() {
    return messagesConfig;
  }
}
