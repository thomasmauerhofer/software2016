package com.bitschupfa.sw16.yaq.utils;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

public class SoloWrapper extends Solo {

    public List<ButtonTextWrapper> answerTextFieldsOfActivity = new ArrayList<>();

    public SoloWrapper(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void addButton(TextView button) {
        answerTextFieldsOfActivity.add(new ButtonTextWrapper(button));
    }

    public ButtonTextWrapper getButtonWrapper(String buttonText) throws Exception {
        for (ButtonTextWrapper buttonTextField : answerTextFieldsOfActivity) {
            if (buttonTextField.getButtonTextField().getText().equals(buttonText)) {
                return buttonTextField;
            }
        }
        throw new Exception("No Button found!");
    }

    @Override
    public void clickOnText(String text) {
        super.clickOnText(text);
        clickOnAnswerButtonText(text);
    }

    private boolean clickOnAnswerButtonText(String buttonText) {
        try {
            getButtonWrapper(buttonText).click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
