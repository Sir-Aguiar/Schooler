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
import java.util.UUID;

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
    List<Claim> playerClaims = loadedData.claims().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

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

  public Claim isPlayerInClaim(Player player) {
    UUID playerUUID = player.getUniqueId();
    List<Claim> playerClaims = loadedData.claims().get(playerUUID);

    if (playerClaims == null) {
      return null;
    }

    int playerX = player.getLocation().getBlockX();
    int playerZ = player.getLocation().getBlockZ();

    for (Claim claim : playerClaims) {
      int[][] claimedBlocks = claim.claimedBlocks();
      int minX = claimedBlocks[0][0];
      int maxX = claimedBlocks[0][1];
      int minZ = claimedBlocks[1][0];
      int maxZ = claimedBlocks[1][1];

      if (playerX >= minX && playerX <= maxX && playerZ >= minZ && playerZ <= maxZ) {
        return claim;
      }
    }

    return null;
  }

}
