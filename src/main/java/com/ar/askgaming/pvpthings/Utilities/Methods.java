package com.ar.askgaming.pvpthings.Utilities;

import java.util.List;

import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;

public class Methods {

    private PvpThings plugin;
    public Methods(PvpThings plugin) {
        this.plugin = plugin;
    }

    public void listTops(List<String> list, String[] args, Player p, String type) {
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(plugin.getLang().get("commands.invalid_page", p));
                return;
            }
        }

        int totalPages = (int) Math.ceil(list.size() / 10.0);
        if (page > totalPages || page < 1) {
            p.sendMessage(plugin.getLang().get("commands.invalid_page", p));
            return;
        }

        int start = (page - 1) * 10;
        int end = Math.min(start + 10, list.size());
        p.sendMessage(plugin.getLang().get("misc.tops", p).replace("{top}", type) + " " + page + "/" + totalPages);
        for (int i = start; i < end; i++) {
            String string = list.get(i);
            p.sendMessage((i + 1) + ". " + string);
        }
    }
}
