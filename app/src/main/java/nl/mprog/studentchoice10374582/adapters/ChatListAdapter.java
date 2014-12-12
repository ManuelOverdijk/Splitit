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
import nl.mprog.studentchoice10374582.objectData.Chat;


/* Bind our FirebaseAdapter to the Chat Adapter */
public class ChatListAdapter extends FirebaseListAdapterChat<Chat> {

    // The username for this client. We use this to indicate which messages originated from this user
    private String username;

    public ChatListAdapter(Query ref, Fragment fragment, int layout, String username) {
        super(ref, Chat.class, layout, fragment.getActivity());
        this.username = username;
    }

    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        TextView authorText = (TextView)view.findViewById(R.id.author);
        authorText.setText(author + ": ");


        if (author.equals(username)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }

        ((TextView)view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}