package com.ar.askgaming.pvpthings;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.npc.NPC;

public class PvpManager {

    private List<String> pvpPlayers;
    private List<NPC> npcs;
    
    public List<String> getPvpPlayers() {
        return pvpPlayers;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }

    private PvpThings plugin;

    public PvpManager(PvpThings plugin) {
        this.plugin = plugin;

        pvpPlayers = new ArrayList<>();
        npcs = new ArrayList<>();
    }
    

}
