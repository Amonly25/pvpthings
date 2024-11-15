package com.ar.askgaming.pvpthings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class PvpPlayer implements ConfigurationSerializable{

    private File file;
    private FileConfiguration config;
    private PvpThings plugin = PvpThings.getPlugin(PvpThings.class);

    public PvpPlayer(Player player) {
        this.player = player;
        this.kills = 0;
        this.deaths = 0;
        this.killstreak = 0;
        this.highestKillstreak = 0;
        this.inCombat = false;
        this.npcKilled = false;
        this.kdr = 0;
        this.headPrice = 0.0;

        file = new File(plugin.getDataFolder() + "/playerdata", player.getUniqueId() + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
        config.set(player.getUniqueId().toString(), this);
        save();
    }
    private void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PvpPlayer(Map<String, Object> map) {
        this.kills = (int) map.get("kills");
        this.deaths = (int) map.get("deaths");
        this.killstreak = (int) map.get("killstreak");
        this.highestKillstreak = (int) map.get("highestKillstreak");
        this.inCombat = (boolean) map.get("inCombat");
        this.npcKilled = (boolean) map.get("npcKilled");
        this.kdr = (int) map.get("kdr");
        this.headPrice = (double) map.get("headPrice");
    }
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("kills", kills);
        map.put("deaths", deaths);
        map.put("killstreak", killstreak);
        map.put("highestKillstreak", highestKillstreak);
        map.put("inCombat", inCombat);
        map.put("npcKilled", npcKilled);
        map.put("kdr", kdr);
        map.put("headPrice", headPrice);

        return map;
    }

    private Player player;
    private boolean npcKilled;
    private int kills;
    private int deaths;
    private int killstreak;
    private int highestKillstreak;
    private boolean inCombat;
    private int kdr;
    private double headPrice;

    public int getKdr() {
        return kdr;
    }
    public void setKdr(int kdr) {
        this.kdr = kdr;
    }
    public double getHeadPrice() {
        return headPrice;
    }
    public void setHeadPrice(double headPrice) {
        this.headPrice = headPrice;
    }
    public boolean isInCombat() {
        return inCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }
    public Player getPlayer() {
        return player;
    }
    public boolean isNpcKilled() {
        return npcKilled;
    }
    public void setNpcKilled(boolean npcKilled) {
        this.npcKilled = npcKilled;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }
}
