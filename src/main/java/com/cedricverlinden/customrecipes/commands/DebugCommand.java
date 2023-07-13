package com.cedricverlinden.customrecipes.commands;

import com.cedricverlinden.customrecipes.managers.ItemManager;
import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;

import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DebugCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		Log log = new Log();
		if (!(sender instanceof Player player)) {
			log.error("Only players can execute this command.");
			return true;
		}

		Chat chat = new Chat();
		if (!player.hasPermission("customrecipes.debug")) {
			player.sendMessage(chat.color("You don't have permission to execute this command."));
			return true;
		}

		if (args.length == 0) {
			ItemStack hand = player.getInventory().getItemInMainHand();
			if (hand.getType().equals(Material.AIR)) {
				player.sendMessage(chat.warning("Something went wrong, are you sure you are holding an item?"));
				return true;
			}

			String id = Chat.stripComponent(hand.displayName()).replace(" ", "-");
			ItemManager item = ItemManager.getItem(id);
			if (item == null) {
				player.sendMessage(chat.debug("Could not find item \"" + id + "\"."));
				return true;
			}

			player.sendMessage(chat.debug("Item information for the item " + id + "\":"));
			player.sendMessage(chat.debug("Material: " + item.getMaterial().toString()));
			player.sendMessage(chat.debug("Displayname: <reset>" + item.getDisplayName()));
			player.sendMessage(chat.debug("Lore:" + (item.getLore().isEmpty() ? " Non set" : "")));
			for (String lore : item.getLore()) {
				player.sendMessage(chat.debug(" - <reset>" + lore));
			}
			return true;
		}

		if (args.length >= 1) {
			String param = args[0].toLowerCase();
			if ("give".equals(param)) {
				if (args.length == 2 && "all".equalsIgnoreCase(args[1])) {
					HashMap<String, ItemManager> itemManager = ItemManager.getItems();
					for (ItemManager item : itemManager.values()) {
						player.getInventory().addItem(item.getItemStack());
					}
					return true;
				}

				if (args.length == 2) {
					String id = args[1].toLowerCase();
					ItemManager item = ItemManager.getItem(id);
					if (item == null) {
						player.sendMessage(chat.debug("Could not find item \"" + id + "\"."));
						return true;
					}

					player.getInventory().addItem(item.getItemStack());
					return true;
				}
			}

			if ("reload".equals(param)) {
				// TODO: refresh items in player's inventory
				ItemManager.getItems().clear();
				ItemManager.registerItems();
				player.sendMessage(chat.debug(Component.text("Reloaded items (recipes needs restart).")));
				return true;
			}
		}

		return true;
	}
}
