package com.ar.askgaming.pvpthings;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.ar.askgaming.pvpthings.Contracts.Contract;
import com.ar.askgaming.pvpthings.Contracts.Controller;
import com.ar.askgaming.pvpthings.DataBase.DataManager;
import com.ar.askgaming.pvpthings.DataBase.Language;
import com.ar.askgaming.pvpthings.Listeners.EntityDamageByEntityListener;
import com.ar.askgaming.pvpthings.Listeners.EntityDamageListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerCommandListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerDeathListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerJoinListener;
import com.ar.askgaming.pvpthings.Listeners.PlayerListeners.PlayerQuitListener;
import com.ar.askgaming.pvpthings.PvpCombat.CombatController;
import com.ar.askgaming.pvpthings.Utils.Dps;
import com.ar.askgaming.pvpthings.Utils.Recipes;

public class PvpThings extends JavaPlugin {
    
    private static PvpThings instance;

    private CombatController combatController;
    private DataManager dataManager;
    private Dps dspFeature;
    private Controller contractController;
    private Language lang;

    public void onEnable() {
        
        instance = this;
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(Contract.class,"Contract");

        dataManager = new DataManager();
        lang = new Language(this);
        combatController = new CombatController(this);
        contractController = new Controller(this);
        dspFeature = new Dps(this);

        new Recipes(this);

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

    public CombatController getCombatController() {
        return combatController;
    }
    public Dps getDps() {
        return dspFeature;
    }

    public Controller getContractController() {
        return contractController;
    }
    public Language getLang() {
        return lang;
    }

    public static PvpThings getInstance() {
        return instance;
    }
    public DataManager getDataManager() {
        return dataManager;
    }
}