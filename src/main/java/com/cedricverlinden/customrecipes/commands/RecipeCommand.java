package com.cedricverlinden.customrecipes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.cedricverlinden.customrecipes.guis.RecipeCreationGUI;
import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;

public class RecipeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Log log = new Log();
        if (!(sender instanceof Player player)) {
            log.error("Only players can execute this command.");
            return true;
        }

        Chat chat = new Chat();
        
        if (args.length == 0) {
            player.sendMessage(chat.warning("Usage: /recipe create <name>"));
            return true;
        }

        if (args.length == 1) {
            player.sendMessage(chat.warning("Please specify a name for the recipe."));
            return true;
        }

        // join all args after the first one
        String recipeName = String.join(" ", args).substring(7).toLowerCase().replace(" ", "-");
        log.debug("Recipe name (command): " + recipeName);

        RecipeCreationGUI inventory = new RecipeCreationGUI(recipeName);
        inventory.open(player);
        
        return true;
    }

}
