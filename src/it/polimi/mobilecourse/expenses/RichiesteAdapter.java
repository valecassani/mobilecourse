package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by valeriocassani on 16/04/15.
 */
public class RichiesteAdapter extends BaseAdapter{
    private ArrayList<RichiestaItem> items;
    private Context context;

    public RichiesteAdapter(Context context, ArrayList<RichiestaItem> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.richieste_item, null);
        }

        TextView testo = (TextView) convertView.findViewById(R.id.testo_richiesta);

        testo.setText(items.get(position).getTesto());




        return convertView;
    }
}
