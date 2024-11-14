package com.ar.askgaming.pvpthings;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Listeners.EntityDamageByEntityListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerDeathListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerJoinListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerQuitListener;

public class PvpThings extends JavaPlugin {
    
    private PvpManager pvpManager;
    private DpsTest dspTest;

    private boolean citizensEnabled;

    public void onEnable() {
        
        saveDefaultConfig();

        pvpManager = new PvpManager(this);
        dspTest = new DpsTest(this);

        getServer().getPluginCommand("pvp").setExecutor(new Commands(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);

        if (getServer().getPluginManager().getPlugin("Citizens") != null) {
            citizensEnabled = true;

        }

        for (Player p : getServer().getOnlinePlayers()) {
            pvpManager.loadOrCreatePvpPlayer(p);

        }
    }

    public void onDisable() {
        getDspTest().remove();
    }
    public boolean isCitizensEnabled() {
        return citizensEnabled;
    }
    public PvpManager getPvpManager() {
        return pvpManager;
    }
    public DpsTest getDspTest() {
        return dspTest;
    }
}