package com.ar.askgaming.pvpthings.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Managers.PvpManager;

import net.citizensnpcs.api.CitizensAPI;

public class EntityDamageByEntityListener implements Listener{

    private PvpThings plugin;
    public EntityDamageByEntityListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
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

            plugin.getDps().setSpeedAttack(p);

            if (e.getEntity() instanceof Zombie) {
                Zombie zombie = (Zombie) e.getEntity();
                if (zombie.equals(plugin.getDps().getZombie())) {
                    zombie.setHealth(1000);
                    double damage = e.getDamage();
                    plugin.getDps().get(p, damage);
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

        PvpManager manager = plugin.getPvpManager();

        PvpPlayer pDamager = manager.getPvpPlayer(damager);
        PvpPlayer pDamaged = manager.getPvpPlayer(damaged);

        if (pDamaged.isInCombat()) {
            if (e.isCancelled()) {
                e.setCancelled(false);
            }
            
        } else {
            if (e.isCancelled()) {
                return;
            }
        }

        manager.setLastCombat(damager, 15);
        manager.setLastCombat(damaged, 15);
        // Tu lógica de combate aquí
        // if (!pDamager.isInCombat()) {
        //     manager.setLastCombat(damager, now);
        // }

        // if (!pDamaged.isInCombat()) {
        //     pDamaged.setInCombat(true);
        //     damaged.sendMessage("You are now in combat!");
        //     timer(pDamaged);
        // }
        
    } 
}
