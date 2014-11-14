package nl.mprog.studentchoice10374582;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Query;

import androidapp.splitit.com.splitit.R;

public class GroupListAdapter extends FirebaseListAdapter<Group> {

    // The username for this client. We use this to indicate which messages originated from this user
    private String username;

    public GroupListAdapter(Query ref, Activity activity, int layout, String username) {
        super(ref, Group.class, layout, activity);
        this.username = username;
    }

    @Override
    protected void populateView(View view, Group group) {

        // Map a Group object to an entry in our listview
        String admin = group.getAdmin();
        TextView adminText = (TextView)view.findViewById(R.id.admin);
        adminText.setText(admin + ": ");
        ((TextView)view.findViewById(R.id.title)).setText(group.getTitle());
    }
}