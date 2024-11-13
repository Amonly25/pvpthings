package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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

        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
            if (plugin.getPvpManager().getNpcs().contains(npc)){
                plugin.getPvpManager().getNpcs().remove(npc);
                npc.destroy();
            }
        }
    }
}
