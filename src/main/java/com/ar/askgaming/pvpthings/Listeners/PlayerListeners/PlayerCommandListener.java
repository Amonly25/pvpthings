package com.ar.askgaming.pvpthings.Listeners.PlayerListeners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerCommandListener implements Listener{

    private PvpThings plugin;

    public PlayerCommandListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();

        if (p.isOp()) return;

        List<String> blackList = plugin.getConfig().getStringList("command_blacklist");

        PvpPlayer pvpPlayer = plugin.getDataManager().getPvpPlayer(p.getUniqueId());
        if (pvpPlayer == null) return;
        if (!pvpPlayer.isInCombat()) return;
    
        String message = event.getMessage().toLowerCase(); // Convertir a minúsculas para evitar problemas con mayúsculas
    
        for (String command : blackList) {
            if (message.startsWith("/" + command.toLowerCase())) { // Comparar comando exacto
                event.setCancelled(true);
                p.sendMessage(plugin.getLang().get("commands.blacklist", p));
                return;
            }
        }
        
    }
}
