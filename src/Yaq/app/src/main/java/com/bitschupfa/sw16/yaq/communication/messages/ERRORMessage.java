package com.bitschupfa.sw16.yaq.communication.messages;


import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.Errors;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;


public class ERRORMessage extends Message {
    private static final String TAG = "ERRORMessage";
    private String message;
    private Errors error;

    public ERRORMessage(@NonNull Errors errorType, @NonNull String errorMessage) {
        super();
        message = errorMessage;
        error = errorType;
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.handleError(error, message);
    }

    @Override
    public void action(ClientMessageHandler handler) {
        handler.handleError(error, message);
    }
}
