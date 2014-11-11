package nl.mprog.studentchoice10374582;

import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import androidapp.splitit.com.splitit.R;


public class chatActivity extends ListActivity {

    private String firebaseroot = "https://splitit-dev.firebaseio.com/";
    private Firebase firebaseRef;
    private ValueEventListener connectedListener;
    private ChatListAdapter chatListAdapter;

    private String username = "Manuel";
    private String chatId = "GroupChatId";

    private static final String TAG = "SplitIt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseRef = new Firebase(firebaseroot).child("chat").child(chatId);

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

        final ListView listView = getListView();

        // Tell our list adapter that we only want 50 messages at a time
        chatListAdapter = new ChatListAdapter(firebaseRef.limit(10), this, R.layout.chat_message, username);
        listView.setAdapter(chatListAdapter);
        chatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        connectedListener = firebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {

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

    private void sendMessage() {
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, username);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            firebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
