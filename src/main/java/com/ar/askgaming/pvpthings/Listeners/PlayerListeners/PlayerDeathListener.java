package com.ar.askgaming.pvpthings.Listeners.PlayerListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerDeathListener implements Listener{

    private final PvpThings plugin;
    public PlayerDeathListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        PvpPlayer pvpPlayer = plugin.getDataManager().getPvpPlayer(p.getUniqueId());
        if (pvpPlayer == null) return;
        pvpPlayer.setInCombat(false);

    }
}
