package com.ar.askgaming.pvpthings.Contracts;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;

public class Controller {

    private final PvpThings plugin;
    private final HashMap<UUID, Contract> contracts = new HashMap<>();
    private final File file;
    private FileConfiguration config;

    public Controller(PvpThings plugin) {
        this.plugin = plugin;

        new Commands();

        file = new File(plugin.getDataFolder(), "contracts.yml");

        load();
    }
    public void load(){
        contracts.clear();
        
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = config.getKeys(false);
        if (keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            Object obj = config.get(key);
            if (obj instanceof Contract) {
                Contract contract = (Contract) obj;
                contracts.put(UUID.fromString(key), contract);
            }
        }
    }
    public HashMap<UUID, Contract> getContracts() {
        return contracts;
    }
    public void save(Contract contract){
        config.set(contract.getId(), contract);
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createContract(Player hunter, Player hunted, double reward){
        String creator;
        if (hunter == null) {
            creator = "Server";
        } else {
            creator = hunter.getName();
        }
        Contract contract = new Contract(reward, hunted.getUniqueId(), creator);
        contracts.put(hunted.getUniqueId(), contract);
        save(contract);
    }
}
