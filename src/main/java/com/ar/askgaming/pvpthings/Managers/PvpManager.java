package com.ar.askgaming.pvpthings.Managers;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.AttributeTrait;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PvpManager extends BukkitRunnable{

    private HashMap<Player, PvpPlayer> pvpPlayers = new LinkedHashMap<>();
    
    public HashMap<Player, PvpPlayer> getPvpPlayers() {
        return pvpPlayers;
    }

    private HashMap<Player, NPC> npcPlayerLink = new LinkedHashMap<>();
    
    public HashMap<Player, NPC> getNpcPlayerLink() {
        return npcPlayerLink;
    }

    private PvpThings plugin;

    public PvpManager(PvpThings plugin) {
        this.plugin = plugin;

        File folder = new File(plugin.getDataFolder(), "/playerdata");

        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            loadOrCreatePvpPlayer(p);
        });
    }

    public PvpPlayer getPvpPlayer(Player p){
        return pvpPlayers.getOrDefault(p, loadOrCreatePvpPlayer(p));
    }

    public PvpPlayer loadOrCreatePvpPlayer(Player p) {
        if (pvpPlayers.containsKey(p)) {
            return pvpPlayers.get(p);
        }
        File file = new File(plugin.getDataFolder() + "/playerdata", p.getUniqueId() + ".yml");

        if (!file.exists()) {
            PvpPlayer pvp = new PvpPlayer(p);
            pvpPlayers.put(p, pvp);
            return pvp;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        PvpPlayer pvp = (PvpPlayer) config.get(p.getUniqueId().toString());
        pvp.setFile(file);
        pvp.setConfig(config);
        pvp.setPlayer(p);
        pvpPlayers.put(p, pvp);

        return pvp;

    }
    public boolean createNpcPlayerLink(Player p){
        try {
            NPCRegistry registry = CitizensAPI.getNPCRegistry();
            NPC npc = registry.createNPC(EntityType.PLAYER, p.getName());
    
            npc.spawn(p.getLocation());
            npc.setName(p.getName() + " (NPC)");
            //npc.getEntity().setGlowing(true);   

            npc.getOrAddTrait(net.citizensnpcs.api.trait.trait.Inventory.class).setContents(p.getInventory().getContents());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.OFF_HAND, p.getInventory().getItemInOffHand());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, p.getInventory().getHelmet());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, p.getInventory().getChestplate());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, p.getInventory().getLeggings());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, p.getInventory().getBoots());
            npc.getOrAddTrait(AttributeTrait.class).setAttributeValue(Attribute.MOVEMENT_SPEED, 0.75);
           
            npc.data().setPersistent(NPC.Metadata.DEFAULT_PROTECTED,false);
            npc.data().setPersistent(NPC.Metadata.DROPS_ITEMS,true);

            WanderGoal w = WanderGoal.builder(npc).build();
            Set<EntityType> targetTypes = Set.of(EntityType.PLAYER, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.ENDERMAN);
            TargetNearbyEntityGoal g = TargetNearbyEntityGoal.builder(npc).aggressive(true).radius(10).targets(targetTypes).build();

            npc.getDefaultGoalController().addGoal(w, 1);
            npc.getDefaultGoalController().addGoal(g, 1);

            npcPlayerLink.put(p, npc);
            return true;

        } catch (Exception e) {
            e.printStackTrace();    
            return false;
        }
    }

    public void switchFromNpc(Player p) {

        NPC npc = getNpcPlayerLink().get(p);
        if (npc == null) return;

        npc.data().setPersistent(NPC.Metadata.DROPS_ITEMS,false);

        p.teleport(npc.getEntity().getLocation());
        p.getInventory().setContents(npc.getOrAddTrait(net.citizensnpcs.api.trait.trait.Inventory.class).getContents());
        p.getInventory().setItemInOffHand(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.OFF_HAND));
        p.getInventory().setHelmet(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HELMET));
        p.getInventory().setChestplate(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.CHESTPLATE));
        p.getInventory().setLeggings(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.LEGGINGS));
        p.getInventory().setBoots(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.BOOTS));

        npc.destroy();
        npcPlayerLink.remove(p);
    }
    private HashMap<Player, Integer> lastCombat = new HashMap<>();

    public void setLastCombat(Player p, int time){
        PvpPlayer pvp = getPvpPlayer(p);
        pvp.setInCombat(true);
        if (!lastCombat.containsKey(p)){
            p.sendMessage(plugin.getLang().get("enter_combat",p));
        }
        lastCombat.put(p, time);
    }

    @Override
    public void run() {
        if (lastCombat.isEmpty()) return;

        Iterator<Entry<Player, Integer>> recorrer = lastCombat.entrySet().iterator();
        
        while (recorrer.hasNext()) {
            
            Entry<Player, Integer> entry = recorrer.next();
            
            Player player = entry.getKey();
            int time = entry.getValue();
            
            if (time >= 1) {
                String s = plugin.getLang().get("in_combat", player).replace("{time}", String.valueOf(time));
                sendActionBar(player, s);
                lastCombat.put(player, time - 1);
            }
            
            if (time == 0) {
                recorrer.remove();
                PvpPlayer pvp = getPvpPlayer(player);
                pvp.setInCombat(false);
                String s = plugin.getLang().get("leave_combat", player);
                player.sendMessage(s);
                sendActionBar(player, s);
            }
        }
    }
    private void sendActionBar(Player p, String message){
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(message));
    }
}
