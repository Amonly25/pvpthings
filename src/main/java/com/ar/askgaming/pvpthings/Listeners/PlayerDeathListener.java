package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerDeathListener implements Listener{

    private PvpThings plugin;
    public PlayerDeathListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player pl = e.getEntity();
        PvpPlayer pvpl = plugin.getPvpManager().getPvpPlayer(pl);
        if (pvpl == null) return;
        pvpl.setLastDeathLocation(pl.getLocation());
    }
}
