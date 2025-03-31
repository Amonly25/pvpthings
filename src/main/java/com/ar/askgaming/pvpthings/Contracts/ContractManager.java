package com.ar.askgaming.pvpthings.Contracts;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;

public class ContractManager {

    private final PvpThings plugin;
    private final HashMap<UUID, Contract> contracts = new HashMap<>();
    private final File file;
    private FileConfiguration config;

    public ContractManager(PvpThings plugin) {
        this.plugin = plugin;

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
