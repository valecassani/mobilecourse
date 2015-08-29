package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

/**
 * Created by valeriocassani on 14/04/15.
 */
public class PrenotazioniAdapter extends ArrayAdapter<PrenotazioniItem>{

    Context context;
    List<PrenotazioniItem> items;

    public PrenotazioniAdapter(Context context, int resource, List<PrenotazioniItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PrenotazioniItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.ripet_item,null);


//set show mode.





        }

        TextView title = (TextView) convertView.findViewById(R.id.prenot_title);
        TextView data = (TextView) convertView.findViewById(R.id.prenot_data);

        title.setText(items.get(position).getMateria());
        data.setText(items.get(position).getData());

        if (items.get(position).isConfermato()) {
            title.setTypeface(Typeface.DEFAULT_BOLD);
        }






        return convertView;
    }
}
