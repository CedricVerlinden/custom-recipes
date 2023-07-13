package com.cedricverlinden.customrecipes.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cedricverlinden.customrecipes.managers.ItemManager;
import com.cedricverlinden.customrecipes.managers.RecipeManager;
import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;

public class InventoryListener implements Listener {

    Log log = new Log();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(event.getView().getOriginalTitle().contains("Create Recipe"))) {
            return;
        }

        boolean hasItem = false;
        for (int i = 1; i < 10; i++) {
            if (inventory.getItem(i) != null) {
                hasItem = true;
                break;
            }
        }

        if (!hasItem || inventory.getItem(0) == null) {
            event.getPlayer().sendMessage(new Chat().error("The recipe requires at least one item to be placed in one of the 9 crafting slots, and the result slot must contain an item. Cancelling recipe creation..."));
            return;
        }

        String recipeName = event.getView().getOriginalTitle().toString().split(": ")[1];

        List<List<String>> itemStacks = new ArrayList<List<String>>();
        for (int i = 0; i < 3; i++) {
            List<String> row = new ArrayList<String>();
            for (int j = 0; j < 3; j++) {
                ItemStack itemStack = inventory.getItem(i * 3 + j + 1);
                if (itemStack == null) {
                    row.add("AIR");
                    continue;
                }

                if (ItemManager.getItem(
                        Chat.stripComponent(itemStack.displayName()).toLowerCase().replace(" ", "-")) != null) {
                            row.add(Chat.stripComponent(itemStack.displayName()).toLowerCase().replace(" ", "-"));
                    continue;
                }

                String item = itemStack.getType().toString();
                row.add(item);
            }

            itemStacks.add(row);
        }

        String result = inventory.getItem(0).getType().toString();
        if (ItemManager.getItem(Chat.stripComponent(inventory.getItem(0).displayName()).toLowerCase().replace(" ", "-")) != null) {
            result = Chat.stripComponent(inventory.getItem(0).displayName()).toLowerCase().replace(" ", "-");
        }

        new RecipeManager(recipeName, itemStacks, result);
    }
}
