package com.ar.askgaming.pvpthings.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class PvpCommand implements TabExecutor{

    private PvpThings plugin;
    public PvpCommand(PvpThings plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of("spawn_dps_entity", "info", "despawn_dps_entity", "back","stats","tops");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length == 0) {
            sender.sendMessage("PvP Things Plugin");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command");
            return false;
        }
        Player p = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "spawn_dps_entity":
                spawnDps(p, args);
                break;
            case "despawn_dps_entity":
                plugin.getDps().remove();
                break;
            case "back":
                back(p,args);
            case "stats":
                stats(p,args);
                break;
            case "tops":
            case "top":
                tops(p,args);
                break;
            default:
                break;
        }
        return false;
    }
    public void spawnDps(Player p, String[] args) {
        plugin.getConfig().set("dps_feature", p.getLocation());
        plugin.saveConfig();
        plugin.getDps().spawn(p.getLocation());
        p.sendMessage("§aEntity spawned.");
    }

    public void back(Player p, String[] args) {
        PvpPlayer pvp = plugin.getPvpManager().loadOrCreatePvpPlayer(p);
        if (pvp.getLastDeathLocation() != null) {
            p.teleport(pvp.getLastDeathLocation());
        }
    }
    public void stats(Player p, String[] args) {
        PvpPlayer pvp = plugin.getPvpManager().loadOrCreatePvpPlayer(p);
        p.sendMessage("Kills: " + pvp.getKills());
        p.sendMessage("Deaths: " + pvp.getDeaths());
        p.sendMessage("K/D: " + pvp.getKdr());
        p.sendMessage("Killstreak: " + pvp.getKillstreak());
        p.sendMessage("Highest Killstreak: " + pvp.getHighestKillstreak());

    }
    public void tops(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage("Usage: /pvp tops <kills/deaths/killstreak/kdr>");
            return;
        }

        String type = args[1].toLowerCase();
        List<String> validTypes = Arrays.asList("kills", "deaths", "killstreak", "kdr");

        if (!validTypes.contains(type)) {
            p.sendMessage("Usage: /pvp tops <kills/deaths/killstreak/kdr>");
            return;
        }

        HashMap<String, Integer> map = plugin.getPvpManager().getTop(type);
        List<String> list = new ArrayList<>();
        map.entrySet().forEach(e -> list.add(e.getKey() + " - " + e.getValue() + " " + type));
        plugin.getMethods().listTops(list, args, p, type);
    }
}
