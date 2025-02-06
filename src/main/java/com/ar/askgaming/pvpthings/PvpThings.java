package com.ar.askgaming.pvpthings;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Commands.ContractsCommand;
import com.ar.askgaming.pvpthings.Commands.PvpCommand;
import com.ar.askgaming.pvpthings.Contracts.Contract;
import com.ar.askgaming.pvpthings.Listeners.EntityDamageByEntityListener;
import com.ar.askgaming.pvpthings.Listeners.EntityDamageListener;
import com.ar.askgaming.pvpthings.Listeners.InventoryClickListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerCommandListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerDeathListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerJoinListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerQuitListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerTeleportListener;
import com.ar.askgaming.pvpthings.Managers.ContractManager;
import com.ar.askgaming.pvpthings.Managers.LangManager;
import com.ar.askgaming.pvpthings.Managers.PvpManager;
import com.ar.askgaming.pvpthings.Utilities.Dps;
import com.ar.askgaming.pvpthings.Utilities.Logs;
import com.ar.askgaming.pvpthings.Utilities.Methods;
import com.ar.askgaming.pvpthings.Utilities.Recipes;

public class PvpThings extends JavaPlugin {
    
    private PvpManager pvpManager;
    private Dps dspTest;
    private Recipes recipes;
    private Logs logs;
    private ContractManager contractManager;
    private LangManager lang;
    private Methods methods;

    public void onEnable() {
        
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(PvpPlayer.class,"PvpPlayer");
        ConfigurationSerialization.registerClass(Contract.class,"Contract");
        lang = new LangManager(this);
        pvpManager = new PvpManager(this);
        pvpManager.runTaskTimer(this, 0, 20);
        contractManager = new ContractManager(this);
        dspTest = new Dps(this);
        recipes = new Recipes(this);
        logs = new Logs(this);
        methods = new Methods(this);
        getServer().getPluginManager().registerEvents(logs, this);
        recipes.add();

        getServer().getPluginCommand("pvp").setExecutor(new PvpCommand(this));
        getServer().getPluginCommand("contract").setExecutor(new ContractsCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        new PlayerTeleportListener(this);
        new PlayerCommandListener(this);

        for (Player p : getServer().getOnlinePlayers()) {
            pvpManager.loadOrCreatePvpPlayer(p);

        }
        Location loc = getConfig().getLocation("dps_feature.location");
        if (loc != null) {
            dspTest.spawn(loc);
        }
    }

    public void onDisable() {
        getDps().remove();
    }

    public PvpManager getPvpManager() {
        return pvpManager;
    }
    public Dps getDps() {
        return dspTest;
    }

    public ContractManager getContractManager() {
        return contractManager;
    }
    public LangManager getLang() {
        return lang;
    }
    public Methods getMethods() {
        return methods;
    }
}