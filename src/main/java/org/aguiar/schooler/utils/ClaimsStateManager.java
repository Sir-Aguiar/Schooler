package org.aguiar.schooler.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimsStateManager {
  public Map<UUID, List<Block>> clickedSequence = new HashMap<>();
  public Map<UUID, int[][]> claimedBlocks = new HashMap<>();

  public ClaimsStateManager() {

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
    List<Block> blockList = clickedSequence.get(player.getUniqueId());

    if (blockList == null) {
      blockList = new ArrayList<>();
      clickedSequence.put(player.getUniqueId(), blockList);
    }

    if (blockList.size() < 2) {
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
    // This alg is AI auxiliated so need to be enhanced

    List<Block> blockList = clickedSequence.get(player.getUniqueId());

    int minX = Math.min(blockList.get(0).getX(), blockList.get(1).getX());
    int maxX = Math.max(blockList.get(0).getX(), blockList.get(1).getX());
    int minZ = Math.min(blockList.get(0).getZ(), blockList.get(1).getZ());
    int maxZ = Math.max(blockList.get(0).getZ(), blockList.get(1).getZ());

    List<int[]> claimedBlockList = new ArrayList<>();

    for (int x = minX; x <= maxX; x++) {
      for (int z = minZ; z <= maxZ; z++) {
        claimedBlockList.add(new int[]{x, z});
      }
    }

    claimedBlocks.put(player.getUniqueId(), claimedBlockList.toArray(new int[0][0]));


    stopClickSequence(player);
  }
}
