package com.bitschupfa.sw16.yaq.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;

/**
 * Created by thomas on 12.04.16.
 */
public class FindDevicesDialog extends DialogFragment {

    public static final String DIALOG_FRAGMENT_TAG = "dialog_find_device";

    private Dialog findDevicesDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_find_device, null);

        //text = (TextView) dialogView.findViewById(R.id.text);

        findDevicesDialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setTitle(R.string.dialog_find_device_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();

        findDevicesDialog.setCanceledOnTouchOutside(true);
        findDevicesDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findDevicesDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        findDevicesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleOkButtonClick();
                    }
                });
            }
        });
        return findDevicesDialog;
    }

    protected void handleOkButtonClick() {
    }

}
