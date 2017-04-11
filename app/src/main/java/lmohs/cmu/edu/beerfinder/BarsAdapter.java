package lmohs.cmu.edu.beerfinder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This adapter extends an ArrayAdapter in order to play the model for the list view within the Main Activity
 * Created by lukasmohs on 31/03/17.
 */

public class BarsAdapter extends ArrayAdapter<Bar> {

    /**
     * Constructor
     * @param context
     * @param textViewResourceId
     */
    public BarsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    /**
     * Constructor
     * @param context
     * @param resource
     * @param bars
     */
    public BarsAdapter(Context context, int resource, List<Bar> bars) {
        super(context, resource, bars);
    }

    /**
     * This method returns a single View based on the barlistrow.xml specification for each element in
     * the provided barlist. This method is invoked for each element and sets the values of the text fields.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        // Check whether view was already provided as argument
        if (v == null) {
            LayoutInflater vi;
            // Inflate layout based on barlistrow.xml
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.barlistrow, null);
        }

        // Get corresponding item from ArrayList
        Bar b = getItem(position);
        if (b != null) {
            // If item not null, get text fields
            TextView t1 = (TextView) v.findViewById(R.id.name);
            TextView t2 = (TextView) v.findViewById(R.id.address);
            TextView t3 = (TextView) v.findViewById(R.id.price);
            // Set values of textfields to Bar item attributes
            t1.setText(b.getName());
            t2.setText(b.getAddress());
            t3.setText(b.getPrice());
            // Set color of price indicator depending on price level
            if(b.getPrice().length()>=3) {
                t3.setTextColor(Color.RED);
            } else if (b.getPrice().length()==2) {
                t3.setTextColor(Color.YELLOW);
            } else {
                t3.setTextColor(Color.GREEN);
            }
        }
        // Return the View
        return v;
    }

}