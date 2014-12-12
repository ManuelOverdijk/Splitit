package nl.mprog.studentchoice10374582;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import nl.mprog.studentchoice10374582.objectData.User;

/**
 * Created by manuel on 14-11-14.
 */
public class MyActionBarActivity extends ActionBarActivity {

    public ObjectPreference objectPreference;

    public User user;
    private Boolean loggedin;

    String firebaseUrl;
    private Firebase ref;
    private AuthData authData;

    private GoogleApiClient mGoogleApiClient;
    private boolean mGoogleIntentInProgress;
    private boolean mGoogleLoginClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectPreference = (ObjectPreference) this.getApplication();
        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();

        firebaseUrl = getResources().getString(R.string.firebase_url);
        Firebase.setAndroidContext(this);
        ref = new Firebase(firebaseUrl);
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    loggedin = true;
                } else {
                    loggedin = false;
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        user = complexPreferences.getObject("user", User.class);
        authData = user.getAuthData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
            if (isLoggedIn()) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean isLoggedIn(){
        return this.loggedin;
    }

    private void logout() {
        if (this.authData != null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Plus.API).build();

            /* logout of Firebase */
            ref.unauth();

            if (this.authData.getProvider().equals("facebook")) {
                /* Logout from Facebook */
                Session session = Session.getActiveSession();
                if (session != null) {
                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                    }
                } else {
                    session = new Session(getApplicationContext());
                    Session.setActiveSession(session);
                    session.closeAndClearTokenInformation();
                }
            } else if (this.authData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }

            /* Update authenticated user and show login buttons */
            this.authData = null;
            this.user = null;
            this.loggedin = false;

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        }
    }
}
