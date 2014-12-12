package nl.mprog.studentchoice10374582;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import nl.mprog.studentchoice10374582.helpers.ComplexPreferences;
import nl.mprog.studentchoice10374582.helpers.ObjectPreference;
import nl.mprog.studentchoice10374582.objectData.Bill;
import nl.mprog.studentchoice10374582.objectData.User;


public class BillActivity extends MyActionBarActivity {

    Bill bill;
    String billId;
    String groupId;

    String firebaseUrl;
    private Firebase billRef;
    private AuthData authData;
    android.support.v7.widget.Toolbar toolbar;
    Map participants;
    ParticipantsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        Firebase.setAndroidContext(getApplicationContext());

        objectPreference = (ObjectPreference) this.getApplication();
        ComplexPreferences complexPreferences = objectPreference.getComplexPreference();

        firebaseUrl = getResources().getString(R.string.firebase_url);

        user = complexPreferences.getObject("user", User.class);
        authData = user.getAuthData();

        participants = new HashMap();
        ListView list = (ListView)this.findViewById(R.id.listViewParticipants);
        adapter = new ParticipantsAdapter(this,participants);
        list.setAdapter(adapter);

        /* Customize our toolbar */
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("BillView: ");

        Button mButtonAdd = (Button) findViewById(R.id.buttonAddPayment);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPayment();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        /* Get GroupId for fetching chat messages */
        Intent intent = getIntent();
        if(intent!=null){
            this.billId = intent.getStringExtra("billId");
            this.groupId = intent.getStringExtra("groupId");

        } else {
            Log.e("BillActivity", "Error - Intent is null!");
        }

        if(billId!=null && groupId!=null){

            setBillRef(billId, groupId);


        /* Get the group data */
            billRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    Log.e("TAG",snapshot.toString());

                    bill = snapshot.getValue(Bill.class);
                    bill.setId(billId);

                    toolbar.setTitle("BillView:" + bill.getTitle());

                    participants = bill.getParticipants();

                    TextView tvBillCreated = (TextView) findViewById(R.id.tvBillCreated);
                    tvBillCreated.setText(bill.getCreated().toString());

                    TextView tvBillTitle = (TextView) findViewById(R.id.tvBillTitle);
                    tvBillTitle.setText(bill.getTitle());

                    TextView tvBillCreator = (TextView) findViewById(R.id.tvBillCreator);
                    tvBillCreator.setText(bill.getAdmin());

                    TextView tvBillPaid = (TextView) findViewById(R.id.tvBillPaid);
                    tvBillPaid.setText("€" + bill.getPaid());

                    TextView tvBillTotal = (TextView) findViewById(R.id.tvBillTotal);
                    tvBillTotal.setText("€" + bill.getTotal());

                    ListView list = (ListView)findViewById(R.id.listViewParticipants);
                    adapter = new ParticipantsAdapter(getApplicationContext(),participants);
                    list.setAdapter(adapter);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

        }



    }

    public void addPayment(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add Payment");

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);


        final EditText inputAmount = new EditText(this);
        inputAmount.setHint("Amount");
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputAmount);

        alert.setView(layout);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!TextUtils.isEmpty(inputAmount.getText().toString())) {

                    int amount = Integer.parseInt(inputAmount.getText().toString());
                    String userid = user.getUid();

                    if(participants.containsKey(userid)){
                        Object value = participants.get(userid);
                        int oldValue = Integer.parseInt(value.toString());

                        participants.put(userid,(oldValue - amount));

                        bill.setParticipants(participants);
                        bill.setPaid(bill.getPaid()+amount);
                        billRef.setValue(bill);
                    } else {
                        Toast.makeText(getApplicationContext(), "User not defined", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return false;
    }

    private void setBillRef(String billId, String groupId){
        this.billRef = new Firebase(firebaseUrl).child("bills").child(groupId).child(billId);
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

                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(getItem(position));
            }

            return convertView;
        }
    }

}
