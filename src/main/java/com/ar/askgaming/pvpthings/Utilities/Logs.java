package com.ar.askgaming.pvpthings.Utilities;

import java.io.File;
import java.time.LocalDateTime;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.ar.askgaming.pvpthings.PvpThings;

public class Logs implements Listener{
    
    private PvpThings plugin;
    private boolean enabled;
    private File logFile;
    private FileConfiguration config;

    public Logs(PvpThings plugin) {
        this.plugin = plugin;
        enabled = plugin.getConfig().getBoolean("logs.enable", true);

        logFile = new File(plugin.getDataFolder(), "logs.yml");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        config = new YamlConfiguration();

        try {
            config.load(logFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!enabled) {
            return;
        }
        Player p = e.getEntity();
        String death = p.getName();

        String killer = "Unknown";
        if (p.getKiller() != null) {
            killer = p.getKiller().getName();
        }
        String cause = "Unknown";
		if (p.getLastDamageCause() != null) {
			cause = p.getLastDamageCause().getCause().toString();
		}
        ItemStack[] contents = p.getInventory().getContents();

        Location location = p.getLocation();

        log(death, killer, cause, contents,location);
        
    }

    public void log(String playerName, String killer, String cause, ItemStack[] contents, Location location) {

        LocalDateTime now = LocalDateTime.now();    
        FileConfiguration cfg = plugin.getConfig();
        
        config.set(now.toString() + ".player", playerName);
        config.set(now.toString() + ".killer", killer);
        config.set(now.toString() + ".location", "World: " + location.getWorld().getName() + " X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ());

        if (cfg.getBoolean("logs.cause")){
            config.set(now.toString() + ".cause", cause);
        }
        if (cfg.getBoolean("logs.inventory")){
            config.set(now.toString() + ".inventory_contents", contents);
        }

        try {
            config.save(logFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
