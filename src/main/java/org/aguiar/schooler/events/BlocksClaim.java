package org.aguiar.schooler.events;

import org.aguiar.schooler.utils.ClaimsStateManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class BlocksClaim implements Listener {
  private final ClaimsStateManager claimsStateManager;

  public BlocksClaim(ClaimsStateManager claimsStateManager) {
    this.claimsStateManager = claimsStateManager;
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
}
