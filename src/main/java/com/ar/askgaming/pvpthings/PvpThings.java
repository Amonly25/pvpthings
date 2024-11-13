package com.ar.askgaming.pvpthings;

import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Listeners.PlayerDeathListener;

public class PvpThings extends JavaPlugin {
    
    private PvpManager pvpManager;

    public PvpManager getPvpManager() {
        return pvpManager;
    }
    public void onEnable() {
        
        saveDefaultConfig();

        pvpManager = new PvpManager(this);

        getServer().getPluginCommand("pvp").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

    }
    public void onDisable() {
        
    }
}