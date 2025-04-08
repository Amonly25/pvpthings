package com.ar.askgaming.pvpthings.Utils;

import org.bukkit.OfflinePlayer;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolders extends PlaceholderExpansion {

    private final PvpThings plugin;

    public PlaceHolders() {
        this.plugin = PvpThings.getInstance();

        register();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        PvpPlayer pvpPlayer = plugin.getDataManager().getPvpPlayer(player.getUniqueId());
        if (pvpPlayer == null) {
            return "No data found";
        }
        switch (identifier.toLowerCase()) {
            case "kills":
                return String.valueOf(pvpPlayer.getKills());
            case "deaths":
                return String.valueOf(pvpPlayer.getDeaths());
            case "killstreak":
                return String.valueOf(pvpPlayer.getKillstreak());
            case "highestkillstreak":
                return String.valueOf(pvpPlayer.getHighestKillstreak());
            case "kdr":
                return String.valueOf(pvpPlayer.getKdr());
            case "time_since_death":
                return pvpPlayer.getTimeSinceDeathString();
            default:
                return "Invalid placeholder: ";
        }
    }

    @Override
    public String getAuthor() {
        return "AskGaming";
    }

    @Override
    public String getIdentifier() {
        return "pvpthings";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
    
}
