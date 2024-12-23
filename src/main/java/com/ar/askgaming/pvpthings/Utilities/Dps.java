package com.ar.askgaming.pvpthings.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ar.askgaming.pvpthings.PvpThings;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Dps {

    private PvpThings plugin;
    public Dps(PvpThings plugin) {
        this.plugin = plugin;
    }

    private final Map<Player, Long> lastUpdateTime = new HashMap<>();

    public Map<Player, Long> getLastUpdateTime() {
        return lastUpdateTime;
    }
    private Entity dpsEntity;

    public Entity getDpsEntity() {
        return dpsEntity;
    }

    private String name;

    public void spawn(Location loc){
        remove();
        String entity = plugin.getConfig().getString("dps_feature.entity_type","Skeleton");
        String name = plugin.getConfig().getString("dps_feature.name","DPS");
        
        try {
            dpsEntity = loc.getWorld().spawnEntity(loc, EntityType.valueOf(entity.toUpperCase()));
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid entity type: " + entity);
            return;
        }
        LivingEntity le = (LivingEntity) dpsEntity;
        le.setCustomNameVisible(true);
        le.setCustomName(name.replace("&", "§"));
        le.setAI(false);
        le.setSilent(true);
        le.setCollidable(false);
        le.setCanPickupItems(false);
        le.setRemoveWhenFarAway(false);
        le.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1000);
        le.setHealth(1000);
   

    }
    public void remove(){
        if (dpsEntity != null) {
            dpsEntity.remove();
        }
    }


    //private List<Material> swords = List.of(Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    private List<Material> axes = List.of(Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, 
    Material.WOODEN_AXE, Material.NETHERITE_AXE,Material.TRIDENT, Material.MACE);

    public void setSpeedAttack(Player player){
        ItemStack i = player.getInventory().getItemInMainHand();
        if (i == null || i.getType() == Material.AIR) return;

        if (axes.contains(i.getType())){
            player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(15);
            
            //player.sendMessage("Speed attack set to 8");
            return;
        } else {
            player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(24); 
           // player.sendMessage("Speed attack set to 7");
        }
    }
    public void get(Player p, double damage){
        
        // Obtener el tiempo actual
        long currentTime = System.currentTimeMillis();
        long lastAttackTime;
        if (lastUpdateTime.containsKey(p)) {
            lastAttackTime = lastUpdateTime.get(p);
        } else {
            lastAttackTime = currentTime;
            lastUpdateTime.put(p, lastAttackTime);
        }

        // Calcular el intervalo entre ataques (en segundos)
        double timeInterval = (currentTime - lastAttackTime) / 1000.0; // Convertir milisegundos a segundos

        // Si el tiempo entre ataques es menor que un segundo, calculamos el DPS
        if (timeInterval > 0) {
            double dps = damage / timeInterval; // Calcular el DPS (Daño por segundo)
            int cps = (int) (1 / timeInterval); // Calcular el CPS (Clics por segundo)
            // Enviar el valor del DPS al jugador

            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("DPS: " + String.format("%.2f", dps)+ " Cps: " + cps));

            // Actualizar el tiempo del último ataque y el daño infligido
            lastUpdateTime.put(p, currentTime);

        } 
    }

}
