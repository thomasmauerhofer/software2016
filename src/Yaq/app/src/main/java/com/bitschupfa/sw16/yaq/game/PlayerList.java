package com.bitschupfa.sw16.yaq.game;

import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.bitschupfa.sw16.yaq.utils.MapUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PlayerList {
    private static final int MAX_PLAYER = 8;
    private final Map<String, Player> players = new HashMap<>(MAX_PLAYER);
    private final Map<String, ConnectedDevice> connectedDevices = new HashMap<>();


    public PlayerList() {
    }

    public void registerConnectedDevice(String id, ConnectedDevice device) {
        connectedDevices.put(id, device);
    }

    public ConnectedDevice unregisterConnectedDevice(String id) {
        ConnectedDevice device = connectedDevices.get(id);
        connectedDevices.remove(id);
        return device;
    }

    public void addPlayer(String id, PlayerProfile profile) throws IllegalStateException {
        if (players.size() == MAX_PLAYER) {
            throw new IllegalStateException("Max number of players already reached.");
        }

        ConnectedDevice device = unregisterConnectedDevice(id);
        if (device == null) {
            throw new IllegalStateException("No device for player available.");
        }

        players.put(id, new Player(device, profile));
    }

    public void removePlayer(String id) {
        players.remove(id);
    }

    public Player getPlayer(String id) {
        return players.get(id);
    }

    public Set<String> getPlayerIds() {
        return players.keySet();
    }

    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players.values()) {
            playerNames.add(player.getProfile().getPlayerName());
        }
        return playerNames;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void resetScores() {
        for (Player player : players.values()) {
            player.resetScore();
        }
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public ArrayList<RankingItem> getSortedScoreList() {
        ArrayList<RankingItem> scoreboard = new ArrayList<>();
        for (Player player : MapUtil.sortByValueHighestFirst(players).values()) {
            scoreboard.add(new RankingItem(player.getProfile().getPlayerName(), player.getScore()));
        }
        return scoreboard;
    }

    public void clear() {
        players.clear();
    }
}
