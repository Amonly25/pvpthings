package com.ar.askgaming.pvpthings.DataBase;

import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpThings;

public class Language {

    private String defaultLang;
    private File defaultFile;
    private HashMap<String, HashMap<String, String>> cache = new HashMap<>();
    
    private final PvpThings plugin;
    public Language(PvpThings plugin){
        this.plugin = plugin;

        createFile("es");
        createFile("en");    
    }

    private void createFile(String locale) {
        File file = new File(plugin.getDataFolder() + "/lang/" + locale + ".yml");
        if (!file.exists()) {
            plugin.saveResource("lang/" + locale + ".yml", false);
        }
    }

    public void load() {
        defaultLang = plugin.getConfig().getString("default_lang", "en");
        defaultFile = new File(plugin.getDataFolder() + "/lang/" + defaultLang + ".yml");
        if (!defaultFile.exists()) {
            plugin.getLogger().warning("Default language file not found, using English as default.");
            defaultLang = "en";
            defaultFile = new File(plugin.getDataFolder() + "/lang/en.yml");
        }
        cache.clear();
    }

    public String get(String path, CommandSender sender) {
        Player p = (sender instanceof Player) ? (Player) sender : null;

        String locale = (p == null) ? defaultLang : p.getLocale().split("_")[0];
    
        // Si el idioma no existe, usar inglés como fallback inmediato
        File file = new File(plugin.getDataFolder() + "/lang/" + locale + ".yml");
        if (!file.exists()) {
            plugin.getLogger().warning("Language file not found for " + locale + ", using English as fallback.");
            locale = defaultLang;
            file = defaultFile;
        }
    
        // Verificar caché
        if (cache.containsKey(locale) && cache.get(locale).containsKey(path)) {
            return ChatColor.translateAlternateColorCodes('&', cache.get(locale).get(path));
        }
    
        // Cargar mensaje desde archivo
        String required = loadMessage(file, path);
    
        // Si el mensaje no se encuentra en el archivo de idioma, obtener el de inglés
        if (required.startsWith("Error:")) {
            required = loadMessage(defaultFile, path);
        }
    
        // Guardar en caché
        cache.computeIfAbsent(locale, k -> new HashMap<>()).put(path, required);
        return ChatColor.translateAlternateColorCodes('&', required);
    }
    
    private String loadMessage(File file, String path) {
        FileConfiguration langFile = YamlConfiguration.loadConfiguration(file);
    
        if (langFile.isList(path)) {
            return String.join("\n", langFile.getStringList(path));
        }
    
        return langFile.getString(path, "Error: Invalid lang path: " + path);  
    } 
}