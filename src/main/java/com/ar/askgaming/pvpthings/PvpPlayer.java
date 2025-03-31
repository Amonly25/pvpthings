package com.ar.askgaming.pvpthings;

import java.util.UUID;

import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PvpPlayer{

    private PvpThings plugin = PvpThings.getInstance();

    private Integer kills, deaths, killstreak, highestKillstreak, kdr, timeSinceDeath;
  
    private Boolean inCombat;
    private UUID uuid;
    public PvpPlayer(UUID uuid, Integer kills, Integer deaths, Integer killstreak, Integer highestKillstreak, Integer timeSinceDeath) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.killstreak = killstreak;
        this.highestKillstreak = highestKillstreak;
        this.inCombat = false;
        this.timeSinceDeath = timeSinceDeath;

        this.kdr = (int) Math.round((double) kills / (deaths == 0 ? 1 : deaths));

        plugin.getServer().getScheduler().runTaskLater(plugin, 
        new Runnable() {
            @Override
            public void run() {
                checkAndUpdateData();
            }
        }, 20L);

    }
    public void checkAndUpdateData() {
        Player p = plugin.getServer().getPlayer(uuid);
        if (p == null) {
            plugin.getLogger().warning("Skipping update for player " + uuid.toString() + " because they are not online.");
            return;
        }
        kills = p.getStatistic(Statistic.KILL_ENTITY, EntityType.PLAYER);
        deaths = p.getStatistic(Statistic.DEATHS);
        timeSinceDeath = p.getStatistic(Statistic.TIME_SINCE_DEATH);

        save();
    }

    private void save(){
        plugin.getDataManager().savePlayerData(this);
    }

    public Integer getTimeSinceDeath() {
        return timeSinceDeath;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }
    public void setHighestKillstreak(int highestKillstreak) {
        this.highestKillstreak = highestKillstreak;
    }
    public void setTimeSinceDeath(int timeSinceDeath) {
        this.timeSinceDeath = timeSinceDeath;
    }

    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public UUID getUuid() {
        return uuid;
    }
    public int getKillstreak() {
        return killstreak;
    }
    public int getHighestKillstreak() {
        return highestKillstreak;
    }

    public int getKdr() {
        return kdr;
    }
    public void setKdr(int kdr) {
        this.kdr = kdr;
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }
}
