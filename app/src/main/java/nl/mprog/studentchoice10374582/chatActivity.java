package nl.mprog.studentchoice10374582;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class chatActivity extends MyActionBarActivity {

    private ValueEventListener connectedListener;
    private ChatListAdapter chatListAdapter;

    private String groupId;
    private Group group;
    private Firebase chatRef;
    private Firebase groupRef;

    private static final String TAG = "SplitIt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Firebase.setAndroidContext(getApplicationContext());

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();

        /* Get GroupId for fetching chat messages */
        Intent intent = getIntent();
        if(intent!=null){
            this.groupId = intent.getStringExtra("groupId");

            setChatRef(groupId);
            setGroupRef(groupId);

            // TODO: separate this is some sort of model

            /* Get the group data */
            groupRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    group = snapshot.getValue(Group.class);

                    ActionBar ab = getSupportActionBar();
                    ab.setTitle(group.getTitle());
                    ab.setSubtitle(group.getAdmin());

                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

            final ListView listView = (ListView)findViewById(R.id.list);
            //final ListView listView = getListView();

            // TODO: Determine best amount of chat messages to be fetched
            chatListAdapter = new ChatListAdapter(chatRef.limit(50), this, R.layout.chat_message,super.user.getName());
            listView.setAdapter(chatListAdapter);
            chatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(chatListAdapter.getCount() - 1);
                }
            });

            connectedListener = chatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {

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
                }
            });
        } else {
            Log.e("chatActivity","Error - Intent is null!");
        }
    }


    /* Handles creation of new chat messages and pushes them to Firebase */
    private void sendMessage() {
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        String input = inputText.getText().toString();

        if (!input.equals("")) {
            Chat chat = new Chat(input, super.user.getName());
            /* Save the new chat model to Firebase */
            chatRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    /* For setting the FirebaseRef, based on which group the Activity is currently in */
    private void setChatRef(String groupId){
        this.chatRef = new Firebase(super.firebaseUrl).child("chats").child(groupId);
    }

    private void setGroupRef(String groupId){
        this.groupRef = new Firebase(super.firebaseUrl).child("groups").child(groupId);
    }

}
