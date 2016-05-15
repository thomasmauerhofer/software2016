package com.bitschupfa.sw16.yaq.communication.messages;

import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;

public class CLIENTQUITMessage extends Message {
    private static final String TAG = "CLIENTQUITMessage";

    private String id;

    public CLIENTQUITMessage(@NonNull String id) {
        super();
        this.id = id;
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.quit();
    }

    @Override
    public void action(ClientMessageHandler handler) {
        handler.clientQuits(id);
    }
}
