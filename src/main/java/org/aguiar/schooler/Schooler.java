package org.aguiar.schooler;

import org.aguiar.schooler.events.BlocksClaim;
import org.aguiar.schooler.utils.ClaimsStateManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Schooler extends JavaPlugin {
  private final ClaimsStateManager claimsStateManager = new ClaimsStateManager();
  private final String ENABLED_MESSAGE = String.format("[%s] - PLUGIN ENABLED", getName());
  private final String DISABLED_MESSAGE = String.format("[%s] - PLUGIN DISABLED", getName());

  @Override
  public void onEnable() {
    toggleEvents();
    Bukkit.getConsoleSender().sendMessage(ENABLED_MESSAGE);
  }

  @Override
  public void onDisable() {
    Bukkit.getConsoleSender().sendMessage(DISABLED_MESSAGE);
  }


  private void toggleEvents() {
    Bukkit.getPluginManager().registerEvents(new BlocksClaim(claimsStateManager), this);

    Bukkit.getConsoleSender().sendMessage(String.format("[%s] - ALL EVENTS LOADED", getName()));
  }
}
