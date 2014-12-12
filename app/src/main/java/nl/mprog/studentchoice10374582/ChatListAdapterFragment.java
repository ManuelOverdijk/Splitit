package nl.mprog.studentchoice10374582;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Query;


/* https://github.com/firebase/AndroidChat/blob/master/src/com/firebase/androidchat/ChatListAdapter.java */

/**
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */

public class ChatListAdapterFragment extends FirebaseListAdapterChat<Chat> {

    // The username for this client. We use this to indicate which messages originated from this user
    private String username;

    public ChatListAdapterFragment(Query ref, Fragment fragment, int layout, String username) {
        super(ref, Chat.class, layout, fragment.getActivity());
        this.username = username;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        TextView authorText = (TextView)view.findViewById(R.id.author);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author.equals(username)) {
            authorText.setTextColor(Color.RED);
        } else {
            authorText.setTextColor(Color.BLUE);
        }
        ((TextView)view.findViewById(R.id.message)).setText(chat.getMessage());
    }
}