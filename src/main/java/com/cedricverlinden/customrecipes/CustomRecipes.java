package com.cedricverlinden.customrecipes;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import com.cedricverlinden.customrecipes.commands.DebugCommand;
import com.cedricverlinden.customrecipes.listeners.PlayerListener;
import com.cedricverlinden.customrecipes.managers.ItemManager;
import com.cedricverlinden.customrecipes.managers.RecipeManager;
import com.cedricverlinden.customrecipes.utils.Log;

public final class CustomRecipes extends JavaPlugin {

	private static CustomRecipes instance;
	public static Log log;

	@Override
	public void onEnable() {
		instance = this;
		log = new Log();

		saveResource("items/test-item.yml", true);
		saveResource("recipes/test-recipe.yml", true);

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
		log.info("Listeners commands.");
	}

	private void registerListeners() {
		log.info("Registering listeners...");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		log.info("Listeners registered.");
	}

	public static CustomRecipes getInstance() {
		return instance;
	}
}
