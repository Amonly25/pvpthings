package com.ar.askgaming.pvpthings.PvpCombat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.pvpthings.PvpThings;

public class TopManager extends BukkitRunnable{

    private final PvpThings plugin;

    private final LinkedHashMap<UUID, Integer> topKills = new LinkedHashMap<>(); // <player, kills>
    private final LinkedHashMap<UUID, Integer> topDeaths = new LinkedHashMap<>(); // <player, deaths>
    private final LinkedHashMap<UUID, Integer> topKillstreak = new LinkedHashMap<>(); // <player, killstreak>
    private final LinkedHashMap<UUID, Double> topKdr = new LinkedHashMap<>(); // <player, kdr>

    public TopManager() {
        plugin = PvpThings.getInstance();
        runTaskTimer(plugin, 20, 20*60*5); // 5 minutes
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            updateTopStats();
        });
    }
    
    private void updateTopStats() {
        topKills.clear();
        topDeaths.clear();
        topKdr.clear();
        topKillstreak.clear();
    
        loadTopAsync("kills", topKills);
        loadTopAsync("deaths", topDeaths);
        loadTopAsync("highestkillstreak", topKillstreak);
        loadTopKDRAsync();
    }
    
    private void loadTopAsync(String column, HashMap<UUID, Integer> map) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "SELECT uuid, " + column + " FROM players ORDER BY " + column + " DESC LIMIT 10";
    
            try (Connection conn = plugin.getDataManager().getHikariSource().getConnection();
                 PreparedStatement statement = conn.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {
    
                HashMap<UUID, Integer> tempMap = new HashMap<>();
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    int value = rs.getInt(column);
                    tempMap.put(uuid, value);
                }
    
                // Actualizar en el Main Thread
                Bukkit.getScheduler().runTask(plugin, () -> map.putAll(tempMap));
    
            } catch (SQLException e) {
                plugin.getLogger().severe("Error updating top " + column + ": " + e.getMessage());
            }
        });
    }
    
    // Método especial para KDR (Kills/Deaths)
    private void loadTopKDRAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String sql = "SELECT uuid, (kills * 1.0 / deaths) AS kdr FROM players WHERE deaths > 0 ORDER BY kdr DESC LIMIT 10";
    
            try (Connection conn = plugin.getDataManager().getHikariSource().getConnection();
                 PreparedStatement statement = conn.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {
    
                HashMap<UUID, Double> tempMap = new HashMap<>();
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    double kdr = rs.getDouble("kdr");
                    tempMap.put(uuid, kdr);
                }
    
                // Actualizar en el Main Thread
                Bukkit.getScheduler().runTask(plugin, () -> topKdr.putAll(tempMap));
    
            } catch (SQLException e) {
                plugin.getLogger().severe("Error updating top KDR: " + e.getMessage());
            }
        });
    }
    public HashMap<UUID, Integer> getTopKills() {
        return topKills;
    }

    public HashMap<UUID, Integer> getTopDeaths() {
        return topDeaths;
    }

    public HashMap<UUID, Integer> getTopKillstreak() {
        return topKillstreak;
    }

    public HashMap<UUID, Double> getTopKdr() {
        return topKdr;
    }
    
    

}
