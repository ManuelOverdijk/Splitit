/** Native App Studio
 *
 * SplitIt - Student's choice
 *
 * Author: Manuel Overdijk
 *         manuel.overdijk@gmail.com
 *         Uva id: 10374582
 *
 * December 2014
 */

package nl.mprog.studentchoice10374582;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;

import nl.mprog.studentchoice10374582.fragments.GroupBillFragment;
import nl.mprog.studentchoice10374582.fragments.GroupChatFragment;
import nl.mprog.studentchoice10374582.fragments.GroupInfoFragment;
import nl.mprog.studentchoice10374582.helpers.ComplexPreferences;
import nl.mprog.studentchoice10374582.helpers.ObjectPreference;
import nl.mprog.studentchoice10374582.objectData.User;

public class MainActivity extends FragmentActivity {

    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xff003768;

    private String groupId;

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
        setContentView(R.layout.activity_main3);

        Firebase.setAndroidContext(getApplicationContext());

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        /* Set SlidingTab color */
        changeColor(currentColor);

        objectPreference = (ObjectPreference) this.getApplication();
        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();

        firebaseUrl = getResources().getString(R.string.firebase_url);

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

        /* Customize our toolbar */
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Split it - "  + user.getName());
    }

    @Override
    public void onResume(){
        super.onResume();

        /* Get GroupId for fetching chat messages */
        Intent intent = getIntent();
        if(intent!=null){
            this.groupId = intent.getStringExtra("groupId");

        } else {
            Log.e("chatActivity","Error - Intent is null!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void changeColor(int newColor) {
        tabs.setIndicatorColor(newColor);
        currentColor = newColor;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
        changeColor(currentColor);
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Info", "Chat", "Bills"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("TAG", Integer.toString(position));
            switch (position) {
                case 0:
                    return GroupInfoFragment.newInstance(groupId);
                case 1:
                    return GroupChatFragment.newInstance(groupId);
                case 2:
                    return GroupBillFragment.newInstance(groupId);
                default:
                    return null;
            }
        }

    }

}