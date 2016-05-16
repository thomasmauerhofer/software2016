package com.bitschupfa.sw16.yaq.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class CastHelper {
    private final static String TAG = CastHelper.class.getCanonicalName();

    public MediaRouter mMediaRouter;
    public MediaRouteSelector mMediaRouteSelector;
    public CastDevice mSelectedDevice;
    public MediaRouter.Callback mMediaRouterCallback;
    public GoogleApiClient mApiClient;

    public Cast.Listener mCastClientListener;
    public ConnectionCallbacks mConnectionCallbacks;
    public ConnectionFailedListener mConnectionFailedListener;

    public YaqGameChannel mYaqGameChannel;

    public boolean mWaitingForReconnect;
    public boolean mApplicationStarted;

    public enum GameState {WAITING, LOBBY, GAME, END};
    private GameState gameState;
    private TextQuestion questionToDisplay;
    private List<RankingItem> scoreboardToDisplay;

    private boolean callbacksActive;
    private Context context;
    private static CastHelper instance = null;

    public final String APPLICATION_ID = "5CC4A228";

    private CastHelper (Context c, GameState gameState) {
        this.context = c;
        setQuestionToDisplay(null);
        setScoreboardToDisplay(null);
        if(gameState != null) {
            setGameState(gameState);
        } else {
            setGameState(GameState.WAITING);
        }
        callbacksActive = false;

        mMediaRouter = MediaRouter.getInstance(this.context);
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(APPLICATION_ID))
                .build();
        mMediaRouterCallback = new YaqMediaRouterCallback();
    }

    public static CastHelper getInstance(Context c, GameState gameState) {
        if (instance == null) {
            instance = new CastHelper(c, gameState);
        } else {
            instance.setGameState(gameState);
        }

        if (gameState == GameState.LOBBY) {
            instance.setQuestionToDisplay(null);
            instance.setScoreboardToDisplay(null);
        }

        return instance;
    }
    
    public class YaqMediaRouterCallback extends MediaRouter.Callback {
        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            String routeId = info.getId();
            Log.d(TAG, "onRouteSelected: " + routeId);

            try {
                mCastClientListener = new Cast.Listener() {
                    @Override
                    public void onApplicationStatusChanged() {
                        if (mApiClient != null) {
                            Log.d(TAG, "onApplicationStatusChanged: "
                                    + Cast.CastApi.getApplicationStatus(mApiClient));
                        }
                    }

                    @Override
                    public void onVolumeChanged() {
                        if (mApiClient != null) {
                            Log.d(TAG, "onVolumeChanged: " + Cast.CastApi.getVolume(mApiClient));
                        }
                    }

                    @Override
                    public void onApplicationDisconnected(int errorCode) {
                        teardown(true);
                    }
                };
                mConnectionCallbacks = new ConnectionCallbacks();
                mConnectionFailedListener = new ConnectionFailedListener();

                Cast.CastOptions.Builder apiOptionsBuilder = new Cast.CastOptions
                        .Builder(mSelectedDevice, mCastClientListener);

                mApiClient = new GoogleApiClient.Builder(context)
                        .addApi(Cast.API, apiOptionsBuilder.build())
                        .addConnectionCallbacks(mConnectionCallbacks)
                        .addOnConnectionFailedListener(mConnectionFailedListener)
                        .build();

                mApiClient.connect();
            } catch (Exception e) {
                Log.e(TAG, "Failed launchReceiver", e);
            }
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            teardown(true);
            Log.d(TAG, "onRouteUnselected: ");
        }
    }

    private class ConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle connectionHint) {
            if (mWaitingForReconnect) {
                mWaitingForReconnect = false;
                // TODO: reconnect channels if needed (see HelloWorld Example)
            } else {
                try {
                    LaunchOptions launchOptions = new LaunchOptions.Builder().setRelaunchIfRunning(true).build();
                    Cast.CastApi.launchApplication(mApiClient, APPLICATION_ID, launchOptions)
                            .setResultCallback(
                                    new ResultCallback<Cast.ApplicationConnectionResult>() {
                                        @Override
                                        public void onResult(@NonNull Cast.ApplicationConnectionResult result) {
                                            Status status = result.getStatus();
                                            if (status.isSuccess()) {
                                                ApplicationMetadata applicationMetadata =
                                                        result.getApplicationMetadata();
                                                String sessionId = result.getSessionId();
                                                String applicationStatus = result.getApplicationStatus();
                                                boolean wasLaunched = result.getWasLaunched();

                                                Log.d(TAG, "application name: "
                                                        + applicationMetadata.getName()
                                                        + ", status: " + applicationStatus
                                                        + ", sessionId: " + sessionId
                                                        + ", wasLaunched: " + wasLaunched);

                                                mApplicationStarted = true;

                                                mYaqGameChannel = new YaqGameChannel();
                                                try {
                                                    Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                                            mYaqGameChannel.getNamespace(),
                                                            mYaqGameChannel);
                                                } catch (IOException e) {
                                                    Log.e(TAG, "Exception while creating channel", e);
                                                }
                                                if (getGameState() == GameState.WAITING) {
                                                    sendMessage(context.getString(R.string.waiting_for_gamestart));
                                                } else if (getGameState() == GameState.LOBBY) {
                                                    sendMessage(context.getString(R.string.waiting_for_players));
                                                } else if (getGameState() == GameState.GAME) {
                                                    if (getQuestionToDisplay() != null) {
                                                        sendTextQuestion(getQuestionToDisplay());
                                                    }
                                                } else if (getGameState() == GameState.END) {
                                                    if (getScoreboardToDisplay() != null) {
                                                        sendScoreboard(getScoreboardToDisplay());
                                                    }
                                                }
                                            } else {
                                                teardown(true);
                                            }
                                        }
                                    });

                } catch (Exception e) {
                    Log.e(TAG, "Failed to launch application", e);
                }
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            mWaitingForReconnect = true;
        }
    }

    private class ConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult result) {
            teardown(true);
        }
    }

    class YaqGameChannel implements Cast.MessageReceivedCallback {
        @SuppressWarnings("SameReturnValue")
        public String getNamespace() {
            return "urn:x-cast:com.bitschupfa.sw16.yaq";
        }

        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace,
                                      String message) {
            Log.d(TAG, "onMessageReceived: " + message);
        }
    }

    public void teardown(boolean selectDefaultRoute) {
        Log.d(TAG, "teardown");
        if (mApiClient != null) {
            if (mApplicationStarted) {
                if (mApiClient.isConnected() || mApiClient.isConnecting()) {
                    try {
                        if (mYaqGameChannel != null) {
                            Cast.CastApi.removeMessageReceivedCallbacks(
                                    mApiClient,
                                    mYaqGameChannel.getNamespace());
                            mYaqGameChannel = null;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception while removing channel", e);
                    }
                    mApiClient.disconnect();
                }
                mApplicationStarted = false;
            }
            mApiClient = null;
        }
        if (selectDefaultRoute) {
            mMediaRouter.selectRoute(mMediaRouter.getDefaultRoute());
        }
        mSelectedDevice = null;
        mWaitingForReconnect = false;
    }

    public void sendTextQuestion(TextQuestion question) {
        if (question == null) {
            question = getQuestionToDisplay();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", "question");
            json.put("question", question.getQuestion());
            json.put("answer_1", question.getAnswers().get(0).getAnswerString());
            json.put("answer_2", question.getAnswers().get(1).getAnswerString());
            json.put("answer_3", question.getAnswers().get(2).getAnswerString());
            json.put("answer_4", question.getAnswers().get(3).getAnswerString());
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        sendMessage(json.toString());
    }

    public void sendScoreboard(List<RankingItem> scoreboard) {
        if (scoreboard == null) {
            scoreboard = getScoreboardToDisplay();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("type", "scoreboard");
            JSONArray players = new JSONArray();
            for(RankingItem item : scoreboard) {
                JSONObject player = new JSONObject();
                player.put("name", item.getName());
                player.put("score", item.getScore());
                players.put(player);
            }
            json.put("players", players);
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        sendMessage(json.toString());
    }

    public void sendMessage(String message) {
        if (mApiClient != null && mYaqGameChannel != null) {
            try {
                Cast.CastApi.sendMessage(mApiClient, mYaqGameChannel.getNamespace(), message)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status result) {
                                        if (!result.isSuccess()) {
                                            Log.e(TAG, "Sending message failed");
                                        }
                                    }
                                });
            } catch (Exception e) {
                Log.e(TAG, "Exception while sending message", e);
            }
        }
    }

    public void removeCallbacks() {
        if(callbacksActive) {
            mMediaRouter.removeCallback(mMediaRouterCallback);
            callbacksActive = false;
        }
    }

    public void addCallbacks() {
        if(!callbacksActive) {
            mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                    MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
            callbacksActive = true;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public TextQuestion getQuestionToDisplay() {
        return questionToDisplay;
    }

    public void setQuestionToDisplay(TextQuestion questionToDisplay) {
        this.questionToDisplay = questionToDisplay;
    }

    public List<RankingItem> getScoreboardToDisplay() {
        return scoreboardToDisplay;
    }

    public void setScoreboardToDisplay(List<RankingItem> scoreboardToDisplay) {
        this.scoreboardToDisplay = scoreboardToDisplay;
    }
}