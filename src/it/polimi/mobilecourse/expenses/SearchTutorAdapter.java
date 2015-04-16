package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by valeriocassani on 15/04/15.
 */
public class SearchTutorAdapter extends BaseAdapter {
    private Context context;
    private List<SearchTutorItem> items;


    public SearchTutorAdapter(Context context, List<SearchTutorItem> objects) {
        this.context = context;
        this.items = objects;

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
            convertView = mInflater.inflate(R.layout.search_tutor_item, null);
        }

        TextView nome = (TextView) convertView.findViewById(R.id.search_tutor_nome);
        nome.setText(items.get(position).getNome());
        return convertView;
    }
}
