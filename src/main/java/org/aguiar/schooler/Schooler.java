package org.aguiar.schooler;

import org.aguiar.schooler.events.BlocksClaim;
import org.aguiar.schooler.utils.ClaimsManager;
import org.aguiar.schooler.utils.ClaimsStateManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Schooler extends JavaPlugin {
  private final ClaimsManager claimsManager = new ClaimsManager(this);
  private final ClaimsStateManager claimsStateManager = new ClaimsStateManager(claimsManager);
  private final String ENABLED_MESSAGE = String.format("[%s] - PLUGIN ENABLED", getName());
  private final String DISABLED_MESSAGE = String.format("[%s] - PLUGIN DISABLED", getName());

  @Override
  public void onEnable() {
    toggleEvents();
    initializeJSONConfig();
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

  private void initializeJSONConfig() throws RuntimeException {
    try {
      getDataFolder().mkdir();

      File JSONFile = new File(getDataFolder(), "claims.json");

      if (!JSONFile.exists()) {
        JSONFile.createNewFile();
      }
    } catch (IOException e) {
      throw new RuntimeException(String.format("[%s] - COULD NOT INTIALIZE CLAIMS.JSON", getName()));
    }
  }
}
