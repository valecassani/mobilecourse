package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valeriocassani on 14/04/15.
 */
public class PrenotazioniAdapter extends ArrayAdapter<RipetizioniItem>{

    Context context;
    List<RipetizioniItem> items;

    public PrenotazioniAdapter(Context context, int resource, List<RipetizioniItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RipetizioniItem getItem(int position) {
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
            SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.ripet_item);

//set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//set drag edge.
            swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout swipeLayout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout swipeLayout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });






        }

        TextView title = (TextView) convertView.findViewById(R.id.prenot_title);
        TextView data = (TextView) convertView.findViewById(R.id.prenot_data);
        Log.i("Ripetizioni", "Sono arrivato a caricarmi le textview");

        title.setText(items.get(position).getMateria());
        data.setText(items.get(position).getData());
        Log.i("Ripetizioni", "MATERIA CARICATA "+ items.get(position).getMateria());


        return convertView;
    }
}
