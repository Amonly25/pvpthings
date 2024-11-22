package com.ar.askgaming.pvpthings.Managers;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PvpManager extends BukkitRunnable{

    private HashMap<Player, PvpPlayer> pvpPlayers = new LinkedHashMap<>();

    public HashMap<Player, PvpPlayer> getPvpPlayers() {
        return pvpPlayers;
    }

    private PvpThings plugin;

    public PvpManager(PvpThings plugin) {
        this.plugin = plugin;

        File folder = new File(plugin.getDataFolder(), "/playerdata");

        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            loadOrCreatePvpPlayer(p);
        });
    }

    public PvpPlayer getPvpPlayer(Player p){
        return pvpPlayers.getOrDefault(p, loadOrCreatePvpPlayer(p));
    }

    public PvpPlayer loadOrCreatePvpPlayer(Player p) {
        if (pvpPlayers.containsKey(p)) {
            return pvpPlayers.get(p);
        }
        File file = new File(plugin.getDataFolder() + "/playerdata", p.getUniqueId() + ".yml");

        if (!file.exists()) {
            PvpPlayer pvp = new PvpPlayer(p);
            pvpPlayers.put(p, pvp);
            return pvp;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        PvpPlayer pvp = (PvpPlayer) config.get(p.getUniqueId().toString());
        pvp.setFile(file);
        pvp.setConfig(config);
        pvp.setPlayer(p);
        pvpPlayers.put(p, pvp);

        return pvp;

    }
 
    private HashMap<Player, Integer> lastCombat = new HashMap<>();

    public void setLastCombat(Player p, int time){
        PvpPlayer pvp = getPvpPlayer(p);
        pvp.setInCombat(true);
        if (!lastCombat.containsKey(p)){
            p.sendMessage(plugin.getLang().get("enter_combat",p));
        }
        lastCombat.put(p, time);
    }

    @Override
    public void run() {

        if (lastCombat.isEmpty()) return;

        Iterator<Entry<Player, Integer>> recorrer = lastCombat.entrySet().iterator();
        
        while (recorrer.hasNext()) {
            
            Entry<Player, Integer> entry = recorrer.next();
            
            Player player = entry.getKey();
            int time = entry.getValue();
            
            if (time >= 1) {
                String s = plugin.getLang().get("in_combat", player).replace("{time}", String.valueOf(time));
                sendActionBar(player, s);
                lastCombat.put(player, time - 1);
            }
            
            if (time == 0) {
                recorrer.remove();
                PvpPlayer pvp = getPvpPlayer(player);
                pvp.setInCombat(false);
                String s = plugin.getLang().get("leave_combat", player);
                player.sendMessage(s);
                sendActionBar(player, s);
            }
        }
    }
    private void sendActionBar(Player p, String message){
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(message));
    }
    public HashMap<String, Integer> getTop(String type){
        HashMap<String, Integer> map = new HashMap<>();
        for (PvpPlayer pvp : pvpPlayers.values()) {
            switch (type) {
                case "kills":
                    map.put(pvp.getPlayer().getName(), pvp.getKills());
                    break;
                case "deaths":
                    map.put(pvp.getPlayer().getName(), pvp.getDeaths());
                    break;
                case "killstreak":
                    map.put(pvp.getPlayer().getName(), pvp.getKillstreak());
                    break;
                case "kdr":
                    map.put(pvp.getPlayer().getName(), pvp.getKdr());
                    break;
                default:
                    break;
            }
        }
        return map.keySet().stream().sorted((a,b) -> map.get(b).compareTo(map.get(a))).collect(HashMap::new, (m, c) -> m.put(c, map.get(c)), HashMap::putAll);
    }
}
