package com.ar.askgaming.pvpthings.Listeners.PlayerListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerJoinListener implements Listener{

    private final PvpThings plugin;
    public PlayerJoinListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        plugin.getDataManager().loadOrCreatePvpPlayer(p.getUniqueId(), pvpPlayer -> {
            
        });
    }
}
