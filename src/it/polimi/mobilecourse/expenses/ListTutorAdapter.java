package it.polimi.mobilecourse.expenses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Matteo on 16/08/2015.
 */
public class ListTutorAdapter extends BaseAdapter {
    private Context context;
    private List<ListTutorItem> items;
    Bitmap bitmap;


    public ListTutorAdapter(Context context, List<ListTutorItem> objects) {
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
            convertView = mInflater.inflate(R.layout.list_tutor_item, null);
        }

        TextView nome = (TextView) convertView.findViewById(R.id.list_tutor_nome);
        TextView uni = (TextView) convertView.findViewById(R.id.list_tutor_uni);
        RatingBar media = (RatingBar) convertView.findViewById(R.id.list_tutor_media);
        CircularImageView img = (CircularImageView) convertView.findViewById(R.id.list_tutor_image);

        nome.setText(items.get(position).getNome() + " " + items.get(position).getCognome().substring(0, 1) + ".");
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

