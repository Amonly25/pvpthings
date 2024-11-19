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
        Inventory inventory = e.getInventory();
        Inventory clickedInventory = e.getClickedInventory();

        if (inventory != null && inventory.equals(inv)) {
            e.setCancelled(true);
        }
        if (clickedInventory != null && clickedInventory.equals(inv)) {
            e.setCancelled(true);
        }
    }
}
