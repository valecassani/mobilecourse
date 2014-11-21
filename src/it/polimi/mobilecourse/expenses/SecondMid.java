package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Matteo on 20/11/2014.
 */
public class SecondMid extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View secondView = inflater.inflate(R.layout.second_mid, container, false);

        TextView saldo=(TextView)secondView.findViewById(R.id.resume);
        saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TopFragmentHome top=new TopFragmentHome();
                MidFragmentHome mid=new MidFragmentHome();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_left);
                transaction.replace(R.id.layoutreplace, top);
                transaction.addToBackStack(null);
                transaction.commit();
                FragmentTransaction transdue = getFragmentManager().beginTransaction();
                transdue.replace(R.id.secondreplace, mid);
                transdue.addToBackStack(null);
                transdue.commit();

            }
        });




        return secondView;
    }
}