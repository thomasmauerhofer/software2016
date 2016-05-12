package com.bitschupfa.sw16.yaq.communication.messages;

import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;


public class QUESTIONMessage extends Message {
    private static final String TAG = "QUESTIONMessage";
    private TextQuestion question;
    private int timeout;

    public QUESTIONMessage(@NonNull TextQuestion question, int timeout) {
        super();
        setQuestion(question);
        setTimeout(timeout);
    }

    public void setQuestion(TextQuestion question) {
        this.question = question;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.showNextQuestion(question, timeout);
    }

    @Override
    public void action(ClientMessageHandler handler) {
    }
}