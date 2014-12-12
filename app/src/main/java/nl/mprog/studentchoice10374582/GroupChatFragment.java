package nl.mprog.studentchoice10374582;


/**
 * Created by manuel on 02-12-14.
 */
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import nl.mprog.studentchoice10374582.objectData.Chat;
import nl.mprog.studentchoice10374582.objectData.Group;
import nl.mprog.studentchoice10374582.objectData.User;

public class GroupChatFragment extends Fragment {

    private ValueEventListener connectedListener;
    private ChatListAdapterFragment chatListAdapterFragment;

    private String groupId;
    private Group group;
    private Firebase chatRef;
    private Firebase groupRef;
    private RelativeLayout relativeLayout;

    private static final String ARG_GROUPID = "groupid";
    private static final String TAG = "chatfragment";

    public User user;
    private Boolean loggedin;

    private String firebaseUrl;
    private Firebase ref;
    private AuthData authData;

    public ObjectPreference objectPreference;

    public static GroupChatFragment newInstance(String groupId) {
        GroupChatFragment f = new GroupChatFragment();
        Bundle b = new Bundle();
        b.putString(ARG_GROUPID, groupId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());
        firebaseUrl = getResources().getString(R.string.firebase_url);

        groupId = getArguments().getString(ARG_GROUPID);

        objectPreference = (ObjectPreference) getActivity().getApplication();
        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();

        user = complexPreferences.getObject("user", User.class);
        authData = user.getAuthData();


        Log.e("TAG", "onCreate ChatFragment");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        relativeLayout = (RelativeLayout )inflater.inflate(R.layout.fragment_chat_list, container, false);

        EditText inputText = (EditText)relativeLayout.findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        relativeLayout.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        return relativeLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Get GroupId for fetching chat messages */


        setChatRef(groupId);
        setGroupRef(groupId);

        // TODO: separate this is some sort of model

        /* Get the group data */
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                group = snapshot.getValue(Group.class);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        final ListView listView = (ListView) getView().findViewById(R.id.list);


        // TODO: Determine best amount of chat messages to be fetched
        chatListAdapterFragment = new ChatListAdapterFragment(chatRef.limit(50), this, R.layout.chat_message, user.getName());
        listView.setAdapter(chatListAdapterFragment);
        chatListAdapterFragment.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatListAdapterFragment.getCount() - 1);
            }
        });

        connectedListener = chatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Log.d(TAG, "Connected to Firebase");
                } else {
                    Log.d(TAG, "Disconnected from Firebase");
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    /* Handles creation of new chat messages and pushes them to Firebase */
    private void sendMessage() {
        EditText inputText = (EditText)relativeLayout.findViewById(R.id.messageInput);
        String input = inputText.getText().toString();

        if (!input.equals("")) {
            Chat chat = new Chat(input, user.getName());
            /* Save the new chat model to Firebase */
            chatRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    /* For setting the FirebaseRef, based on which group the Activity is currently in */
    private void setChatRef(String groupId){
        this.chatRef = new Firebase(firebaseUrl).child("chats").child(groupId);
    }

    private void setGroupRef(String groupId){
        this.groupRef = new Firebase(firebaseUrl).child("groups").child(groupId);
    }

}