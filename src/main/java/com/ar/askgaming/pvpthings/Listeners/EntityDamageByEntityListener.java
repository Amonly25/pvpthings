package com.ar.askgaming.pvpthings.Listeners;

import java.util.List;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.PvpCombat.CombatController;

public class EntityDamageByEntityListener implements Listener{

    private final PvpThings plugin;
    public EntityDamageByEntityListener(PvpThings plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();

            double damage = e.getFinalDamage();
            plugin.getDps().get(p, damage);   
            plugin.getDps().setSpeedAttack(p);   
            if (e.getEntity().equals(plugin.getDps().getDpsEntity())){
                ((Damageable) e.getEntity()).setHealth(1000);
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
        

        List<String> disabledWorlds = plugin.getConfig().getStringList("disabled_worlds");
        for (String world : disabledWorlds) {
            if (damaged.getWorld().getName().equalsIgnoreCase(world)) {
                return;
            }
        }

        if (damaged.equals(damager)){
            return;

        }        

        PvpPlayer pDamager = plugin.getDataManager().getPvpPlayer(damager.getUniqueId());
        PvpPlayer pDamaged = plugin.getDataManager().getPvpPlayer(damaged.getUniqueId());
        if (pDamager == null || pDamaged == null) {
            return;
        }

        CombatController manager = plugin.getCombatController();
        if (e.isCancelled()){
            if (pDamaged.isInCombat() && pDamager.isInCombat()){
                e.setCancelled(false);
            } else {
                return;
            }
        }

        pDamaged.setInCombat(true);
        pDamager.setInCombat(true);
        manager.setLastCombat(damager, 15);
        manager.setLastCombat(damaged, 15);
        
    } 
}
