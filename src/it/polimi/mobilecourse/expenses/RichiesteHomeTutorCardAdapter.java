package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Valerio on 16/09/2015.
 */
public class RichiesteHomeTutorCardAdapter extends RecyclerView.Adapter<RichiesteHomeTutorCardAdapter.DataObjectHolder>{

    private static String TAG = "RichiesteCardAdapter";

    private static Context context;

    private static ArrayList<ListRichiesteItem> items = new ArrayList<ListRichiesteItem>();
    private String tipoUtente;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView nome;
        TextView titolo;
        TextView data;
        CircularImageView img;



        public DataObjectHolder(View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.richiesta_nome);
            data = (TextView) itemView.findViewById(R.id.data_entro);
            titolo = (TextView) itemView.findViewById(R.id.titolo_richiesta);
            img = (CircularImageView) itemView.findViewById(R.id.richiesta_image);

            itemView.setOnClickListener(this);
            Log.i(TAG, "Image view set");


        }

        @Override
        public void onClick(View v) {

            FragmentManager fragmentManager = ((Activity)context).getFragmentManager();

            Fragment fragment = new MostraRichiestaFragment();
            Bundle bundle = new Bundle();
            bundle.putString("idr", items.get(getAdapterPosition()).getId());
            fragment.setArguments(bundle);
            System.out.println("Bundle" + bundle);
            fragmentManager.beginTransaction().replace(R.id.tutor_fragment,fragment).addToBackStack(null).commit();


        }




    }

    public RichiesteHomeTutorCardAdapter(ArrayList<ListRichiesteItem> myDataset,String tipoUtente) {

        this.items = myDataset;
        this.tipoUtente = tipoUtente;
    }





    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row_hometutor_richiesta, parent, false);

        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.nome.setText(items.get(position).getNome() + " " + items.get(position).getCognome().substring(0, 1) + ".");
        holder.titolo.setText(items.get(position).getTitolo());
        holder.data.setText("Entro il "+Functions.convertiData(items.get(position).getData_entro().substring(0,10)));

        if (items.get(position).getUrl().compareTo(" ") == 0) {
            holder.img.setImageResource(R.drawable.dummy_profpic);
        } else if (items.get(position).getUrl().compareTo("face") == 0) {

            System.out.println(items.get(position).getIdfb());

            Picasso.with(context.getApplicationContext()).load("https://graph.facebook.com/" + items.get(position).getIdfb() + "/picture"
            ).into(holder.img);



        } else {
            Picasso.with(context.getApplicationContext()).load("http://www.unishare.it/tutored/" + items.get(position).getUrl()
            ).into(holder.img);

        }









    }

    public void startItemDetails(int position) {


    }

    public void removeItem(final int position) {

        ListRichiesteItem item = items.get(position);
        Log.d(TAG,"ID to be deleted " + item.getId());
        String delete_url = "http://www.unishare.it/tutored/delete_prenotazione.php?id="
                + item.getId();
        RequestQueue queue1 = Volley.newRequestQueue(context);
        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public boolean onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        Toast.makeText(context,
                                "Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);

                    } else {
                        Toast.makeText(context,
                                "failed to delete", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Errore di connessione",Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(delete_request);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
