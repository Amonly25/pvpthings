package com.ar.askgaming.pvpthings.Contracts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contract{

    private Integer prize;
    private UUID hunted;
    private List<String> contractors = new ArrayList<>();
    private Long createdTime;
    private String id;

    public Contract(Integer prize, UUID hunted, String creator) {
        this.prize = prize;
        this.hunted = hunted;
        this.contractors.add(creator);
        
        createdTime= System.currentTimeMillis();
        id = UUID.randomUUID().toString();

    }
    public Contract(String id, Integer prize, UUID hunted, String creator, long createdTime) {
        this.id = id;
        this.prize = prize;
        this.hunted = hunted;
        this.createdTime = createdTime;
        this.contractors.add(creator);
    }

    public Integer getPrize() {
        return prize;
    }

    public void setPrize(Integer price) {
        this.prize = price;
    }

    public UUID getHunted() {
        return hunted;
    }
    public boolean existContract(UUID hunted) {
        return this.hunted.equals(hunted);
    }

    public String getContractors() {
        StringBuilder sb = new StringBuilder();
        for (String creator : contractors) {
            sb.append(creator).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        return sb.toString();
    }
    public void addContractor(String creator) {
        this.contractors.add(creator);
    }

    public Long getCreatedTime() {
        return createdTime;
    }
    public String getId() {
        return id;
    }
}
