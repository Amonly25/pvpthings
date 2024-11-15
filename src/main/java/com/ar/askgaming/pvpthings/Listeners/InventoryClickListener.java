package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.ar.askgaming.pvpthings.PvpThings;

public class InventoryClickListener implements Listener{

    private PvpThings plugin;
    public InventoryClickListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Inventory inv = plugin.getPvpInfo().getInventory();
        if (e.getInventory().equals(inv) || e.getClickedInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}
