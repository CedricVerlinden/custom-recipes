package com.cedricverlinden.customrecipes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.cedricverlinden.customrecipes.guis.RecipeCreationGUI;
import com.cedricverlinden.customrecipes.managers.RecipeManager;
import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;

public class RecipeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        Log log = new Log();
        if (!(sender instanceof Player player)) {
            log.error("Only players can execute this command.");
            return true;
        }

        Chat chat = new Chat();
		if (!player.hasPermission("customrecipes.recipe")) {
			player.sendMessage(chat.color("You don't have permission to execute this command."));
			return true;
		}

        if (args.length == 0) {
            player.sendMessage(chat.color("<#e84855>Something went wrong executing this command. Try using: /recipe <create/list>"));
            return true;
        }

        String param = args[0].toLowerCase();
        if ("create".equals(param)) {
            if (args.length == 1) {
            player.sendMessage(chat.color("<#e84855>Please specify a name for the recipe. Try using: /recipe create <name>"));
                return true;
            }

            String recipeName = String.join(" ", args).substring(7).toLowerCase().replace(" ", "-");

            RecipeCreationGUI inventory = new RecipeCreationGUI(recipeName);
            inventory.open(player);
            return true;
        }

        if ("list".equals(param)) {
            player.sendMessage(chat.color("<#403F4C><strikethrough>----------------------------------------\n"));
            player.sendMessage(chat
                    .color("<#E84855>\u029F\u026As\u1D1B \u1D0F\uA730 \u1D00\u029F\u029F \u0280\u1D07\u1D04\u026A\u1D18\u1D07s:"));
            RecipeManager.getRecipes().forEach(recipe -> {
                player.sendMessage(chat.color(" <#403F4C>- <#3185FC>" + recipe));
            });
            player.sendMessage(chat.color("\n<#403F4C><strikethrough>----------------------------------------"));
            return true;
        }

        player.sendMessage(chat.color("<#e84855>Something went wrong executing this command. Try using: /recipe <create/list>"));
        return true;
    }

}
