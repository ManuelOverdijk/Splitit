package nl.mprog.studentchoice10374582;

/**
 * Created by manuel on 05-12-14.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.mprog.studentchoice10374582.adapters.GroupListAdapter;
import nl.mprog.studentchoice10374582.objectData.Bill;


public class NewBillActivity extends MyActionBarActivity {

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
    private Bill bill;
    private String groupId;
    private int userAmount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("NewGroup", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbill);

        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        mToolbar.setTitle("Add a new bill");

        bill = new Bill();
        mParticipants = new HashMap();

          /* Get GroupId for fetching chat messages */
        Intent intent = getIntent();
        if(intent!=null){
            this.groupId = intent.getStringExtra("groupId");

        } else {
            Log.e("NewBillActivity", "Error - Intent is null!");
        }

        /* Set Firebase reference for fetching grouplist */
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(super.firebaseUrl).child("bills").child(groupId);

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



        EditText amount = (EditText) findViewById(R.id.editTextAmount);
        if(TextUtils.isEmpty(amount.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Total is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText amountYou = (EditText) findViewById(R.id.editTextAmountYou);
        if(TextUtils.isEmpty(amountYou.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Your amount is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int total = Integer.parseInt(amount.getText().toString());

        userAmount = Integer.parseInt(amountYou.getText().toString());
        mParticipants.put(mAdmin,userAmount);

        // Create new group
        bill.setAdmin(mAdmin);
        bill.setTitle(mTitle);
        bill.setParticipants(mParticipants);
        bill.setCompleted(false);
        bill.setCreated(new Date().getTime());
        bill.setTotal(total);
        bill.setPaid(0);

        // Push new Bill to Firebase
        ref.push().setValue(bill);


        // Send user back to MainActivity
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    private void addParticipant() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add participant");

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);


        final EditText inputPart = new EditText(this);
        inputPart.setHint("Use simplelogin:1 for testing");
        layout.addView(inputPart);

        final EditText inputAmount = new EditText(this);
        inputAmount.setHint("Amount to be paid by this user");
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputAmount);

        alert.setView(layout);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // parse data from EditText.

                String title = inputPart.getText().toString();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(inputAmount.getText().toString())) {

                    if(mParticipants.containsKey(title)){
                        Log.e("Key","Contains key!");
                    } else {
                        Log.e("Key","Loggin key");
                        mParticipants.put(title,Integer.parseInt(inputAmount.getText().toString()));
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

                Log.e("TAG",participants.toString());

                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(getItem(position));
            }

            return convertView;
        }
    }

}
