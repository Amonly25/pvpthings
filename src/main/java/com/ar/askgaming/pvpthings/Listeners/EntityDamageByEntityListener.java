package com.ar.askgaming.pvpthings.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ar.askgaming.pvpthings.PvpManager;
import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EntityDamageByEntityListener implements Listener{

    private PvpThings plugin;
    public EntityDamageByEntityListener(PvpThings plugin) {
        this.plugin = plugin;
    }

    private HashMap<Player, Long> lastHitTime = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Zombie){
            Zombie zombie = (Zombie) e.getEntity();
            if (zombie.equals(plugin.getDspTest().getZombie())){
                zombie.setHealth(1000);
                if (e.getDamager() instanceof Player){
                    Player player = (Player) e.getDamager();
                    
                    double damage = e.getDamage();
                    long currentTime = System.currentTimeMillis();
                    Long lastHit = lastHitTime.get(player);
                    double dps = 0;

                    if (lastHit != 0) {
                        long timeDifference = currentTime - lastHit;
                        dps = (damage / timeDifference) * 1000; // Convert to damage per second
                    }

                    lastHitTime.put(player,currentTime);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("DPS: " + String.format("%.2f", dps)));
                }
            }
            return;
        }
        

        // if (!(e.getEntity() instanceof Player)){
        //     return;
        // }
        // Player damaged = (Player) e.getEntity();
        // Player damager = null;
        // if (e.getDamager() instanceof Projectile){
        //     Projectile projectile = (Projectile) e.getDamager();
        //     if (projectile.getShooter() instanceof Player){
        //         damager = (Player) projectile.getShooter();
        //     }
        // }
        // if (e.getDamager() instanceof Player){
        //     damager = (Player) e.getDamager();
        // }

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Bukkit.broadcastMessage("test");
            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();
            PvpPlayer pDamager = PvpManager.getPvpPlayer(damager);
            PvpPlayer pDamaged = PvpManager.getPvpPlayer(damaged);
            if (!pDamager.isInCombat()) {
                pDamager.setInCombat(true);
                damager.sendMessage("You are now in combat!");
                timer(pDamager);
            }
            if (!pDamaged.isInCombat()) {
                pDamaged.setInCombat(true);
                damaged.sendMessage("You are now in combat!");
                timer(pDamaged);
            }
        }
    } 
    public void timer(PvpPlayer player) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                player.setInCombat(false);
                player.getPlayer().sendMessage("You are no longer in combat!");
            }
        }, 5 * 20L); // Convert seconds to ticks (20 ticks = 1 second)
    }
}
