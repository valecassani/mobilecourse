package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Matteo on 02/09/2015.
 */
public class ListRecensioniAdapter extends BaseAdapter {

    private Context context;
    private List<ListRecensioneItem> items;

    public ListRecensioniAdapter(Context context, List<ListRecensioneItem> objects) {
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
            convertView = mInflater.inflate(R.layout.recensione_tutor_item, null);
        }

        CircularImageView si=(CircularImageView)convertView.findViewById(R.id.stud_image);
        TextView nome=(TextView)convertView.findViewById(R.id.rec_nome);
        RatingBar rb=(RatingBar)convertView.findViewById(R.id.rec_val);
        LayerDrawable stars=(LayerDrawable)rb.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.primaryColor), PorterDuff.Mode.SRC_ATOP);

        nome.setText("Recensione di "+items.get(position).getNome()+ " "+items.get(position).getCognome().substring(0,1)+".");
        rb.setRating(items.get(position).getVoto_finale());

         if (items.get(position).getIdfb().compareTo("") != 0) {

            Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + items.get(position).getIdfb() + "/picture"
            ).into(si);



        } else if(items.get(position).getFoto().compareTo("") != 0){
            Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + items.get(position).getFoto()
            ).into(si);

        }
        else{

             si.setImageResource(R.drawable.dummy_profpic);

         }






        return convertView;
    }


}
