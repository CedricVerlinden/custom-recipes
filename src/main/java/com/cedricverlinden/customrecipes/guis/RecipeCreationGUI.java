package com.cedricverlinden.customrecipes.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.cedricverlinden.customrecipes.utils.Chat;

import net.kyori.adventure.text.Component;

public class RecipeCreationGUI {

    private final Inventory inventory;
    private final Component title;

    private final Chat chat;

    public RecipeCreationGUI(String recipeName) {
        chat = new Chat();
        title = chat.color("<dark_green>Create Recipe: " + recipeName);
        inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, title);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Component getTitle() {
        return title;
    }
}