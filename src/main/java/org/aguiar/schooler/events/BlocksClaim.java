package org.aguiar.schooler.events;

import org.aguiar.schooler.records.Claim;
import org.aguiar.schooler.utils.ClaimsManager;
import org.aguiar.schooler.utils.ClaimsStateManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlocksClaim implements Listener {
  private final ClaimsStateManager claimsStateManager;
  private final ClaimsManager claimsManager;

  public BlocksClaim(ClaimsStateManager claimsStateManager, ClaimsManager claimsManager) {
    this.claimsStateManager = claimsStateManager;
    this.claimsManager = claimsManager;
  }

  @EventHandler
  public void onRightClick(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    Player player = event.getPlayer();
    ItemStack handItem = player.getInventory().getItemInMainHand();

    if (handItem.getType() != Material.DIAMOND_SHOVEL) {
      claimsStateManager.stopClickSequence(player);
      return;
    }

    Block clickedBlock = event.getClickedBlock();

    if (claimsStateManager.isPlayerInSequence(player)) {
      claimsStateManager.addClickSequence(player, clickedBlock);
    } else {
      claimsStateManager.startClickSequence(player, clickedBlock);
    }
  }


  @EventHandler
  public void onItemChange(PlayerItemHeldEvent event) {
    claimsStateManager.stopClickSequence(event.getPlayer());
  }

  @EventHandler
  public void onWalk(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    Claim playerClaim = claimsManager.isPlayerInClaim(player);

    if (playerClaim == null) return;

  }
}
