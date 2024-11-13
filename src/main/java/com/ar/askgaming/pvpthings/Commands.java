package com.ar.askgaming.pvpthings;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.Attr;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.ai.goals.WanderGoal;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.AttributeTrait;
import net.citizensnpcs.trait.TargetableTrait;

public class Commands implements TabExecutor{

    private PvpThings plugin;
    public Commands(PvpThings plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of("");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length == 0) {
            sender.sendMessage("PvP Things Plugin");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command");
            return false;
        }
        Player p = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "test":
                test(p,args);
                break;
        
            default:
                break;
        }
        return false;
    }
    public void test(Player p, String[] args) {
        createCustomVillager(p.getLocation(), p);
    }
    private void createCustomVillager(Location location, Player p) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, "pvplogger");

        npc.spawn(location);
        npc.setName(p.getName());
        npc.getEntity().setGlowing(true);   
        npc.getOrAddTrait(net.citizensnpcs.api.trait.trait.Inventory.class).setContents(p.getInventory().getContents());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, p.getInventory().getItemInMainHand());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.OFF_HAND, p.getInventory().getItemInOffHand());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, p.getInventory().getHelmet());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, p.getInventory().getChestplate());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, p.getInventory().getLeggings());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, p.getInventory().getBoots());
        npc.data().setPersistent(NPC.Metadata.DEFAULT_PROTECTED,false);
        npc.data().setPersistent(NPC.Metadata.DROPS_ITEMS,true);
        npc.getOrAddTrait(TargetableTrait.class).addTargeter(p.getUniqueId());
        plugin.getPvpManager().getNpcs().add(npc);
        npc.getOrAddTrait(AttributeTrait.class).setAttributeValue(Attribute.MOVEMENT_SPEED, 0.75);
        
        WanderGoal w = WanderGoal.builder(npc).build();
        Set<EntityType> targetTypes = Set.of(EntityType.PLAYER, EntityType.ZOMBIE, EntityType.SKELETON);
        TargetNearbyEntityGoal g = TargetNearbyEntityGoal.builder(npc).aggressive(true).radius(10).targets(targetTypes).build();
        npc.getDefaultGoalController().addGoal(w, 1);
        npc.getDefaultGoalController().addGoal(g,1);
    }
}
