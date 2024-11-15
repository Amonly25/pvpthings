package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Managers.PvpManager;

public class PlayerJoinListener implements Listener{

    private PvpThings plugin;
    public PlayerJoinListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PvpPlayer pvp = plugin.getPvpManager().loadOrCreatePvpPlayer(p);

        PvpManager manager = plugin.getPvpManager();

        if (manager.getNpcPlayerLink().containsKey(p)){
            manager.switchFromNpc(p);
        } 
        if (pvp.isNpcKilled()){
            p.setHealth(0);
            pvp.setNpcKilled(false);
        }
    }
}
