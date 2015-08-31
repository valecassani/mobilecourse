package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

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

        TextView nome = (TextView) convertView.findViewById(R.id.ricerca_tutor_nome);
        TextView uni= (TextView) convertView.findViewById(R.id.ricerca_tutor_uni);
        RatingBar media = (RatingBar) convertView.findViewById(R.id.ricerca_tutor_media);
        CircularImageView img = (CircularImageView) convertView.findViewById(R.id.search_tutor_image);


        nome.setText(items.get(position).getNome() + " " + items.get(position).getCognome());
        uni.setText(items.get(position).getUni());
        if (items.get(position).getMedia() != 0) {
            media.setRating(items.get(position).getMedia());
        } else {
            media.setVisibility(View.GONE);
        }
        if (items.get(position).getUrl().compareTo(" ") == 0) {
            img.setImageResource(R.drawable.dummy_profpic);
        } else if (items.get(position).getUrl().compareTo("face") == 0) {

            Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + items.get(position).getIdfb() + "/picture"
            ).into(img);



        } else {
            Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + items.get(position).getUrl()
            ).into(img);

        }
        return convertView;
    }
}
