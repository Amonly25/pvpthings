package com.ar.askgaming.pvpthings.Contracts;

import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.pvpthings.PvpThings;

public class Task extends BukkitRunnable{

    private PvpThings plugin;
    private Integer contractDuration;
    private Integer createCooldown;
    public Task() {
        this.plugin = PvpThings.getInstance();

        runTaskTimer(plugin, 20*60, 20*60);

        load();
    }

    public void load() {
        contractDuration = plugin.getConfig().getInt("contracts.duration", 60);
        createCooldown = plugin.getConfig().getInt("contracts.create_cooldown", 60);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

}
