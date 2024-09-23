package org.aguiar.schooler.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aguiar.schooler.records.Claim;
import org.aguiar.schooler.records.JSONClaim;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClaimsManager {
  private final Plugin plugin;
  private final File JSONFile;
  private JSONClaim loadedData;

  public ClaimsManager(Plugin plugin) {
    this.plugin = plugin;
    this.JSONFile = new File(plugin.getDataFolder(), "claims.json");
    this.loadedData = readFile();

  }

  public void updatePlayerClaims(Player player, Claim newClaim) {
    List<Claim> playerClaims = loadedData.claims().get(player.getUniqueId());

    if (playerClaims == null) {
      playerClaims = new ArrayList<>();
      loadedData.claims().put(player.getUniqueId(), playerClaims);
    }

    playerClaims.add(newClaim);
    saveFile();
  }

  private void saveFile() {
    try (Writer writer = new FileWriter(JSONFile)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(loadedData, writer);
    } catch (IOException e) {
      throw new RuntimeException(String.format("[%s] - COULDN'T WRITE TO 'claims.json'", plugin.getName()));
    }
  }

  private JSONClaim readFile() {
    if (!JSONFile.exists()) {
      return new JSONClaim(new HashMap<>());
    }

    try (Reader reader = new FileReader(JSONFile)) {
      Gson gson = new Gson();
      return gson.fromJson(reader, JSONClaim.class);
    } catch (IOException e) {
      throw new RuntimeException(String.format("[%s] - COULDN'T READ THE 'claims.json'", plugin.getName()));
    }
  }

}
