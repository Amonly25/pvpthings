package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Managers.PvpManager;

import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EntityDamageByEntityListener implements Listener{

    private PvpThings plugin;
    public EntityDamageByEntityListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (plugin.isCitizensEnabled()){
            if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
                return;
            }
            if (CitizensAPI.getNPCRegistry().isNPC(e.getDamager())) {
                return;
            }
        }

        if (e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();

            plugin.getDpsTest().setSpeedAttack(p);

            if (e.getEntity() instanceof Zombie) {
                Zombie zombie = (Zombie) e.getEntity();
                if (zombie.equals(plugin.getDpsTest().getZombie())) {
                    zombie.setHealth(1000);
                    double damage = e.getDamage();
                    plugin.getDpsTest().get(p, damage);
                }
                return;
            }          
        }
        
        if (!(e.getEntity() instanceof Player)){
            return;
        }

        Player damaged = (Player) e.getEntity();
        Player damager = null;
        if (e.getDamager() instanceof Player){
            damager = (Player) e.getDamager();

        } else if (e.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player){
                damager = (Player) projectile.getShooter();
            } else {return;}
        } else {return;}

        PvpPlayer pDamager = PvpManager.getPvpPlayer(damager);
        PvpPlayer pDamaged = PvpManager.getPvpPlayer(damaged);
        if (!pDamager.isInCombat()) {
            if (e.isCancelled()){
                return;
            }
            pDamager.setInCombat(true);
            damager.sendMessage("You are now in combat!");
            timer(pDamager);
        }
        if (!pDamaged.isInCombat()) {
            if (e.isCancelled()){
                return;
            }
            pDamaged.setInCombat(true);
            damaged.sendMessage("You are now in combat!");
            timer(pDamaged);
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
