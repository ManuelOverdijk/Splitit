package nl.mprog.studentchoice10374582.fragments;

/**
 * Created by manuel on 02-12-14.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import nl.mprog.studentchoice10374582.adapters.ChatListAdapter;
import nl.mprog.studentchoice10374582.helpers.ComplexPreferences;
import nl.mprog.studentchoice10374582.helpers.ObjectPreference;
import nl.mprog.studentchoice10374582.R;
import nl.mprog.studentchoice10374582.objectData.Group;
import nl.mprog.studentchoice10374582.objectData.User;

public class GroupInfoFragment extends Fragment {

    private ValueEventListener connectedListener;
    private ChatListAdapter chatListAdapter;

    private String groupId;
    private Group group;
    private Firebase chatRef;
    private Firebase groupRef;
    private LinearLayout linearlayout;

    private static final String ARG_GROUPID = "groupid";
    private static final String TAG = "chatfragment";

    public User user;
    private Boolean loggedin;

    private String firebaseUrl;
    private Firebase ref;
    private AuthData authData;
    private Map participants;

    public ObjectPreference objectPreference;

    public static GroupInfoFragment newInstance(String groupId) {
        GroupInfoFragment f = new GroupInfoFragment();
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        linearlayout = (LinearLayout)inflater.inflate(R.layout.fragment_group_info, container, false);

        Button mButtonAdd = (Button) linearlayout.findViewById(R.id.btPartAdd);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Add a new participant");

                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String participant = input.getText().toString();
                        if(!TextUtils.isEmpty(participant)){

                            /* Add a participant to our group, and push it to Firebase */
                            participants.put(participant.toString(), "true");
                            group.setParticipants(participants);
                            groupRef.setValue(group);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });

        Button btChTitle = (Button) linearlayout.findViewById(R.id.btChTitle);
        btChTitle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Change Title");

                final EditText input = new EditText(getActivity());
                input.setText(group.getTitle());
                alert.setView(input);

                alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // parse data from EditText.
                        String title = input.getText().toString();
                        if (!TextUtils.isEmpty(title)) {

                            /* Change group Title and push it to Firebase */
                            group.setTitle(title);
                            groupRef.setValue(group);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });
        return linearlayout;
    }

    @Override
    public void onResume() {
        super.onResume();

        setGroupRef(groupId);

        /* Get the group data */
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                group = snapshot.getValue(Group.class);
                group.setId(groupId);

                TextView textViewGroupAdmin = (TextView)linearlayout.findViewById(R.id.tvGroupAdmin);
                textViewGroupAdmin.setText(group.getAdmin());

                TextView textViewGroupTitle = (TextView)linearlayout.findViewById(R.id.tvGroupTitle);
                textViewGroupTitle.setText(group.getTitle());

                 participants = group.getParticipants();


                /* Bind our ListView to the adapter, to fetch it Participants */
                ListView list = (ListView)linearlayout.findViewById(R.id.listViewParticipants);
                ParticipantsAdapter adapter = new ParticipantsAdapter(getActivity(),group.getParticipants());
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    private void setGroupRef(String groupId){
        this.groupRef = new Firebase(firebaseUrl).child("groups").child(groupId);
    }

    public class ParticipantsAdapter extends BaseAdapter{

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

                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(getItem(position));
            }
            return convertView;
        }
    }

}