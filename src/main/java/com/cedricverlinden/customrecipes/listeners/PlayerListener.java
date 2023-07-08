package com.cedricverlinden.customrecipes.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.cedricverlinden.customrecipes.managers.ItemManager;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		HashMap<String, ItemManager> items = ItemManager.getItems();
		for (ItemManager item : items.values()) {
			if (player.getInventory().contains(item.getItemStack())) {
				player.getInventory().remove(item.getItemStack());
			}
			
			player.getInventory().addItem(item.getItemStack());
		}
	}
}
