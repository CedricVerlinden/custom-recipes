package com.cedricverlinden.customrecipes.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.cedricverlinden.customrecipes.managers.ItemManager;
import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;

public class ForgerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Log log = new Log();
        if (!(sender instanceof Player player)) {
            log.error("Only players can execute this command.");
            return true;
        }

        Chat chat = new Chat();
        if (!player.hasPermission("forger.reload")) {
            player.sendMessage(chat.color("<red>You don't have permission to execute this command."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(chat.color("<red>Usage: /forger reload"));
            return true;
        }

        String param = args[0].toLowerCase();
        if ("reload".equals(param)) {
            player.sendMessage(chat.color("<green>Reloading Forger..."));
            player.sendMessage(chat.color("<gold><italic>Only the items will be reloaded, not the recipes!"));

            try {
                ItemManager.getItems().clear();
                ItemManager.registerItems();
            } catch (NullPointerException ex) {
                player.sendMessage(chat.color(
                        "<red>Something went wrong while reloading Forger, check the console for more information."));
                log.error(
                        "Something went wrong while trying to reload the items, before reporting this error, please try restarting the server.");
                log.error("Stacktrace:");
                ex.printStackTrace();
                return true;
            }

            player.sendMessage(chat.color("<green>Forger reloaded!"));
            return true;
        }

        player.sendMessage(chat.color("<red>Usage: /forger reload"));
        return true;
    }
}
