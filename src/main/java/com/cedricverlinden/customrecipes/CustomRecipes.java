package com.cedricverlinden.customrecipes;

import com.cedricverlinden.customrecipes.commands.DebugCommand;
import com.cedricverlinden.customrecipes.commands.ItemCommand;
import com.cedricverlinden.customrecipes.commands.RecipeCommand;
import com.cedricverlinden.customrecipes.listeners.InventoryListener;
import com.cedricverlinden.customrecipes.listeners.PlayerListener;
import com.cedricverlinden.customrecipes.managers.FileManager;
import com.cedricverlinden.customrecipes.managers.ItemManager;
import com.cedricverlinden.customrecipes.managers.RecipeManager;
import com.cedricverlinden.customrecipes.utils.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CustomRecipes extends JavaPlugin {

	private static CustomRecipes instance;
	public static Log log;

	@Override
	public void onEnable() {
		instance = this;
		log = new Log();

		if (FileManager.getFiles("items") == null || FileManager.getFiles("recipes") == null) {
			log.info("No items or recipes found, creating default ones...");
			saveResource("items/test-item.yml", true);
			saveResource("recipes/test-recipe.yml", true);
		}

		ItemManager.registerItems();
		RecipeManager.registerRecipes();
		registerCommands();
		registerListeners();
	}

	@Override
	public void onDisable() {
		log.info("Disabling CustomRecipes, bye!");
	}

	private void registerCommands() {
		log.info("Registering commands...");
		Objects.requireNonNull(getCommand("debug")).setExecutor(new DebugCommand());
		Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
		Objects.requireNonNull(getCommand("recipe")).setExecutor(new RecipeCommand());
		log.info("Commands registered.");
	}

	private void registerListeners() {
		log.info("Registering listeners...");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		log.info("Listeners registered.");
	}

	public static CustomRecipes getInstance() {
		return instance;
	}
}
