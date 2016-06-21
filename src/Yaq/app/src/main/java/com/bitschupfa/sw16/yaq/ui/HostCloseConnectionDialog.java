package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.bluetooth.ConnectionListener;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.utils.CastHelper;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class HostCloseConnectionDialog extends DialogFragment {

    private static final String TAG = "HostCloseConnectionDl";
    private Activity activity = null;
    private CastHelper castHelper = null;
    private ServerSocket fakeHost = null;
    private ConnectionListener btConnectionListener = null;

    public HostCloseConnectionDialog(Activity activity, CastHelper castHelper, ServerSocket fakeHost,
                                     ConnectionListener btConnectionListener) {
        this.activity = activity;
        this.castHelper = castHelper;
        this.fakeHost = fakeHost;
        this.btConnectionListener = btConnectionListener;
    }

    public HostCloseConnectionDialog(Activity activity, CastHelper castHelper) {
        this.activity = activity;
        this.castHelper = castHelper;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.shouldConnectionBeClosed)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.onBackPressed();

                        try {
                            QuizFactory.instance().clearQuiz();
                            QuizFactory.instance().setNumberOfQuestions(10);
                            HostGameLogic.getInstance().quit(getResources().getString(R.string.connectionClosedByHost));
                            castHelper.teardown(false);

                            if(fakeHost != null)
                                fakeHost.close();

                            if(btConnectionListener != null)
                                btConnectionListener.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error while BackButton pressed!");
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
