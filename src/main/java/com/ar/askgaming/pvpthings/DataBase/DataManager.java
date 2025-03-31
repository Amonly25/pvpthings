package com.ar.askgaming.pvpthings.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.ar.askgaming.pvpthings.PvpPlayer;
import com.ar.askgaming.pvpthings.PvpThings;

public class DataManager {

    private final PvpThings plugin;
    private HikariSource hikariSource;

    private final HashMap<UUID, PvpPlayer> pvpPlayers = new HashMap<>();

    public DataManager() {
        this.plugin = PvpThings.getInstance();

        hikariSource = new HikariSource();

        for (Player pl : Bukkit.getOnlinePlayers()) {
            loadOrCreatePvpPlayer(pl.getUniqueId(), pvpPlayer -> {
                if (pvpPlayer != null) {
                    Bukkit.getLogger().info("Jugador cargado: " + pl.getName());
                } else {
                    Bukkit.getLogger().warning("Error al cargar jugador: " + pl.getName());
                }
            });
        }
    }
    public PvpPlayer getPvpPlayer(UUID uuid) {
        return pvpPlayers.get(uuid);
    }

    public void loadOrCreatePvpPlayer(UUID uuid, Consumer<PvpPlayer> callback) {
        if (pvpPlayers.containsKey(uuid)) {
            callback.accept(pvpPlayers.get(uuid)); // Devuelve el jugador ya cargado
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection conn = hikariSource.getConnection()) {
                PreparedStatement statement = conn.prepareStatement("SELECT * FROM players WHERE uuid = ?");
                statement.setString(1, uuid.toString());

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    PvpPlayer pvpPlayer = new PvpPlayer(
                        uuid,
                        resultSet.getInt("kills"),
                        resultSet.getInt("deaths"),
                        resultSet.getInt("killstreak"),
                        resultSet.getInt("highestkillstreak"),
                        resultSet.getInt("lastdeathtime")
                    );

                    pvpPlayers.put(uuid, pvpPlayer);

                    // Llama al callback en el main thread
                    Bukkit.getScheduler().runTask(plugin, () -> callback.accept(pvpPlayer));
                } else {
                    // Crea el jugador en la base de datos de forma asíncrona
                    createNewPvpPlayer(uuid, callback);
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Error loading player data: " + e.getMessage());
                createNewPvpPlayer(uuid, callback);
            }
        });
    }

    private void createNewPvpPlayer(UUID uuid, Consumer<PvpPlayer> callback) {
        PvpPlayer pvpPlayer = new PvpPlayer(uuid, 0, 0, 0, 0, 0);
        pvpPlayers.put(uuid, pvpPlayer);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection conn = hikariSource.getConnection()) {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO players (uuid, kills, deaths, killstreak, highestkillstreak, lastdeathtime) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, uuid.toString());
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                statement.setInt(5, 0);
                statement.setInt(6, 0);
                statement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error creating new player: " + e.getMessage());
            }

            // Devuelve el nuevo jugador en el main thread
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(pvpPlayer));
        });
    }

    public HashMap<UUID, PvpPlayer> getPvpPlayers() {
        return pvpPlayers;
    }

    public HikariSource getHikariSource() {
        return hikariSource;
    }
    public void savePlayerData(PvpPlayer pvpPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection conn = hikariSource.getConnection()) {
                PreparedStatement statement = conn.prepareStatement("UPDATE players SET kills = ?, deaths = ?, killstreak = ?, highestkillstreak = ?, lastdeathtime = ? WHERE uuid = ?");
                statement.setInt(1, pvpPlayer.getKills());
                statement.setInt(2, pvpPlayer.getDeaths());
                statement.setInt(3, pvpPlayer.getKillstreak());
                statement.setInt(4, pvpPlayer.getHighestKillstreak());
                statement.setInt(5, pvpPlayer.getTimeSinceDeath());
                statement.setString(6, pvpPlayer.getUuid().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error saving player data: " + e.getMessage());
            }
        });
    }

}
