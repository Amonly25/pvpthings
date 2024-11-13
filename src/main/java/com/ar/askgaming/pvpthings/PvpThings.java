package com.ar.askgaming.pvpthings;

import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Listeners.PlayerDeathListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerJoinListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerQuitListener;

public class PvpThings extends JavaPlugin {
    
    private PvpManager pvpManager;
    private DspTest dspTest;

    private boolean citizensEnabled;

    public void onEnable() {
        
        saveDefaultConfig();

        pvpManager = new PvpManager(this);
        dspTest = new DspTest(this);

        getServer().getPluginCommand("pvp").setExecutor(new Commands(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        if (getServer().getPluginManager().getPlugin("Citizens") != null) {
            citizensEnabled = true;

        }
    }

    public void onDisable() {
        
    }
    public boolean isCitizensEnabled() {
        return citizensEnabled;
    }
    public PvpManager getPvpManager() {
        return pvpManager;
    }
    public DspTest getDspTest() {
        return dspTest;
    }
}