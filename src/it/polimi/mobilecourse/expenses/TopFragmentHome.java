package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;


/**
 * Created by Matteo on 16/11/2014.
 */
public class TopFragmentHome extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.top_fragment, container, false);

        ImageView plusButton=(ImageView) rootView.findViewById(R.id.imageView);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent=new Intent(view.getContext(),NewOperation.class);
                startActivity(myintent);
            }
        });

        ImageView linkbar=(ImageView)rootView.findViewById(R.id.linkbar);
        linkbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent inet=new Intent(v.getContext(),NavigationDrawer.class);
                startActivity(inet);
            }


        });


        return rootView;






    }
}