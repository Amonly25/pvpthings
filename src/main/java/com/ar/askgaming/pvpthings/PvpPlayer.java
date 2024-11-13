package com.ar.askgaming.pvpthings;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PvpPlayer {

    private Player player;

    private OfflinePlayer offlinePlayer;
    private int kills;
    private int deaths;
    private int killstreak;
    private int highestKillstreak;
    
    private boolean inCombat;

    public PvpPlayer(Player player) {
        this.player = player;
        this.offlinePlayer = player;
        this.kills = 0;
        this.deaths = 0;
        this.killstreak = 0;
        this.highestKillstreak = 0;
        this.inCombat = false;
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
}
