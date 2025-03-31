package com.ar.askgaming.pvpthings.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.ar.askgaming.pvpthings.PvpThings;

public class Recipes {

    private final PvpThings plugin;
    public Recipes(PvpThings plugin) {
        this.plugin = plugin;

        add();
    }

    public void add(){

        if (plugin.getConfig().getBoolean("utilities.enchanted_golden_apple_recipe")) {

            ItemStack enchantedGoldenApple =  new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
            addIfNotExists("enchanted_golden_apple", enchantedGoldenApple , "AAA", "AGA", "AAA", 
                                'A', Material.GOLD_BLOCK, 'G', Material.APPLE);

        } else {
            removeIfExists("enchanted_golden_apple");
        }
    }

    private void addIfNotExists(String key, ItemStack result,  String row1, String row2, String row3, char ingredient1Char, Material ingredient1Material,  char ingredient2Char, Material ingredient2Material) {
       
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        
        if (Bukkit.getRecipe(namespacedKey) == null) {
            ShapedRecipe recipe = new ShapedRecipe(namespacedKey, result);
            recipe.shape(row1, row2, row3);
            recipe.setIngredient(ingredient1Char, ingredient1Material);
            recipe.setIngredient(ingredient2Char, ingredient2Material);
            Bukkit.addRecipe(recipe);
        }
    }
    private void removeIfExists(String key) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        Recipe recipe = Bukkit.getRecipe(namespacedKey);
        
        if (recipe != null) {
            Bukkit.removeRecipe(namespacedKey);  // Elimina la receta si existe
        }
    }

}
