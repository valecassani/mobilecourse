package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Valerio on 11/09/2015.
 */
public class ListTutorRecAdapter extends RecyclerView.Adapter<ListTutorRecAdapter.DataObjectHolder> {
    private static String TAG = "ViewAdapter";
    private static ArrayList<ListTutorItem> items;

    private static Context context;


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView nome;
        TextView uni;
        RatingBar media;
        CircularImageView img;


        public DataObjectHolder(View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.list_tutor_nome);
            uni = (TextView) itemView.findViewById(R.id.list_tutor_uni);
            media = (RatingBar) itemView.findViewById(R.id.list_tutor_media);
            img = (CircularImageView) itemView.findViewById(R.id.list_tutor_image);
            itemView.setOnClickListener(this);
            Log.i(TAG,"Image view set");


        }

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
            int itemPosition = getAdapterPosition();

            Fragment fragment = new SearchTutorDetails();
            Bundle bundle = new Bundle();
            bundle.putString("idt", items.get(itemPosition).getId());
            fragment.setArguments(bundle);
            System.out.println("Bundle" + bundle);
            fragmentManager.beginTransaction().replace(R.id.student_fragment, fragment).addToBackStack(null).commit();


        }
    }



    public ListTutorRecAdapter(ArrayList<ListTutorItem> myDataset) {

        this.items = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row_tutor, parent, false);

        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {


        holder.nome.setText(items.get(position).getNome() + " " + items.get(position).getCognome().substring(0, 1) + ".");
        holder.uni.setText(items.get(position).getUni());
        if (items.get(position).getMedia() != 0) {
            holder.media.setRating(items.get(position).getMedia());
            LayerDrawable stars = (LayerDrawable)holder.media.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.primaryColor), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.media.setVisibility(View.GONE);
        }
        if (items.get(position).getUrl().compareTo(" ") == 0) {
            holder.img.setImageResource(R.drawable.dummy_profpic);
        } else if (items.get(position).getUrl().compareTo("face") == 0) {

            Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + items.get(position).getIdfb() + "/picture"
            ).into(holder.img);


        } else {
            Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + items.get(position).getUrl()
            ).into(holder.img);

        }





    }

    public void addItem(ListTutorItem dataObj, int index) {
        items.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface MyClickListener extends DialogInterface.OnClickListener {


    }
}