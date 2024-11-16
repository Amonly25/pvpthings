package com.ar.askgaming.pvpthings.Commands;

import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class PvpCommand implements TabExecutor{

    private PvpThings plugin;
    public PvpCommand(PvpThings plugin) {
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
            case "spawn_zombie":
                spawnZombie(p,args);
                break;
            case "info":
                p.openInventory(plugin.getPvpInfo().getInventory());
                break;
            case "despawn_zombie":
                plugin.getDps().remove();
                break;
            case "back":
                back(p,args);
                break;
            case "get_vel":
                getVel(p,args);
                break;
            case "set_vel":
                setVel(p, args);
                break;
            default:
                break;
        }
        return false;
    }
    public void spawnZombie(Player p, String[] args) {
        plugin.getDps().spawn(p);
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
    public void back(Player p, String[] args) {
        PvpPlayer pvp = plugin.getPvpManager().loadOrCreatePvpPlayer(p);
        if (pvp.getLastDeathLocation() != null) {
            p.teleport(pvp.getLastDeathLocation());
        }
    }
}
