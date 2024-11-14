package com.ar.askgaming.pvpthings;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class DpsTest {

    private PvpThings plugin;
    public DpsTest(PvpThings plugin) {
        this.plugin = plugin;
    }
    private final Map<Player, Double> accumulatedDamage = new HashMap<>();
    private final Map<Player, Long> lastUpdateTime = new HashMap<>();

    public Map<Player, Double> getAccumulatedDamage() {
        return accumulatedDamage;
    }

    public Map<Player, Long> getLastUpdateTime() {
        return lastUpdateTime;
    }
    private Zombie z;

    private String name;

    public void spawn(Player p){
        remove();

        z = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
        z.setCustomName(name);
        z.setCustomNameVisible(true);
        z.setAI(false);
        z.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1000);
        z.setHealth(1000);

        //z.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD));
    }
    public void remove(){
        if (z != null) {
            z.remove();
        }
    }
    public Zombie getZombie() {
        return z;
    }
}
