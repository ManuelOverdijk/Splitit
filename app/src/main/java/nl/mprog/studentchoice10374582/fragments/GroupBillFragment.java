package nl.mprog.studentchoice10374582.fragments;

/**
 * Created by manuel on 02-12-14.
 */

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import nl.mprog.studentchoice10374582.BillActivity;
import nl.mprog.studentchoice10374582.adapters.BillListAdapter;
import nl.mprog.studentchoice10374582.helpers.ComplexPreferences;
import nl.mprog.studentchoice10374582.NewBillActivity;
import nl.mprog.studentchoice10374582.helpers.ObjectPreference;
import nl.mprog.studentchoice10374582.R;
import nl.mprog.studentchoice10374582.objectData.Group;
import nl.mprog.studentchoice10374582.objectData.User;

public class GroupBillFragment extends Fragment {

    private ValueEventListener connectedListener;
    private BillListAdapter billListAdapter;

    private String groupId;
    private Group group;

    private Firebase billRef;
    private Firebase groupRef;
    private LinearLayout linearLayout;

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
       return linearLayout = (LinearLayout )inflater.inflate(R.layout.fragment_bill_list, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        setBillRef(groupId);
        setGroupRef(groupId);

        /* Get the group data */
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                /* Map the data to a group object */
                group = snapshot.getValue(Group.class);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        final ListView listView = (ListView) getView().findViewById(R.id.list);


        billListAdapter = new BillListAdapter(billRef.limit(10), this, R.layout.bill_layout, user.getName());
        listView.setAdapter(billListAdapter);
        billListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(billListAdapter.getCount() - 1);
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

          /* Handle clicks on ListView's items and start new chatActivity */
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvBillId = (TextView)view.findViewById(R.id.billId);
                String billId = (String) tvBillId.getText();

                Intent intent = new Intent().setClass(getActivity().getBaseContext(),BillActivity.class);

                intent.putExtra("billId", billId);
                intent.putExtra("groupId",groupId);
                startActivity(intent);

                Toast.makeText(getActivity().getBaseContext(), billId, Toast.LENGTH_SHORT).show();
            }
        });

        Button mNewBill = (Button) getView().findViewById(R.id.btNewBill);

        mNewBill.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent().setClass(getActivity().getApplicationContext(),NewBillActivity.class);

                intent.putExtra("groupId", groupId);
                startActivity(intent);
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