package com.ar.askgaming.pvpthings.Listeners.PlayerListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;


public class PlayerQuitListener implements Listener {

    private final PvpThings plugin;
    public PlayerQuitListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PvpPlayer p = plugin.getDataManager().getPvpPlayer(player.getUniqueId());
        if (p == null) return;
        if (p.isInCombat()) {
            if (plugin.getServer().getPluginManager().getPlugin("PvpThingsNpcAddon") == null) {
                player.setHealth(0);
                player.getWorld().strikeLightningEffect(player.getLocation());
                p.setInCombat(false);
                Bukkit.getOnlinePlayers().forEach(pl ->{
                    pl.sendMessage(plugin.getLang().get("death_logout",pl).replace("{player}", player.getName()));
                });
            } 
        }
    }
}
