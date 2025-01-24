package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerTeleportListener implements Listener{

    private PvpThings plugin;
    public PlayerTeleportListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }
    @EventHandler
    public void onEntityTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
            if (event.getCause() == TeleportCause.ENDER_PEARL){
                Location loc = event.getTo();

                if (loc.getBlock().getType().isSolid() || loc.getBlock().getRelative(0, 1, 0).getType().isSolid()) {
                    player.sendMessage("§cTeleport cancelled, prevent pearl exploit.");
                    event.setCancelled(true);

                }
            }
       
    }

}
