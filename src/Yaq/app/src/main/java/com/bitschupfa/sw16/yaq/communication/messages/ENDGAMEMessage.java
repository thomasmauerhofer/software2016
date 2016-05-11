package com.bitschupfa.sw16.yaq.communication.messages;


import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.ui.RankingItem;

import java.util.ArrayList;

public class ENDGAMEMessage extends Message {
    private static final String TAG = "ENDGAMEMessage";
    private ArrayList<RankingItem> scoreList;

    public ENDGAMEMessage(@NonNull ArrayList<RankingItem> scoreList) {
        super();
        this.scoreList = scoreList;
    }

    public ENDGAMEMessage(@NonNull String address, @NonNull ArrayList<RankingItem> scoreList) {
        super(address);
        this.scoreList = scoreList;
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.endGame(scoreList);
    }

    @Override
    public void action(ClientMessageHandler handler) {
        // noting to do here
    }

    @Override
    public String toString() {
        return String.format("End game message from host (%s)", getSenderAddress());
    }
}
