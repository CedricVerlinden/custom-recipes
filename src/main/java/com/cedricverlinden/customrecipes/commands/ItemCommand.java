package com.cedricverlinden.customrecipes.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ExactMatchConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.cedricverlinden.customrecipes.CustomRecipes;
import com.cedricverlinden.customrecipes.managers.ItemManager;
import com.cedricverlinden.customrecipes.prompts.ItemCreationPrompt;
import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;

public class ItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        Log log = new Log();
        if (!(sender instanceof Player player)) {
            log.error("Only players can execute this command.");
            return true;
        }

        Chat chat = new Chat();

        if (args.length == 0) {
            player.sendMessage(chat.debug("Usage: /item <create>"));
            return true;
        }

        if (args.length >= 1) {
            String param = args[0].toLowerCase();
            if ("create".equals(param)) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand.getType().isAir()) {
                    player.sendMessage(chat.warning("Something went wrong, are you sure you are holding an item?"));
                    return true;
                }

                conversation(player);
            }
        }

        return true;
    }

    public void conversation(Player player) {
        Conversation conversation = new ConversationFactory(CustomRecipes.getInstance())
                .addConversationAbandonedListener(event -> {
                    Conversable conversable = event.getContext().getForWhom();

                    if (event.gracefulExit()) {
                        String displayName = (String) event.getContext().getSessionData("displayName");
                        String lore = event.getContext().getSessionData("lore").toString();
                        List<String> loreList = Arrays.asList(lore.split("<newline>"));

                        ItemManager item = new ItemManager(player.getInventory().getItemInMainHand().getType(),
                                displayName, loreList);
                        player.getInventory().addItem(item.getItemStack());
                        return;
                    }

                    conversable.sendRawMessage("Item creation has been cancelled.");
                })
                .withConversationCanceller(new ExactMatchConversationCanceller("exit"))
                .withTimeout(60)
                .withLocalEcho(false)
                .withFirstPrompt(new ItemCreationPrompt())
                .buildConversation(player);

        player.beginConversation(conversation);
    }

}