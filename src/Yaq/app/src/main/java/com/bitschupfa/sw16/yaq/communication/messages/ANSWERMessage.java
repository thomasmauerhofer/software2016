package com.bitschupfa.sw16.yaq.communication.messages;


import android.support.annotation.NonNull;
import android.util.Log;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.database.object.Answer;


public class ANSWERMessage extends Message {
    private static final String TAG = "ANSWERMessage";
    private Answer answer;

    public ANSWERMessage(@NonNull Answer answer) {
        super();
        setAnswer(answer);
    }

    public void setAnswer(Answer answer) {
        if (answer == null) {
            throw new IllegalArgumentException("Answer may not be null!");
        }
        this.answer = answer;
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.showCorrectAnswer(answer);
    }

    @Override
    public void action(ClientMessageHandler handler) {
        Log.d(TAG, this.toString());
        handler.handleAnswer(getSenderAddress(), answer);
    }

    @Override
    public String toString() {
        return String.format("Received Answer message from %s", getSenderAddress());
    }
}
