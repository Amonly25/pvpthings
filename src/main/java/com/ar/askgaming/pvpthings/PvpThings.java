package com.ar.askgaming.pvpthings;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

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
import com.ar.askgaming.pvpthings.PvpCombat.TopManager;
import com.ar.askgaming.pvpthings.Utils.Dps;
import com.ar.askgaming.pvpthings.Utils.Recipes;
import com.ar.askgaming.realisticeconomy.RealisticEconomy;
import com.ar.askgaming.realisticeconomy.Utilities.PlaceHolders;
import com.ar.askgaming.universalnotifier.UniversalNotifier;

import net.milkbowl.vault.economy.Economy;

public class PvpThings extends JavaPlugin {
    
    private static PvpThings instance;

    private CombatController combatController;
    private DataManager dataManager;
    private Dps dspFeature;
    private Controller contractController;
    private Language lang;
    private TopManager topManager;

    private Economy vaultEconomy;
    private RealisticEconomy realisticEconomy;
    private UniversalNotifier universalNotifier;

    public void onEnable() {
        
        instance = this;
        saveDefaultConfig();
        
        dataManager = new DataManager();
        lang = new Language(this);
        combatController = new CombatController(this);
        contractController = new Controller(this);
        dspFeature = new Dps(this);
        topManager = new TopManager();

        new Recipes(this);

        new PlayerDeathListener(this);
        new PlayerQuitListener(this);
        new PlayerJoinListener(this);
        new EntityDamageByEntityListener(this);
        new EntityDamageListener(this);
        new PlayerCommandListener(this);

        if (getServer().getPluginManager().isPluginEnabled("RealisticEconomy")) {
            getLogger().info("RealisticEconomy found!");
            realisticEconomy = RealisticEconomy.getInstance();
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            getLogger().info("PlaceHolderAPI found!");
            new PlaceHolders();
        }
        if (getServer().getPluginManager().isPluginEnabled("UniversalNotifier")) {
            getLogger().info("UniversalNotifier found!");
            universalNotifier = UniversalNotifier.getInstance();
        } else {
            getLogger().info("UniversalNotifier not found!");
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                getLogger().info("Non economy plugin found!");
            } else {
                vaultEconomy = rsp.getProvider();
                getLogger().info("Vault Economy found!");
            }

        } else {
            getLogger().info("Vault not found!");
            return;
        }
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
    public Economy getVaultEconomy() {
        return vaultEconomy;
    }
    public RealisticEconomy getRealisticEconomy() {
        return realisticEconomy;
    }
    public TopManager getTopManager() {
        return topManager;
    }
    public UniversalNotifier getUniversalNotifier() {
        return universalNotifier;
    }
}