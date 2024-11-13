package com.ar.askgaming.pvpthings;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class DspTest {

    private PvpThings plugin;
    public DspTest(PvpThings plugin) {
        this.plugin = plugin;
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

        z.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD));
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
