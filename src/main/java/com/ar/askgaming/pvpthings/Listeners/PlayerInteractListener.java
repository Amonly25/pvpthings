package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.ar.askgaming.pvpthings.PvpThings;

public class PlayerInteractListener implements Listener{

    private PvpThings plugin;
    public PlayerInteractListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) return;

    }
    

}
