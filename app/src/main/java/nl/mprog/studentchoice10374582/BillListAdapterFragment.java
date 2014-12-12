package nl.mprog.studentchoice10374582;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;


public class BillListAdapterFragment extends FirebaseListAdapterBill{

    // The username for this client. We use this to determine if the username is the admin, and display the appropriote controls.
    private String username;

    public BillListAdapterFragment(Query ref, Fragment fragment, int layout, String username) {
//        super(ref, Bill.class, layout, fragment.getActivity());
        super(ref, layout, fragment.getActivity(), username);
        this.username = username;
    }


    /* Populate our list with Bill objects */
    @Override
    protected void populateView(View view, Bill bill) {

        String author = bill.getAdmin();
        TextView authorText = (TextView)view.findViewById(R.id.author);
        authorText.setText(author + ":");

        authorText.setTextColor(Color.BLUE);

        ((TextView)view.findViewById(R.id.message)).setText(bill.getTitle());

        ((TextView)view.findViewById(R.id.billId)).setText(bill.getId());
    }
}