package nl.mprog.studentchoice10374582;

/**
 * Created by manuel on 02-12-14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class GroupViewFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static GroupViewFragment newInstance(int position) {
        GroupViewFragment f = new GroupViewFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        Log.e("TAG", "onCreate GroupViewFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                .getDisplayMetrics());

        TextView v = new TextView(getActivity());
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        //v.setBackgroundResource(R.drawable.powered_by_google_light);
        v.setText("CARD " + (position + 1));

        fl.addView(v);
        return fl;
    }

}