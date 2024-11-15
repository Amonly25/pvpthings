package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Managers.PvpManager;


public class PlayerQuitListener implements Listener {

    private PvpThings plugin;
    public PlayerQuitListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PvpPlayer p = PvpManager.getPvpPlayer(player);
        if (p.isInCombat()) {
            p.setInCombat(false);
            if (plugin.isCitizensEnabled() && plugin.getPvpManager().createNpcPlayerLink(player)){
                // Create a inventory backup
                player.getEquipment().clear();
                player.getInventory().clear();
                Bukkit.broadcastMessage("El jugador " + player.getName() + " ha abandonado el juego, pero su NPC sigue en combate");
            } else{
                player.setHealth(0);
                Bukkit.broadcastMessage("El jugador " + player.getName() + " ha abandonado el juego, y ha muerto en combate");
            }
        }
    }
}
