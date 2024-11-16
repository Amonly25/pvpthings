package com.ar.askgaming.pvpthings.Managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Contracts.Contract;

public class ContractManager {

    private PvpThings plugin;
    private HashMap<UUID, Contract> contracts = new HashMap<>();
    private File file;
    private FileConfiguration config;

    public HashMap<UUID, Contract> getContracts() {
        return contracts;
    }

    public ContractManager(PvpThings plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "contracts.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        config = new YamlConfiguration();
        try{
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Obtener todas las claves del nivel raíz
        Set<String> keys = config.getKeys(false);

        // Iterar sobre todas las keys y cargar cada Protection
        for (String key : keys) {
            Object obj = config.get(key);
            if (obj instanceof Contract) {
                Contract contract = (Contract) obj;
                contracts.put(UUID.fromString(key), contract);
    
            }
        }
    }
    public void save(Contract contract){
        config.set(contract.getHunted().toString(), contract);
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createContract(Player hunter, Player hunted, double reward){
        Contract contract = new Contract(reward, hunted.getUniqueId(), hunter.getUniqueId());
        contracts.put(hunted.getUniqueId(), contract);
        save(contract);
    }
}
