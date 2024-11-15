package com.ar.askgaming.pvpthings.Utilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ar.askgaming.pvpthings.PvpThings;

public class PvpInfo {

    private Inventory inventory;

    private PvpThings plugin;
    public PvpInfo(PvpThings plugin) {
        this.plugin = plugin;

        inventory = Bukkit.createInventory(null, 27, "Pvp Info");

        List<String> cooldownLore = List.of("El pvp es similiar a la 1.8",
        "Tiene cooldown indetectable, pero suficiente para impedir autoclickers.",
        "Puedes probar el daño por segundo en el spawn.");
        addItem(Material.DIAMOND_SWORD, 9, "Cooldown Pvp", cooldownLore);

        List<String> dpsLore = List.of("El acha posee una velocidad menor a la espada", 
        "Espadas, hachas y tridente hacen un daño similar.",
        "Prueba el daño por segundo en el spawn.");
        addItem(Material.NETHERITE_AXE, 11, "Daño", dpsLore);

        List<String> appleLore = List.of("Puedes craftear manzanas doradas con bloques","Pero los efectos son los usuales de las ultimas versiones.");
        addItem(Material.ENCHANTED_GOLDEN_APPLE, 13, "Manzanas dorada", appleLore);

        List<String> otherLore = List.of("Para ver opciones como contratos a cazadores usa /pvp help",
        "No existen otras modificiones en el pvp.",
        "Puedes contactar a soporte si tienes alguna duda o ideas para mejorar la experiencia");
        addItem(Material.BARRIER, 15, "Manzanas dorada", otherLore);
    }
    public Inventory getInventory() {
        return inventory;
    }

    private void addItem(Material type, int slot, String name, List<String> lore) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);

    }
}
