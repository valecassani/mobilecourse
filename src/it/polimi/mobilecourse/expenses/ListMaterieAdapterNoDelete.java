package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Valerio on 28/08/2015.
 */
public class ListMaterieAdapterNoDelete extends BaseAdapter {

    private Context context;
    private List<ListMaterieItem> items;


    public ListMaterieAdapterNoDelete(Context context, List<ListMaterieItem> objects) {
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
            convertView = mInflater.inflate(R.layout.materia_tutor_item_nodelete, null);
        }
        TextView nome=(TextView)convertView.findViewById(R.id.materia_nome);
        TextView prezzo=(TextView)convertView.findViewById(R.id.materia_prezzo);

        nome.setText(items.get(position).getNome());
        prezzo.setText(items.get(position).getPrezzo()+ "   Euro /ora");






        return convertView;
    }


}




