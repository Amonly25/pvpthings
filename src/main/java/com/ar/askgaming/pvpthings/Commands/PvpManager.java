package com.ar.askgaming.pvpthings.Commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PvpManager extends BukkitRunnable{

    private final HashMap<Player, Integer> lastCombat = new HashMap<>();

    private final PvpThings plugin;

    public PvpManager(PvpThings plugin) {
        this.plugin = plugin;

        runTaskTimer(plugin, 20L, 20L);

    }

    public void setLastCombat(Player p, int time){
        PvpPlayer pvpPlayer = plugin.getDataManager().getPvpPlayer(p.getUniqueId());
        if (pvpPlayer == null) return;

        pvpPlayer.setInCombat(true);
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
                plugin.getDataManager().loadOrCreatePvpPlayer(player.getUniqueId(), pvpPlayer -> {
                    if (pvpPlayer != null) {
                        pvpPlayer.setInCombat(false);
                        String s = plugin.getLang().get("leave_combat", player);
                        player.sendMessage(s);
                        sendActionBar(player, s);
                    }
                });
            }
        }
    }
    private void sendActionBar(Player p, String message){
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(message));
    }

}
