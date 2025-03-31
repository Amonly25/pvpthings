package com.ar.askgaming.pvpthings.Listeners.PlayerListeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerTeleportListener implements Listener{

    private final PvpThings plugin;
    public PlayerTeleportListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onEntityTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
    
        if (event.getCause() == TeleportCause.ENDER_PEARL) {
            Location from = player.getLocation();
            Location to = event.getTo();
            
            Block targetBlock = to.getBlock();
            Block aboveBlock = targetBlock.getRelative(0, 1, 0);
        
            // Calcular la dirección del TP y el siguiente bloque en esa dirección
            Vector direction = to.toVector().subtract(from.toVector()).normalize();
            Location nextLocation = to.clone().add(direction);
            Block nextBlock = nextLocation.getBlock();
            Block nextAboveBlock = nextBlock.getRelative(0, 1, 0);

            if (targetBlock.isPassable()) {
                return;

            }
    
            List<String> blacklistMaterials = plugin.getConfig().getStringList("anti_pearl.blacklist_materials");

            for (String mat : blacklistMaterials) {
                if (targetBlock.getType().toString().replace("_", "").contains(mat)) {
                    return;
                }
            }

            // Si el destino o el siguiente bloque en la dirección es sólido, cancelar
            if (targetBlock.getType().isSolid() || aboveBlock.getType().isSolid()) {
                
                player.sendMessage(plugin.getLang().get("antipearl", player));
                event.setCancelled(true);
                return;
            }
            if (player.getLocation().distanceSquared(nextLocation) < 16){ 
                
                if (nextBlock.getType().isSolid() || nextAboveBlock.getType().isSolid()) {
                
                    player.sendMessage(plugin.getLang().get("antipearl", player));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}


