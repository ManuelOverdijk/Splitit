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

public class GroupBillFragment extends Fragment {

    private ValueEventListener connectedListener;
    private ChatListAdapterFragment chatListAdapterFragment;

    private String groupId;
    private Group group;

    private Firebase billRef;
    private Firebase groupRef;
    private RelativeLayout relativeLayout;

    private static final String GROUPID = "groupid";
    private static final String TAG = "billfragment";

    public User user;
    private Boolean loggedin;

    private String firebaseUrl;
    private Firebase ref;
    private AuthData authData;

    public ObjectPreference objectPreference;

    public static GroupBillFragment newInstance(String groupId) {
        GroupBillFragment f = new GroupBillFragment();
        Bundle b = new Bundle();
        b.putString(GROUPID, groupId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());
        firebaseUrl = getResources().getString(R.string.firebase_url);

        groupId = getArguments().getString(GROUPID);

        objectPreference = (ObjectPreference) getActivity().getApplication();
        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();

        user = complexPreferences.getObject("user", User.class);
        authData = user.getAuthData();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return relativeLayout = (RelativeLayout )inflater.inflate(R.layout.fragment_bill_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Get GroupId for fetching bill s */

        setBillRef(groupId);
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
        chatListAdapterFragment = new ChatListAdapterFragment(billRef.limit(10), this, R.layout.bill_layout, user.getName());
        listView.setAdapter(chatListAdapterFragment);
        chatListAdapterFragment.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatListAdapterFragment.getCount() - 1);
            }
        });

        connectedListener = billRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {

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

    /* For setting the FirebaseRef, based on which group the Activity is currently in */
    private void setBillRef(String groupId){
        this.billRef = new Firebase(firebaseUrl).child("bills").child(groupId);
    }

    private void setGroupRef(String groupId){
        this.groupRef = new Firebase(firebaseUrl).child("groups").child(groupId);
    }

}