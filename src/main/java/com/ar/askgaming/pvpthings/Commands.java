package com.ar.askgaming.pvpthings;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Attr;

public class Commands implements TabExecutor{

    private PvpThings plugin;
    public Commands(PvpThings plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of("spawn");
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
            case "spawn":
                spawnZombie(p,args);
                break;
            case "info":
                p.openInventory(plugin.getPvpInfo().getInventory());
                break;
            case "despawn":
                plugin.getDpsTest().remove();
                break;
            case "get_vel":
                getVel(p,args);
                break;
            case "set_vel":
                setVel(p, args);
                break;
            case "test":
                test(p,args);
                break;
            default:
                break;
        }
        return false;
    }
    public void spawnZombie(Player p, String[] args) {
        plugin.getDpsTest().spawn(p);
        p.sendMessage("Zombie spawned");
    }
    public void getVel(Player p, String[] args) {
        p.sendMessage("Actual Attack speed: " + p.getAttribute(Attribute.ATTACK_SPEED).getValue());
    }
    public void setVel(Player p, String[] args) {

        try {
            int amount = Integer.parseInt(args[1]);
            p.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(amount);
            p.sendMessage("Attack speed set to: " + amount);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void test(Player p, String[] args) {

    }

}
