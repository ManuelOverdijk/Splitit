package nl.mprog.studentchoice10374582;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GroupActivity extends MyActionBarActivity {

    private ValueEventListener connectedListener;
    private GroupListAdapter groupListAdapter;

    private Firebase ref;
    private static final String TAG = "SplitIt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("GroupActivity","onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(getResources().getString(R.string.subtitle_activity_group));

        /* Set Firebase reference for fetching grouplist */
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(super.firebaseUrl).child("groups");
    }

    @Override
    public void onStart(){
        super.onStart();

        /* Fetch GroupList and fill the listView */
        final ListView listView = (ListView)findViewById(R.id.list);

        groupListAdapter = new GroupListAdapter(ref.limit(20), this, R.layout.group_view, super.user.getUid());
        listView.setAdapter(groupListAdapter);
        groupListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(groupListAdapter.getCount() - 1);
            }
        });

        /* Handle clicks on ListView's items and start new chatActivity */
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView adminText = (TextView)view.findViewById(R.id.admin);
                String groupName = (String) adminText.getTag();

                Intent intent = new Intent().setClass(getApplicationContext(),chatActivity.class);

                intent.putExtra("groupId", groupName);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),groupName, Toast.LENGTH_SHORT).show();
            }
        });

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
}
