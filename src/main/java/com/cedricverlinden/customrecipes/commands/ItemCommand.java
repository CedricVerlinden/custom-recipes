package com.cedricverlinden.customrecipes.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

        // TODO: add permission

        if (args.length == 0) {
            player.sendMessage(chat.color("<#e84855>Something went wrong executing this command. Try using: /item <create/list>"));
            return true;
        }

        String param = args[0].toLowerCase();
        if ("create".equals(param)) {
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (hand.getType().isAir()) {
                player.sendMessage(chat.color("<#e84855>Something went wrong, are you sure you are holding an item?"));
                return true;
            }

            conversation(player);
            return true;
        }

        if ("list".equals(param)) {
            player.sendMessage(chat.color("<#403F4C><strikethrough>----------------------------------------\n"));
            player.sendMessage(chat.color("<#E84855>\u029F\u026As\u1D1B \u1D0F\uA730 \u1D00\u029F\u029F \u026A\u1D1B\u1D07\u1D0Ds:"));
            ItemManager.getItems().keySet().forEach(item -> {
                ItemManager im = ItemManager.getItems().get(item);
                String lore = im.getLore().toString().replace("[", "").replace("]", "").replace(", ", "\n");
                player.sendMessage(chat.color(" <#403F4C>-  <hover:show_text:\"" + im.getDisplayName() + "\n" + lore +"\"><#3185FC>" + chat.stripString(im.getDisplayName()) + "</hover> <#F9DC5C><italic>(file: " + item + ".yml)"));
            });
            player.sendMessage(chat.color("\n<#403F4C><strikethrough>----------------------------------------"));
            return true;
        }

        player.sendMessage(chat.color("<#e84855>Something went wrong executing this command. Try using: /item <create/list>"));
        return true;
    }

    public void conversation(Player player) {
        Conversation conversation = new ConversationFactory(CustomRecipes.getInstance())
                .addConversationAbandonedListener(event -> {
                    Chat chat = new Chat();
                    if (event.gracefulExit()) {
                        String displayName = (String) event.getContext().getSessionData("displayName");
                        String lore = event.getContext().getSessionData("lore").toString();
                        List<String> loreList = Arrays.asList(lore.split("<newline>"));

                        ItemManager item = new ItemManager(player.getInventory().getItemInMainHand().getType(),
                                displayName, loreList);
                        player.getInventory().addItem(item.getItemStack());
                        player.sendMessage(chat.color("<#3185fc>Item has been created and added to your inventory."));
                        return;
                    }

                    player.sendMessage(chat.color("<#e84855>Item creation has been cancelled."));
                })
                .withConversationCanceller(new ExactMatchConversationCanceller("exit"))
                .withTimeout(60)
                .withLocalEcho(false)
                .withFirstPrompt(new ItemCreationPrompt())
                .buildConversation(player);

        player.beginConversation(conversation);
    }

}