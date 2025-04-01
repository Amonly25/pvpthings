package com.ar.askgaming.pvpthings.Contracts;

import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Contract implements ConfigurationSerializable {

    private Double prize;
    private UUID hunted;
    private String creator;
    private Long createdTime;
    private String id;

    public Contract(double prize, UUID hunted, String creator) {
        this.prize = prize;
        this.hunted = hunted;
        this.creator = creator;
        
        createdTime= System.currentTimeMillis();
        id = UUID.randomUUID().toString();

    }

    public Contract(Map<String, Object> map) {
        this.prize = (double) map.get("price");
        this.hunted = UUID.fromString((String) map.get("hunted"));
        this.creator = (String) map.get("creator");
        this.createdTime = (long) map.get("time");
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
            "price", prize,
            "hunted", hunted.toString(),
            "hunter", creator,
            "time", createdTime
        );
    }
    public double getPrize() {
        return prize;
    }

    public void setPrize(double price) {
        this.prize = price;
    }

    public UUID getHunted() {
        return hunted;
    }

    public String getCreator() {
        return creator;
    }

    public Long getCreatedTime() {
        return createdTime;
    }
    public String getId() {
        return id;
    }
}
