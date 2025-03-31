package com.ar.askgaming.pvpthings.Contracts;

import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Contract implements ConfigurationSerializable {

    private Double price;
    private UUID hunted, hunter;
    private Long createdTime;
    private String id;

    public Contract(double price, UUID hunted, UUID hunter) {
        this.price = price;
        this.hunted = hunted;
        this.hunter = hunter;
        
        createdTime= System.currentTimeMillis();
        id = UUID.randomUUID().toString();

    }

    public Contract(Map<String, Object> map) {
        this.price = (double) map.get("price");
        this.hunted = UUID.fromString((String) map.get("hunted"));
        this.hunter = UUID.fromString((String) map.get("hunter"));
        this.createdTime = (long) map.get("time");
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
            "price", price,
            "hunted", hunted.toString(),
            "hunter", hunter.toString(),
            "time", createdTime
        );
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getHunted() {
        return hunted;
    }

    public void setHunted(UUID hunted) {
        this.hunted = hunted;
    }

    public UUID getHunter() {
        return hunter;
    }

    public void setHunter(UUID hunter) {
        this.hunter = hunter;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    

}
