package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;


public class PlayerQuitListener implements Listener {

    private PvpThings plugin;
    public PlayerQuitListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PvpPlayer p = plugin.getPvpManager().getPvpPlayer(player);
        if (p.isInCombat()) {
            if (plugin.getServer().getPluginManager().getPlugin("PvpThingsNpcAddon") == null) {
                player.setHealth(0);
                p.setInCombat(false);
                Bukkit.getOnlinePlayers().forEach(pl ->{
                    pl.sendMessage(plugin.getLang().get("death_logout",pl));
                });
            } 
        }
    }
}
