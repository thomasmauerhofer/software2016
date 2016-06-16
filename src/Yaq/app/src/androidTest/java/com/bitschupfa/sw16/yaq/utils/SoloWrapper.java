package com.bitschupfa.sw16.yaq.utils;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.Button;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

public class SoloWrapper extends Solo {

    public List<ButtonWrapper> buttonsOfActivity = new ArrayList<>();

    public SoloWrapper(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void addButton(Button button) {
        buttonsOfActivity.add(new ButtonWrapper(button));
    }

    public ButtonWrapper getButtonWrapper(String buttonText) throws Exception {
        for(ButtonWrapper button : buttonsOfActivity) {
            if(button.getButton().getText().equals(buttonText)) {
                return button;
            }
        }
        throw new Exception("No Button found!");
    }

    @Override
    public void clickOnButton(String text) {
        super.clickOnButton(text);
        clickOnAnswerButton(text);
    }

    private boolean clickOnAnswerButton(String buttonText) {
        try {
            getButtonWrapper(buttonText).click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
