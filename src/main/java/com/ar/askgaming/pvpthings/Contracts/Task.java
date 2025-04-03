package com.ar.askgaming.pvpthings.Contracts;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.pvpthings.PvpThings;

public class Task extends BukkitRunnable{

    private final PvpThings plugin;
    private final Controller controller;

    private Integer contractDuration;
    private Integer createCooldown;
    private Integer lastCreateTime;
    private Integer defaultPrize;

    public Task() {
        this.plugin = PvpThings.getInstance();
        this.controller = plugin.getContractController();

        runTaskTimer(plugin, 20*60, 20*60);

        load();
    }

    public void load() {
        contractDuration = plugin.getConfig().getInt("contracts.duration", 60);
        createCooldown = plugin.getConfig().getInt("contracts.create_cooldown", 60);
        lastCreateTime = plugin.getConfig().getInt("contracts.last", 0);
        defaultPrize = plugin.getConfig().getInt("contracts.default_prize", 200);
    }

    @Override
    public void run() {
        if (lastCreateTime > 0) {
            lastCreateTime -= 1;
            plugin.getConfig().set("contracts.last", lastCreateTime);
            plugin.saveConfig();
        } else{
            controller.createContract("Server", controller.getMostTimeSinceDeath(), defaultPrize);
            plugin.getConfig().set("contracts.last", createCooldown);
            plugin.saveConfig();
        }
        controller.getContracts().forEach((uuid, contract) -> {
            if (System.currentTimeMillis() - contract.getCreatedTime() > contractDuration * 60 * 1000) {
                controller.rewardContract(contract.getHunted(), contract);
                for (Player pl : plugin.getServer().getOnlinePlayers()) {
                    pl.sendMessage(plugin.getLang().get("contracts.expired", pl).replace("{player}", contract.getHunted().toString()));
                }
                controller.removeContract(uuid);
            }
        });  
    }
}
