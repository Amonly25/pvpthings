package com.ar.askgaming.pvpthings.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.ar.askgaming.pvpthings.PvpManager;
import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.trait.text.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EntityDamageByEntityListener implements Listener{

    private PvpThings plugin;
    public EntityDamageByEntityListener(PvpThings plugin) {
        this.plugin = plugin;
    }
    private long lastAttackTime = 0; // Tiempo del último ataque
    private double lastDamage = 0; // Daño del último golpe
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

        if (e.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) e.getEntity();
            if (zombie.equals(plugin.getDspTest().getZombie())) {
                zombie.setHealth(1000); // Assuming this is for a special case, otherwise it could be dangerous
                if (e.getDamager() instanceof Player) {
                    Player player = (Player) e.getDamager();

                    // Obtener el daño que se le está causando al zombie
                    double damage = e.getDamage();
                    
                    // Obtener el tiempo actual
                    long currentTime = System.currentTimeMillis();
    
                    // Calcular el intervalo entre ataques (en segundos)
                    double timeInterval = (currentTime - lastAttackTime) / 1000.0; // Convertir milisegundos a segundos
    
                    // Si el tiempo entre ataques es menor que un segundo, calculamos el DPS
                    if (timeInterval > 0) {
                        double dps = damage / timeInterval; // Calcular el DPS (Daño por segundo)
    
                        // Enviar el valor del DPS al jugador

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("DPS: " + String.format("%.2f", dps)));
    
                        // Actualizar el tiempo del último ataque y el daño infligido
                        lastAttackTime = currentTime;
                        lastDamage = damage;
                    }
                }
            }
            return;
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
