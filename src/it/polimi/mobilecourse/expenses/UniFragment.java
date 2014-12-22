package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matteo on 21/12/2014.
 */
public class UniFragment extends Fragment {

    private View view;
    private ProvaUni activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dblayout, container, false);


        Button im=(Button) view.findViewById(R.id.buttonDB);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestFtp().setParameters(activity,"univer.php",UniFragment.this).execute();

            }
        });



        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ProvaUni) activity;
    }

    public void displayResults(ArrayList<Entity> result) {
        Entity user = result.get(0);
        TextView textView = (TextView) view.findViewById(R.id.textViewdb);
        textView.setText(user.get("nome"));
    }


}