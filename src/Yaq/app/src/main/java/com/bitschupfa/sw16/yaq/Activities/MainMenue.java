package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainMenue extends AppCompatActivity {

    private static final String TAG = MainMenue.class.getCanonicalName();

    private static MediaRouter mMediaRouter;
    private static MediaRouteSelector mMediaRouteSelector;
    private static CastDevice mSelectedDevice;
    private static MediaRouter.Callback mMediaRouterCallback;
    private static GoogleApiClient mApiClient;

    private static Cast.Listener mCastClientListener;
    private static ConnectionCallbacks mConnectionCallbacks;
    private static ConnectionFailedListener mConnectionFailedListener;

    private static YaqGameChannel mYaqGameChannel;

    private static boolean mWaitingForReconnect;
    private static boolean mApplicationStarted;

    private static final String APPLICATION_ID = "5CC4A228";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(APPLICATION_ID))
                        .build();
        mMediaRouterCallback = new MyMediaRouterCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menue, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onStop() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            sendMessage("Settings clicked");
            return true;
        } else if (id == R.id.menu_profile) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            sendMessage("Profile clicked");
            return true;
        } else if (id == R.id.menu_manage) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            JSONObject json = new JSONObject();
            try {
                json.put("type", "question");
                json.put("question", "What is longer than the rest?");
                json.put("answer_1", "2000 metres");
                json.put("answer_2", "2 kilometres");
                json.put("answer_3", "1.24274 miles");
                json.put("answer_4", "1 light-year");
            } catch (JSONException e) {
                Log.d(TAG, e.getMessage());
            }
            sendMessage(json.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("UnusedParameters")
    public void hostButtonClicked(View view)
    {
        Intent intent = new Intent(MainMenue.this, Host.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void joinButtonClicked(View view) {
        Intent intent = new Intent(MainMenue.this, Join.class);
        startActivity(intent);
    }

    private class MyMediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            String routeId = info.getId();
            Log.d(MainMenue.class.getCanonicalName(), "onRouteSelected: " + routeId);

            // launch the receiver application
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
                        //teardown();
                    }
                };
                mConnectionCallbacks = new ConnectionCallbacks();
                mConnectionFailedListener = new ConnectionFailedListener();

                Cast.CastOptions.Builder apiOptionsBuilder = new Cast.CastOptions
                        .Builder(mSelectedDevice, mCastClientListener);

                mApiClient = new GoogleApiClient.Builder(getApplicationContext())
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
            Log.d(MainMenue.class.getCanonicalName(), "onRouteUnselected: ");
        }
    }

    private class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {
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
                                            } else {
                                                teardown(true);
                                            }
                                        }
                                    });

                } catch (Exception e) {
                    Log.e(MainMenue.class.getCanonicalName(), "Failed to launch application", e);
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

    private void teardown(boolean selectDefaultRoute) {
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

    private void sendMessage(String message) {
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
}

