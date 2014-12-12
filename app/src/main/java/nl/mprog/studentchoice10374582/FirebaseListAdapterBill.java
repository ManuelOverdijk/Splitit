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

public abstract class FirebaseListAdapterBill extends BaseAdapter {

    private Query ref;
    private Bill modelClass;
    private int layout;
    private LayoutInflater inflater;
    private List<Bill> models;
    private Map<String, Bill> modelNames;
    private ChildEventListener listener;
    private String userId;

    public FirebaseListAdapterBill(Query ref, int layout, Activity activity, final String userId) {
        this.ref = ref;
        this.userId = userId;
        this.modelClass = modelClass;

        this.layout = layout;
        inflater = activity.getLayoutInflater();

        models = new ArrayList<Bill>();
        modelNames = new HashMap<String, Bill>();

        listener = this.ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                String groupId = dataSnapshot.getKey();
                Log.e("TAG",dataSnapshot.toString());
                Log.e("TAG",listener.toString());
                Bill model = dataSnapshot.getValue(Bill.class);
                model.setId(dataSnapshot.getKey());

                modelNames.put(dataSnapshot.getKey(), model);

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    models.add(0, model);
                } else {
                    Bill previousModel = modelNames.get(previousChildName);
                    int previousIndex = models.indexOf(previousModel);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == models.size()) {
                        models.add(model);
                    } else {
                        models.add(nextIndex, model);
                    }
                }
                notifyDataSetChanged();

            }


            public void onCancelled(FirebaseError error){
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // One of the models changed. Replace it in our list and name mapping
                String modelName = dataSnapshot.getKey();
                Bill oldModel = modelNames.get(modelName);
                Bill newModel = dataSnapshot.getValue(Bill.class);
                int index = models.indexOf(oldModel);

                models.set(index, newModel);
                modelNames.put(modelName, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String modelName = dataSnapshot.getKey();
                Bill oldModel = modelNames.get(modelName);
                models.remove(oldModel);
                modelNames.remove(modelName);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String modelName = dataSnapshot.getKey();
                Bill oldModel = modelNames.get(modelName);
                Bill newModel = dataSnapshot.getValue(Bill.class);
                int index = models.indexOf(oldModel);
                models.remove(index);
                if (previousChildName == null) {
                    models.add(0, newModel);
                } else {
                    Bill previousModel = modelNames.get(previousChildName);
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

        Bill model = models.get(i);
        // Call out to subclass to marshall this model into the provided view
        populateView(view, model);
        return view;
    }

    protected abstract void populateView(View v, Bill model);
}