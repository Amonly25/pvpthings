package com.ar.askgaming.pvpthings.Contracts;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;

public class ContractsCommand implements TabExecutor {

    private PvpThings plugin;
    public ContractsCommand(PvpThings plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("create", "list");
        }
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       
       if (!(sender instanceof Player)) {
           sender.sendMessage("You must be a player to use this command");
           return false;
       }
        Player p = (Player) sender;
        switch (args[0].toLowerCase()) {
        case "create":
            createContract(p, args);
            break;
        case "list":
            listContracts(p,args);
            break;
        default:
            break;
        }
       
        return false;
    }
    public void createContract(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage("Usage: /contracts create <player> <amount>");
            return;
        }
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            p.sendMessage("Player not found");
            return;
        }
        if (target == p) {
            p.sendMessage("You can't create a contract with yourself");
            return;
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            p.sendMessage("Invalid amount");
            return;
        }
        if (amount <= 0) {
            p.sendMessage("Amount must be greater than 0");
            return;
        }
        plugin.getContractManager().createContract(p, target, amount);
        // Create contract
    }
    public void listContracts(Player p, String[] args) {
        // List contracts
        HashMap<UUID, Contract> contracts = plugin.getContractManager().getContracts();
        p.sendMessage("List of contracts:" + contracts.size());
        for (Contract contract : contracts.values()) {
            OfflinePlayer hunted = plugin.getServer().getOfflinePlayer(contract.getHunted());
            String message = "Hunted: " + hunted.getName() + " Reward: " + contract.getPrice();
            p.sendMessage(message);
        }
    }
}
