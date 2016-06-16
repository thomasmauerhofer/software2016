package com.bitschupfa.sw16.yaq.utils;

import android.widget.Button;

public class ButtonWrapper {
    Button button;
    int clicked;

    public ButtonWrapper(Button button) {
        this.button = button;
        clicked = 0;
    }

    public boolean wasClicked() {
        return clicked > 0 ? true : false;
    }

    public void click() {
        clicked ++;
    }

    public Button getButton() {
        return button;
    }
}
