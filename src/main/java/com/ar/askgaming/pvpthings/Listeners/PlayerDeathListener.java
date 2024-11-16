package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class PlayerDeathListener implements Listener{

    private PvpThings plugin;
    public PlayerDeathListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        if (plugin.isCitizensEnabled()){
            try {
                if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
                    if (plugin.getPvpManager().getNpcPlayerLink().containsValue(npc)){
                        
                        npc.destroy();
                        //Check if this must be treated as OfflinePlayer
                        Player p = (Player) plugin.getPvpManager().getNpcPlayerLink().entrySet().stream()
                            .filter(entry -> entry.getValue().equals(npc))
                            .map(entry -> entry.getKey())
                            .findFirst()
                            .orElse(null);
                        plugin.getPvpManager().getNpcPlayerLink().remove(p);

                        PvpPlayer pvp = plugin.getPvpManager().getPvpPlayer(p);
                        pvp.setNpcKilled(true);
                        return;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
        Player pl = e.getEntity();
        PvpPlayer pvpl = plugin.getPvpManager().getPvpPlayer(pl);
        //pvp.setDeaths(pvp.getDeaths() + 1);
        pvpl.setLastDeathLocation(pl.getLocation());
    }
}
