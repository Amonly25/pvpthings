package com.ar.askgaming.pvpthings.Contracts;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;
import com.ar.askgaming.pvpthings.Utils.Utils;
import com.ar.askgaming.universalnotifier.Managers.AlertManager.Alert;

public class Controller {

    private final PvpThings plugin;
    private final HashMap<UUID, Contract> contracts = new HashMap<>();
    private final File file;
    private FileConfiguration config;
    private Integer createCooldown, defaultPrize;

    public Controller(PvpThings plugin) {
        this.plugin = plugin;

        new Commands(this);

        file = new File(plugin.getDataFolder(), "contracts.yml");

        load();
    }
    public void load(){
        contracts.clear();

        createCooldown = plugin.getConfig().getInt("contracts.create_cooldown", 60);
        defaultPrize = plugin.getConfig().getInt("contracts.default_prize", 200);
        
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
            if (key.equals("death_times")) {
                continue;
            }
            String id = key;
            Integer prize = config.getInt(key + ".prize");
            UUID hunted = UUID.fromString(config.getString(key + ".hunted"));
            String creator = config.getString(key + ".contractors");
            long createdTime = config.getLong(key + ".createdTime");

            Contract contract = new Contract(id, prize, hunted, creator, createdTime);
            contracts.put(hunted, contract);
        }
    }
    public HashMap<UUID, Contract> getContracts() {
        return contracts;
    }
    private void save(){
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveContract(Contract contract) {
        config.set(contract.getId() + ".prize", contract.getPrize());
        config.set(contract.getId() + ".hunted", contract.getHunted().toString());  
        config.set(contract.getId() + ".contractors", contract.getContractors());
        config.set(contract.getId() + ".createdTime", contract.getCreatedTime());
        save();
    }
    //#region create
    public void createContract(String creator, UUID hunted, Integer prize){
        Contract contract = new Contract(prize, hunted, creator);
        contracts.put(hunted, contract);
        saveContract(contract);
        OfflinePlayer huntedPlayer = plugin.getServer().getOfflinePlayer(hunted);
        String huntedName = huntedPlayer.getName() != null ? huntedPlayer.getName() : "Unknown";
        for (Player pl : plugin.getServer().getOnlinePlayers()) {
            pl.sendMessage(plugin.getLang().get("contracts.created", pl).replace("{player}", huntedName).replace("{prize}", prize+"").replace("{creator}", creator));
        }
        if (plugin.getUniversalNotifier() != null) {
            String message = plugin.getConfig().getString("notifier.created").replace("{player}", huntedName).replace("{prize}", prize+"");  
            plugin.getUniversalNotifier().getNotificationManager().broadcastToAll(Alert.CUSTOM, message);

        }
    }
    public boolean hasContract(UUID uuid) {
        return contracts.containsKey(uuid);
    }
    //#region addCreator
    public void addContractor(UUID uuid, String creator, Integer amount) {
        Contract contract = contracts.get(uuid);
        if (contract != null) {
            contract.addContractor(creator);
            contract.setPrize(contract.getPrize() + amount);
            config.set(contract.getId() + ".contractors", contract.getContractors());
            config.set(contract.getId() + ".prize", contract.getPrize());
            save();
        }
    }
    //#region remove
    public void removeContract(Contract contract) {
        contracts.remove(contract.getHunted());
        config.set(contract.getId(), null);
        save();
    }
    public void updateDeathTime(Player player){
        PvpPlayer pvpPlayer = plugin.getDataManager().getPvpPlayer(player.getUniqueId());
        int timeSinceDeath = player.getStatistic(Statistic.TIME_SINCE_DEATH);
        if (pvpPlayer != null) {
            pvpPlayer.setTimeSinceDeath(0);
            config.set("death_times."+ player.getUniqueId().toString(), timeSinceDeath);
            save();
        }
    }
    public void resetDeathTimes(){
       config.set("death_times", null);
       save();
    }
    public HashMap<UUID, Integer> getDeathTimes(){
        HashMap<UUID, Integer> deathTimes = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("death_times");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int time = section.getInt(key);
                deathTimes.put(uuid, time);
            }
        }
        return deathTimes;
    }
    public UUID getMostTimeSinceDeath(){
        HashMap<UUID, Integer> deathTimes = getDeathTimes();
        UUID mostTime = null;
        int maxTime = 0;
        for (UUID uuid : deathTimes.keySet()){
            if (deathTimes.get(uuid) > maxTime){
                maxTime = deathTimes.get(uuid);
                mostTime = uuid;
            }
        }
        return mostTime;
    }
    public void rewardContract(UUID winner, Contract contract) {
        Integer prize = contract.getPrize();
        Utils.addMoney(plugin.getServer().getPlayer(winner), prize);
        Player player = plugin.getServer().getPlayer(winner);
        if (player != null) {
            player.sendMessage(plugin.getLang().get("contracts.reward", player).replace("{money}", prize.toString()));
        }   
    }
    public void updateLastDeathTime(Player player) {
        Integer timeSinceDeath = player.getStatistic(Statistic.TIME_SINCE_DEATH);
        config.set("death_times." + player.getUniqueId().toString(), timeSinceDeath);
        save();
    }
    public void createAutoContracts() {
        createContract("Server", getMostTimeSinceDeath(), defaultPrize);
        plugin.getConfig().set("contracts.last", createCooldown);
        plugin.saveConfig();
    }
}   
