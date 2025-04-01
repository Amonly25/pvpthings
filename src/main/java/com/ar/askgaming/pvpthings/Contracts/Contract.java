package com.ar.askgaming.pvpthings.Contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contract{

    private Double prize;
    private UUID hunted;
    private List<String> creators = new ArrayList<>();
    private Long createdTime;
    private String id;

    public Contract(double prize, UUID hunted, String creator) {
        this.prize = prize;
        this.hunted = hunted;
        this.creators.add(creator);
        
        createdTime= System.currentTimeMillis();
        id = UUID.randomUUID().toString();

    }
    public Contract(String id, double prize, UUID hunted, String creator, long createdTime) {
        this.id = id;
        this.prize = prize;
        this.hunted = hunted;
        this.createdTime = createdTime;
        this.creators.add(creator);
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

    public String getCreators() {
        StringBuilder sb = new StringBuilder();
        for (String creator : creators) {
            sb.append(creator).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        return sb.toString();
    }
    public void addCreator(String creator) {
        this.creators.add(creator);
    }

    public Long getCreatedTime() {
        return createdTime;
    }
    public String getId() {
        return id;
    }
}
