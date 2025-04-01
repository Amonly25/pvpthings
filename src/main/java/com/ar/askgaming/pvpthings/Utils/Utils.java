package com.ar.askgaming.pvpthings.Utils;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.realisticeconomy.RealisticEconomy;

import net.milkbowl.vault.economy.EconomyResponse;

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
    public static boolean hasMoney(Player player, Double amount){
        
        if (plugin.getRealisticEconomy() != null){
            RealisticEconomy economy = plugin.getRealisticEconomy();
            return economy.getEconomyService().getBalance(player.getUniqueId()) >= amount;
        }
        if (plugin.getVaultEconomy() != null){
            return plugin.getVaultEconomy().getBalance(player) >= amount;
        }
        player.sendMessage("No economy plugin found, please contact the server admin.");
        return false;
    }
    public static boolean removeMoney(Player player, Double amount){
        if (plugin.getRealisticEconomy() != null){
            RealisticEconomy economy = plugin.getRealisticEconomy();
            return economy.getServerBank().transferWithPlayer(player.getUniqueId(), amount, false);
        }
        if (plugin.getVaultEconomy() != null){
            EconomyResponse r = plugin.getVaultEconomy().withdrawPlayer(player, amount);
            return r.transactionSuccess();
        }
        return false;
    }
}
