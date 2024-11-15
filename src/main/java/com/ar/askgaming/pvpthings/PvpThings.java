package com.ar.askgaming.pvpthings;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Listeners.EntityDamageByEntityListener;
import com.ar.askgaming.pvpthings.Listeners.InventoryClickListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerDeathListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerInteractListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerJoinListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerQuitListener;
import com.ar.askgaming.pvpthings.Managers.PvpManager;
import com.ar.askgaming.pvpthings.Utilities.Dps;
import com.ar.askgaming.pvpthings.Utilities.PvpInfo;
import com.ar.askgaming.pvpthings.Utilities.Recipes;

public class PvpThings extends JavaPlugin {
    
    private PvpManager pvpManager;
    private Dps dspTest;
    private Recipes recipes;
    private PvpInfo pvpInfo;

    private boolean citizensEnabled;

    public void onEnable() {
        
        saveDefaultConfig();

        pvpManager = new PvpManager(this);
        dspTest = new Dps(this);
        recipes = new Recipes(this);
        pvpInfo = new PvpInfo(this);
        recipes.add();

        getServer().getPluginCommand("pvp").setExecutor(new Commands(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);

        if (getServer().getPluginManager().getPlugin("Citizens") != null) {
            citizensEnabled = true;

        }

        for (Player p : getServer().getOnlinePlayers()) {
            pvpManager.loadOrCreatePvpPlayer(p);

        }
    }

    public void onDisable() {
        getDpsTest().remove();
    }
    public boolean isCitizensEnabled() {
        return citizensEnabled;
    }
    public PvpManager getPvpManager() {
        return pvpManager;
    }
    public Dps getDpsTest() {
        return dspTest;
    }
    public PvpInfo getPvpInfo() {
        return pvpInfo;
    }
}