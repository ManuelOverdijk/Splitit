package nl.mprog.studentchoice10374582;

/**
 * Created by manuel on 05-12-14.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class NewGroupActivity extends MyActionBarActivity {

    private ValueEventListener connectedListener;
    private GroupListAdapter groupListAdapter;

    private Firebase ref;
    private static final String TAG = "SplitIt";

    private Button mButtonAdd;
    private Button mButtonDone;
    private EditText mEditTextTitle;
    private EditText mEditTextParticipant;

    private Map mParticipants;
    private String mTitle;
    private String mAdmin;
    private Group group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("NewGroup", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgroup);


        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        mToolbar.setTitle("Add a new group");

        group = new Group();
        mParticipants = new HashMap();

        /* Set Firebase reference for fetching grouplist */
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(super.firebaseUrl).child("groups");

        mButtonAdd = (Button) findViewById(R.id.buttonAdd);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addParticipant();
            }
        });

        mButtonDone = (Button) findViewById(R.id.buttonDone);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });

        ListView list = (ListView)this.findViewById(R.id.listViewParticipants);
        ParticipantsAdapter adapter = new ParticipantsAdapter(this,mParticipants);
        list.setAdapter(adapter);
    }

    private void done(){

        mAdmin = super.user.getUid();
        mEditTextTitle = (EditText) findViewById(R.id.editTextTitle);
        mTitle = mEditTextTitle.getText().toString();

        if (TextUtils.isEmpty(mTitle)) {
            mTitle = "Sample Group";
        }

        mParticipants.put(mAdmin,"true");


        // Create new group
        group.setAdmin(mAdmin);
        group.setTitle(mTitle);
        group.setParticipants(mParticipants);

        // Push new Group to Firebase
        ref.push().setValue(group);

        // Send user back to GroupView

        Intent intent = new Intent(getApplicationContext(),GroupActivity.class);
        startActivity(intent);
    }

    private void addParticipant() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add participant");

        final EditText input = new EditText(this);
        input.setHint("Use simplelogin:1 for testing");
        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // parse data from EditText.

                String title = input.getText().toString();
                if (!TextUtils.isEmpty(title)) {

                    if(mParticipants.containsKey(title)){
                        Log.e("Key","Contains key!");
                    } else {
                        Log.e("Key","Loggin key");
                        mParticipants.put(title,"true");

                    }

                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });

        alert.show();
    }


    @Override
    public void onStart(){
        super.onStart();


        /* Firebase connectivity status */
        connectedListener = ref.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean)dataSnapshot.getValue();
                if (connected) {
                    Log.d(TAG, "Connected to Firebase");
                } else {
                    Log.d(TAG,"Disconnected from Firebase");
                }
            }
            @Override
            public void onCancelled(FirebaseError error) {
                // No-op
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class ParticipantsAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Map participants;

        public ParticipantsAdapter(Context ctx, Map part){
            mInflater = LayoutInflater.from(ctx);
            participants = part;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return participants.size();
        }

        @Override
        public String getItem(int arg0) {
            int count = 0;

            for (Object entry : participants.entrySet())
            {
                if(arg0 == count){
                    return entry.toString();
                }
                count++;

            }
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = mInflater.inflate(R.layout.participant_item, null);

                Log.e("TAG","Position: "+position);
                Log.e("TAG",participants.toString());
//                Log.e("TAG","contains: " + participants.containsKey(0));
//                Log.e("TAG",participants.get(0).toString());

                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(getItem(position));
            }

            return convertView;
        }
    }

}
