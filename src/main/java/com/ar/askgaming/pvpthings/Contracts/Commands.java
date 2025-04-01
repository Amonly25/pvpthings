package com.ar.askgaming.pvpthings.Contracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Utils.Utils;

public class Commands implements TabExecutor {

    private final PvpThings plugin;
    public Commands() {
        this.plugin = PvpThings.getInstance();

        plugin.getServer().getPluginCommand("contract").setExecutor(this);
    }

    private String getLang(String path, CommandSender sender){
        return plugin.getLang().get(path, sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("create", "list");
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       
        switch (args[0].toLowerCase()) {
        case "create":
            createContract(sender, args);
            break;
        case "list":
            listContracts(sender,args);
            break;
        default:
            break;
        }
       
        return false;
    }
    //#region Create
    public void createContract(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /contracts create <player> <amount>");
            return;
        }
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(getLang("commands.player_not_found", sender));
            return;
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(getLang("commands.invalid_number", sender));
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(getLang("commands.invalid_number", sender));
            return;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (target == sender) {
                return;
            }
            plugin.getContractController().createContract(p, target, amount);

        } else plugin.getContractController().createContract(null, target, amount);

    }
    //#region List
    public void listContracts(CommandSender sender, String[] args) {

        HashMap<UUID, Contract> contracts = plugin.getContractController().getContracts();

        if (contracts.isEmpty()) {
            sender.sendMessage(getLang("contracts.empty", sender));
            return;
        }
        sender.sendMessage(getLang("contracts.list", sender));
        
        List<String> list = new ArrayList<>();
        for (Contract contract : contracts.values()) {
            OfflinePlayer hunted = plugin.getServer().getOfflinePlayer(contract.getHunted());
            String huntedText = getLang("contracts.hunted", sender);
            String prize = getLang("contracts.prize", sender);
            String message = huntedText + hunted.getName() + " " + prize + contract.getPrize();
            list.add(message);
        }
        Utils.listPages(list, args, sender);
    }
}
//         