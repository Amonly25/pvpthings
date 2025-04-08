package com.ar.askgaming.pvpthings.PvpCombat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class Commands implements TabExecutor{

    private final PvpThings plugin;
    public Commands() {
        this.plugin = PvpThings.getInstance();
        plugin.getServer().getPluginCommand("pvp").setExecutor(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> commands = new ArrayList<>(List.of("stats","tops"));
            if (sender.hasPermission("pvpthings.admin")) {
                commands.add("dps_entity");
                commands.add("reload");
            }
            return commands;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("tops")) {
                return Arrays.asList("kills","deaths","killstreak","kdr");
            }
        }
        return null;
    }

    private String getLang(String key, Player p) {
        return plugin.getLang().get(key, p);
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
            case "dps_entity":
                dpsEntity(p, args);
                break;
            case "stats":
                stats(p, args);
                break;
            case "tops":
            case "top":
                tops(p, args);
                break;
            case "reload":
                reload(p, args);
                break;
            default:
                p.sendMessage("§cUnknown command.");
                break;
        }
        return false;
    }
    //#region Dps
    public void dpsEntity(Player p, String[] args) {
        if (!p.hasPermission("pvpthings.admin")) {
            p.sendMessage("§cYou don't have permission to use this command.");
            return;
        }
        if (args.length < 2) {
            p.sendMessage("§cUsage: /pvp dps_entity <spawn/remove>");
            return;
        }
        switch (args[1].toLowerCase()) {
            case "spawn":
                plugin.getConfig().set("dps_feature", p.getLocation());
                plugin.saveConfig();
                plugin.getDps().spawn(p.getLocation());
                p.sendMessage("§aEntity spawned.");
                break;
            case "remove":
                if (plugin.getConfig().get("dps_feature") == null) {
                    p.sendMessage("§cNo entity to remove.");
                    return;
                }
                plugin.getDps().remove();
                p.sendMessage("§aEntity removed.");
                break;
            default:
                break;
        }
    }
    //#region Stats
    public void stats(Player p, String[] args) {
        PvpPlayer pvp = plugin.getDataManager().getPvpPlayer(p.getUniqueId());
        if (pvp == null) {
            p.sendMessage("§cYou have no stats yet.");
            return;
        }
        p.sendMessage(getLang("stats.kills", p) + pvp.getKills());
        p.sendMessage(getLang("stats.deaths", p) + pvp.getDeaths());
        p.sendMessage(getLang("stats.kdr", p) + pvp.getKdr());
        p.sendMessage(getLang("stats.killstreak", p) + pvp.getKillstreak());
        p.sendMessage(getLang("stats.highest_killstreak", p) + pvp.getHighestKillstreak());
        p.sendMessage(getLang("stats.time_since_death", p) + pvp.getTimeSinceDeathString());
    }
    //#region Tops
    public void tops(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage("Usage: /pvp tops <kills/deaths/killstreak/kdr>");
            return;
        }
        
        switch (args[1].toLowerCase()) {
            case "kills":
                plugin.getTopManager().getTopKills().forEach((uuid, kills) -> {
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                    p.sendMessage("§a" + offlinePlayer.getName() + " §7- §a" + kills);
                });
                break;
            case "deaths":
                plugin.getTopManager().getTopDeaths().forEach((uuid, deaths) -> {
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                    p.sendMessage("§a" + offlinePlayer.getName() + " §7- §a" + deaths);
                });
                break;
            case "killstreak":
                plugin.getTopManager().getTopKillstreak().forEach((uuid, killstreak) -> {
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                    p.sendMessage("§a" + offlinePlayer.getName() + " §7- §a" + killstreak);
                });
                break;
            case "kdr":
                plugin.getTopManager().getTopKdr().forEach((uuid, kdr) -> {
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                    String value = String.format("%.2f", kdr);
                    p.sendMessage("§a" + offlinePlayer.getName() + " §7- §a" + value);
                });
                break;
            default:
                p.sendMessage("Usage: /pvp tops <kills/deaths/killstreak/kdr>");
                break;
        }
    }
    //#region reload
    private void reload(Player p, String[] args) {
        if (!p.hasPermission("pvpthings.admin")) {
            p.sendMessage("§cYou don't have permission to use this command.");
            return;
        }
        plugin.reloadConfig();
        plugin.getDps().load();
        plugin.getLang().load();
        p.sendMessage("§aConfig reloaded.");
    }
}
