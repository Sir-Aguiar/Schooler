package org.aguiar.schooler.utils;

import org.aguiar.schooler.records.Claim;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimsStateManager {
  private final ClaimsManager claimsManager;
  public Map<UUID, List<Block>> clickedSequence = new HashMap<>();

  public ClaimsStateManager(ClaimsManager claimsManager) {
    this.claimsManager = claimsManager;
  }

  public void stopClickSequence(Player player) {
    clickedSequence.remove(player.getUniqueId());
  }

  public void startClickSequence(Player player, Block clickedBlock) {
    List<Block> blockList = new ArrayList<>();
    blockList.add(clickedBlock);
    clickedSequence.put(player.getUniqueId(), blockList);
  }

  public void addClickSequence(Player player, Block clickedBlock) {
    List<Block> blockList = clickedSequence.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

    if (blockList.size() < 2 && !blockList.contains(clickedBlock)) {
      blockList.add(clickedBlock);

      if (blockList.size() == 2) {
        claimBlocks(player);
      }
    }
  }

  public boolean isPlayerInSequence(Player player) {
    return clickedSequence.get(player.getUniqueId()) != null;
  }

  public void claimBlocks(Player player) {
    List<Block> blockList = clickedSequence.get(player.getUniqueId());

    int minX = Math.min(blockList.get(0).getX(), blockList.get(1).getX());
    int maxX = Math.max(blockList.get(0).getX(), blockList.get(1).getX());
    int minZ = Math.min(blockList.get(0).getZ(), blockList.get(1).getZ());
    int maxZ = Math.max(blockList.get(0).getZ(), blockList.get(1).getZ());

    int[][] claimedBlocks = {{minX, maxX}, {minZ, maxZ}};
    Claim newClaim = new Claim(UUID.randomUUID().toString(), claimedBlocks);

    this.claimsManager.updatePlayerClaims(player, newClaim);

    stopClickSequence(player);
  }
}
