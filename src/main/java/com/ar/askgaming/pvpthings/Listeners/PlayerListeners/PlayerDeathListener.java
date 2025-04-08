package com.ar.askgaming.pvpthings.Listeners.PlayerListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Contracts.Contract;
import com.ar.askgaming.pvpthings.Contracts.Controller;

public class PlayerDeathListener implements Listener{

    private final PvpThings plugin;
    private final Controller controller;
    public PlayerDeathListener(PvpThings plugin) {
        
        this.plugin = plugin;
        controller = plugin.getContractController();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();

        Player killer = p.getKiller();
        if (killer != null) {
            PvpPlayer playerKiller = plugin.getDataManager().getPvpPlayer(killer.getUniqueId());
            if (playerKiller != null) {
                playerKiller.checkAndUpdateData();
                playerKiller.setKillstreak(playerKiller.getKillstreak() + 1);
                if (playerKiller.getKillstreak() > playerKiller.getHighestKillstreak()) {
                    playerKiller.setHighestKillstreak(playerKiller.getKillstreak());
                }
            }
        }

        if (controller.hasContract(p.getUniqueId())) {
            Contract contract = controller.getContracts().get(p.getUniqueId());
            plugin.getContractController().removeContract(contract);

            if (killer != null) {
                controller.rewardContract(killer.getUniqueId(), contract);
                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                    pl.sendMessage(plugin.getLang().get("contracts.claimed", pl).replace("{player}", p.getName()).replace("{killer}", killer.getName()).replace("{prize}", String.valueOf(contract.getPrize())));
                }
            } else {
                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                    pl.sendMessage(plugin.getLang().get("contracts.died", pl).replace("{player}", p.getName()));
                }
            }
        }
        
        controller.updateDeathTime(p);

        PvpPlayer pvpPlayer = plugin.getDataManager().getPvpPlayer(p.getUniqueId());
        if (pvpPlayer == null) return;
        pvpPlayer.checkAndUpdateData();
        pvpPlayer.setKillstreak(0);
        pvpPlayer.setHighestKillstreak(0);
        pvpPlayer.setInCombat(false);

    }
}
