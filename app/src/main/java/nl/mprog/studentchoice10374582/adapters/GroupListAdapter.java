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

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import nl.mprog.studentchoice10374582.R;
import nl.mprog.studentchoice10374582.objectData.Group;

public class GroupListAdapter extends FirebaseListAdapterGroup {

    // The username for this client. We use this to indicate which messages originated from this user
    private String username;

    public GroupListAdapter(Query ref, Activity activity, int layout, String username) {

        //create FirebaseListAdapter
        super(ref, layout, activity, username);
        this.username = username;
    }

    @Override
    protected void populateView(View view, Group group) {

        // Map a Group object to an entry in our listview
        String admin = group.getAdmin();
        String title = group.getTitle();
        TextView adminText = (TextView)view.findViewById(R.id.admin);
        adminText.setTag(String.valueOf(group.getId()));
        adminText.setText(title);
        ((TextView)view.findViewById(R.id.title)).setText(admin);

    }
}