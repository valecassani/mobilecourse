package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Valerio on 14/09/2015.
 */
public class PrenotazioniCardAdapter extends RecyclerView.Adapter<PrenotazioniCardAdapter.DataObjectHolder> {

    private static String TAG = "PrenotazioniCardAdap";

    private static Context context;

    private static ArrayList<PrenotazioniItem> items = new ArrayList<PrenotazioniItem>();
    private String tipoUtente;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView nome;
        TextView materia;
        TextView data;
        TextView ora;
        CircularImageView imageTutor;



        public DataObjectHolder(View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.prenot_nome);
            materia = (TextView) itemView.findViewById(R.id.prenot_materia);
            data = (TextView) itemView.findViewById(R.id.prenot_data);
            ora = (TextView) itemView.findViewById(R.id.prenot_ora);
            imageTutor = (CircularImageView) itemView.findViewById(R.id.prenot_tutor_image);


            itemView.setOnClickListener(this);
            Log.i(TAG, "Image view set");


        }

        @Override
        public void onClick(View v) {


            Intent intent = new Intent(context,PrenotazioniDettagliActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id",items.get(getAdapterPosition()).getId());
            intent.putExtras(bundle);

            context.startActivity(intent);


        }
    }

    public PrenotazioniCardAdapter(ArrayList<PrenotazioniItem> myDataset,String tipoUtente) {

        this.items = myDataset;
        this.tipoUtente = tipoUtente;
    }





    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row_prenotazione, parent, false);

        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        if (tipoUtente.equals("0")) {
            holder.nome.setText(items.get(position).getTutorNome() + " " + items.get(position).getTutorCognome());

            if (items.get(position).getTutorUrl().compareTo(" ") == 0) {
                holder.imageTutor.setImageResource(R.drawable.dummy_profpic);
            } else if (items.get(position).getTutorUrl().compareTo("face") == 0) {

                Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + items.get(position).getTutorIdfb() + "/picture"
                ).into(holder.imageTutor);


            } else {
                Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + items.get(position).getTutorUrl()
                ).into(holder.imageTutor);

            }

        } else {
            holder.nome.setText(items.get(position).getStudentNome() + " " + items.get(position).getStudentCognome().substring(0, 1) + ".");
            if (items.get(position).getStudentUrl().compareTo(" ") == 0) {
                holder.imageTutor.setImageResource(R.drawable.dummy_profpic);
            } else if (items.get(position).getStudentUrl().compareTo("face") == 0) {

                Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + items.get(position).getStudentIdfb() + "/picture"
                ).into(holder.imageTutor);


            } else {
                Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + items.get(position).getStudentUrl()
                ).into(holder.imageTutor);

            }
        }
        holder.materia.setText(items.get(position).getMateria());
        holder.data.setText(Functions.convertiData(items.get(position).getData()));
        holder.ora.setText(items.get(position).getOraInizio());
        if (items.get(position).isConfermato()) {
            holder.materia.setTextColor(context.getResources().getColor(R.color.green_prenotazione));

        } else {
            holder.materia.setTextColor(Color.BLUE);
        }




    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
