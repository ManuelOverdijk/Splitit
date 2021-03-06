/** Native App Studio
 *
 * SplitIt - Student's choice
 *
 * Author: Manuel Overdijk
 *         manuel.overdijk@gmail.com
 *         Uva id: 10374582
 *
 * December 2014
 */

package nl.mprog.studentchoice10374582.adapters;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import nl.mprog.studentchoice10374582.R;
import nl.mprog.studentchoice10374582.objectData.Bill;

/* Bind our FirebaseAdapter to the Chat Adapter */
public class BillListAdapter extends FirebaseListAdapterBill {

    // The username for this client. We use this to determine if the username is the admin, and display the controls.
    private String username;

    public BillListAdapter(Query ref, Fragment fragment, int layout, String username) {
        super(ref, layout, fragment.getActivity(), username);
        this.username = username;
    }

    /* Populate our list with Bill objects */
    @Override
    protected void populateView(View view, Bill bill) {

        String author = bill.getAdmin();
        TextView authorText = (TextView)view.findViewById(R.id.author);
        authorText.setText(author + ":");

        TextView billId = (TextView)view.findViewById((R.id.billId));

        billId.setText(bill.getId());

        authorText.setTextColor(Color.BLUE);

        ((TextView)view.findViewById(R.id.message)).setText(bill.getTitle());

        ((TextView)view.findViewById(R.id.billId)).setText(bill.getId());
    }
}