package com.bitschupfa.sw16.yaq.game;

import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.bitschupfa.sw16.yaq.utils.MapUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerList {

    private Map<String, Player> players = new HashMap<>();

    public PlayerList() {
    }

    public void addPlayer(String address, ConnectedDevice playerDevice) {
        players.put(address, new Player(playerDevice));
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
        for(Player player : players.values()) {
            player.resetScore();
        }
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public ArrayList<RankingItem> getSortedScoreList() {
        ArrayList<RankingItem> scoreboard = new ArrayList<>();
        for(Player player : MapUtil.sortByValueHighestFirst(players).values()) {
            scoreboard.add(new RankingItem(player.getProfile().getPlayerName(), player.getScore()));
        }
        return scoreboard;
    }

    public void clear() {
        players.clear();
    }
}
