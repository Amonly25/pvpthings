package com.ar.askgaming.pvpthings.Utils;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.ar.askgaming.pvpthings.PvpThings;

public class Utils {

    private static PvpThings plugin = PvpThings.getInstance();

    public static void listPages(List<String> list, String[] args, CommandSender sender) {
        int page = 1;
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getLang().get("commands.invalid_page", sender));
                return;
            }
        }

        int totalPages = (int) Math.ceil(list.size() / 10.0);
        if (page > totalPages || page < 1) {
            sender.sendMessage(plugin.getLang().get("commands.invalid_page", sender));
            return;
        }

        int start = (page - 1) * 10;
        int end = Math.min(start + 10, list.size());
        sender.sendMessage(plugin.getLang().get("commands.listing", sender) + " " + page + "/" + totalPages);
        for (int i = start; i < end; i++) {
            String string = list.get(i);
            sender.sendMessage((i + 1) + ". " + string);
        }
    }
}
