package nl.mprog.studentchoice10374582;

import android.database.DataSetObserver;
import android.os.Bundle;
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

import androidapp.splitit.com.splitit.R;


public class GroupActivity extends MyListActivity {

    private ValueEventListener connectedListener;
    private GroupListAdapter groupListAdapter;

    private Firebase ref;
    private static final String TAG = "SplitIt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("GroupActivity","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);

        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(super.firebaseUrl).child("groups");
        Log.e("GroupActivity",ref.toString());

        android.util.Log.i("SplitIt", "REF = " + ref);

    }

    @Override
    public void onStart(){
        super.onStart();

        final ListView listView = getListView();

        groupListAdapter = new GroupListAdapter(ref.limit(20), this, R.layout.group_view, super.user.getUid());
        listView.setAdapter(groupListAdapter);
        groupListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(groupListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
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

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text or do whatever you need.
                TextView adminText = (TextView)view.findViewById(R.id.admin);
                String groupName = (String) adminText.getTag();
                Toast.makeText(getApplicationContext(),groupName, Toast.LENGTH_SHORT).show();
            }
        });


        //handle clicks on the listview
    }


//
//    private void sendMessage() {
//        EditText inputText = (EditText)findViewById(R.id.messageInput);
//        String input = inputText.getText().toString();
//        if (!input.equals("")) {
//            // Create our 'model', a Chat object
//            Chat chat = new Chat(input, super.user.getName());
//            // Create a new, auto-generated child of that chat location, and save our chat data there
//            ref.push().setValue(chat);
//            inputText.setText("");
//        }
//    }
}
