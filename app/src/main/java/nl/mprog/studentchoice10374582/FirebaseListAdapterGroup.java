package nl.mprog.studentchoice10374582;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * Edited version of the FirebaseListAdapter provided by Firebase.
 * https://github.com/firebase/AndroidChat/blob/master/src/com/firebase/androidchat/FirebaseListAdapter.java
 *
 */
public abstract class FirebaseListAdapterGroup extends BaseAdapter {

    private Query ref;
    private Group modelClass;
    private int layout;
    private LayoutInflater inflater;
    private List<Group> models;
    private Map<String, Group> modelNames;
    private ChildEventListener listener;
    private String userId;

    public FirebaseListAdapterGroup(Query ref, int layout, Activity activity, final String userId) {
        this.ref = ref;
        this.userId = userId;
        this.modelClass = modelClass;
        this.layout = layout;
        inflater = activity.getLayoutInflater();
        models = new ArrayList<Group>();
        modelNames = new HashMap<String, Group>();
        
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        listener = this.ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.e("Firebase",userId);
                String groupId = dataSnapshot.getName();
//                dataSnapshot.chi
                Map participants = dataSnapshot.child("participants").getValue(Map.class);
                Log.e("Name", participants.toString());

                Boolean isEmpty = true;

                Iterator iterator = participants.entrySet().iterator();

                // iterate through participants
                while (iterator.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) iterator.next();
                    System.out.println("The key is: " + mapEntry.getKey()
                            + ",value is :" + mapEntry.getValue());

                    String member = mapEntry.getKey().toString();
                    Log.e("member",member);

                    // found current user, populate view with group object
                    if(member.equalsIgnoreCase(userId)){
                        Log.e("member","found a valid group!");

                        //Create group object with Firebase data
                        Group model = dataSnapshot.getValue(Group.class);
                        model.setId(dataSnapshot.getName());

                        modelNames.put(dataSnapshot.getName(), model);

                        // Insert into the correct location, based on previousChildName
                        if (previousChildName == null) {
                            models.add(0, model);
                        } else {
                            Group previousModel = modelNames.get(previousChildName);
                            int previousIndex = models.indexOf(previousModel);
                            int nextIndex = previousIndex + 1;
                            if (nextIndex == models.size()) {
                                models.add(model);
                            } else {
                                models.add(nextIndex, model);
                            }
                        }
                        notifyDataSetChanged();
                        isEmpty = false;
                        break;
                    }
                }

                if(isEmpty){
                    // TODO: Display empty message
                }

            }


            public void onCancelled(FirebaseError error){
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // One of the models changed. Replace it in our list and name mapping
                String modelName = dataSnapshot.getName();
                Group oldModel = modelNames.get(modelName);
                Group newModel = dataSnapshot.getValue(Group.class);
                int index = models.indexOf(oldModel);

                models.set(index, newModel);
                modelNames.put(modelName, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String modelName = dataSnapshot.getName();
                Group oldModel = modelNames.get(modelName);
                models.remove(oldModel);
                modelNames.remove(modelName);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String modelName = dataSnapshot.getName();
                Group oldModel = modelNames.get(modelName);
                Group newModel = dataSnapshot.getValue(Group.class);
                int index = models.indexOf(oldModel);
                models.remove(index);
                if (previousChildName == null) {
                    models.add(0, newModel);
                } else {
                    Group previousModel = modelNames.get(previousChildName);
                    int previousIndex = models.indexOf(previousModel);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == models.size()) {
                        models.add(newModel);
                    } else {
                        models.add(nextIndex, newModel);
                    }
                }
                notifyDataSetChanged();
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our listener and forget about all of the models
        ref.removeEventListener(listener);
        models.clear();
        modelNames.clear();
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        Group model = models.get(i);
        // Call out to subclass to marshall this model into the provided view
        populateView(view, model);
        return view;
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the layout and modelClass given to the constructor of this class.
     *
     * Your implementation should populate the view using the data contained in the model.
     * @param v The view to populate
     * @param model The object containing the data used to populate the view
     */
    protected abstract void populateView(View v, Group model);
}