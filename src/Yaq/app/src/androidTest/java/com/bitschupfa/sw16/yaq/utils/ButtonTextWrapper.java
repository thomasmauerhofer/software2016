package com.bitschupfa.sw16.yaq.utils;

import android.widget.TextView;

public class ButtonTextWrapper {
    TextView buttonText;
    int clicked;

    public ButtonTextWrapper(TextView buttonText) {
        this.buttonText = buttonText;
        clicked = 0;
    }

    public boolean wasClicked() {
        return clicked > 0 ? true : false;
    }

    public void click() {
        clicked++;
    }

    public TextView getButtonTextField() {
        return buttonText;
    }
}
