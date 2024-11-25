package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class EntityDamageListener implements Listener{

    private PvpThings plugin;
    public EntityDamageListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)){
            return;
        }

        Player damaged = (Player) e.getEntity();
        PvpPlayer pDamaged = plugin.getPvpManager().getPvpPlayer(damaged);

        if (pDamaged.isInCombat()) {
            if (e.isCancelled()) {
                e.setCancelled(false);
            }
            
        } else {
            if (e.isCancelled()) {
                return;
            }
        }        
    } 
}
