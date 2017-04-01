package lmohs.cmu.edu.beerfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lukasmohs on 31/03/17.
 */

public class BarsAdapter extends ArrayAdapter<Bar> {


    public BarsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public BarsAdapter(Context context, int resource, List<Bar> bars) {
        super(context, resource, bars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.barlistrow, null);
        }

        Bar b = getItem(position);
        System.out.println("bar " + position + ": " + b.getName());
        if (b != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt1 != null) {
                tt1.setText(b.getName());
            }

            if (tt2 != null) {
                tt2.setText(b.getAddress());
            }

            if (tt3 != null) {
                tt3.setText(b.getPrice());
            }
        }

        return v;
    }

}