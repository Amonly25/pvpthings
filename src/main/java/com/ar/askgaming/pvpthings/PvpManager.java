package com.ar.askgaming.pvpthings;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.AttributeTrait;

public class PvpManager {

    private static HashMap<Player, PvpPlayer> pvpPlayers;
    
    private HashMap<Player, NPC> npcPlayerLink;
    
    public HashMap<Player, NPC> getNpcPlayerLink() {
        return npcPlayerLink;
    }

    private PvpThings plugin;

    public PvpManager(PvpThings plugin) {
        this.plugin = plugin;

        npcPlayerLink = new LinkedHashMap<>();
        pvpPlayers = new LinkedHashMap<>();
    }

    public static PvpPlayer getPvpPlayer(Player p){
        return pvpPlayers.getOrDefault(p, null);
    }

    public PvpPlayer loadOrCreatePvpPlayer(Player p) {
        if (pvpPlayers.containsKey(p)) {
            return pvpPlayers.get(p);
        }
        PvpPlayer pvp = new PvpPlayer(p);
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
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, p.getInventory().getItemInMainHand());
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
        npc.data().setPersistent(NPC.Metadata.DROPS_ITEMS,false);

        p.teleport(npc.getEntity().getLocation());
        p.getInventory().setContents(npc.getOrAddTrait(net.citizensnpcs.api.trait.trait.Inventory.class).getContents());
        p.getInventory().setItemInMainHand(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HAND));
        p.getInventory().setItemInOffHand(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.OFF_HAND));
        p.getInventory().setHelmet(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HELMET));
        p.getInventory().setChestplate(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.CHESTPLATE));
        p.getInventory().setLeggings(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.LEGGINGS));
        p.getInventory().setBoots(npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.BOOTS));

        npc.destroy();
    }
}
