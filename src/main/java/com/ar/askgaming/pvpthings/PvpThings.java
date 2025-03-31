package com.ar.askgaming.pvpthings;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Commands.PvpCommand;
import com.ar.askgaming.pvpthings.Commands.PvpManager;
import com.ar.askgaming.pvpthings.Contracts.Contract;
import com.ar.askgaming.pvpthings.Contracts.ContractManager;
import com.ar.askgaming.pvpthings.Contracts.ContractsCommand;
import com.ar.askgaming.pvpthings.DataBase.DataManager;
import com.ar.askgaming.pvpthings.DataBase.Language;
import com.ar.askgaming.pvpthings.Listeners.EntityDamageByEntityListener;
import com.ar.askgaming.pvpthings.Listeners.EntityDamageListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerCommandListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerDeathListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerJoinListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerQuitListener;
import com.ar.askgaming.pvpthings.Utilities.Dps;
import com.ar.askgaming.pvpthings.Utilities.Methods;
import com.ar.askgaming.pvpthings.Utilities.Recipes;

public class PvpThings extends JavaPlugin {
    
    private static PvpThings instance;

    private PvpManager pvpManager;
    private DataManager dataManager;
    private Dps dspTest;
    private ContractManager contractManager;
    private Language lang;
    private Methods methods;

    public void onEnable() {
        
        instance = this;
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(Contract.class,"Contract");

        dataManager = new DataManager();
        lang = new Language(this);
        pvpManager = new PvpManager(this);
        contractManager = new ContractManager(this);
        dspTest = new Dps(this);
        new Recipes(this);
        methods = new Methods(this);

        getServer().getPluginCommand("pvp").setExecutor(new PvpCommand(this));
        getServer().getPluginCommand("contract").setExecutor(new ContractsCommand(this));

        new PlayerDeathListener(this);
        new PlayerQuitListener(this);
        new PlayerJoinListener(this);
        new EntityDamageByEntityListener(this);
        new EntityDamageListener(this);
        new PlayerCommandListener(this);
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
    public Language getLang() {
        return lang;
    }
    public Methods getMethods() {
        return methods;
    }
    public static PvpThings getInstance() {
        return instance;
    }
    public DataManager getDataManager() {
        return dataManager;
    }
}