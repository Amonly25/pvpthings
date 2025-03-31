package com.ar.askgaming.pvpthings.Listeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class EntityDamageListener implements Listener{

    private final PvpThings plugin;
    public EntityDamageListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)){
            return;
        }

        Player damaged = (Player) e.getEntity();

        List<String> disabledWorlds = plugin.getConfig().getStringList("disabled_worlds");
        for (String world : disabledWorlds) {
            if (damaged.getWorld().getName().equalsIgnoreCase(world)) {
                return;
            }
        }
        
        PvpPlayer pDamaged = plugin.getDataManager().getPvpPlayer(damaged.getUniqueId());
        if (pDamaged == null) {
            return;
        }

        if (pDamaged.isInCombat()) {
            if (e.isCancelled()) {
                if (e.getDamageSource().getCausingEntity() instanceof Player){
                    return;
                }
                e.setCancelled(false);
            }
        }     
    } 
}
