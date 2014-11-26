package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Matteo on 17/11/2014.
 */
public class MidFragmentHome extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View midView = inflater.inflate(R.layout.mid_fragment, container, false);

        TextView conto=(TextView)midView.findViewById(R.id.myconto);
        conto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fg = new ContoFragment();
                Fragment sm= new SecondMid();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.layoutreplace, fg);
                transaction.replace(R.id.secondreplace,sm);
                transaction.addToBackStack(null);
                transaction.commit();
                /*FragmentTransaction transa = getFragmentManager().beginTransaction();
                transa.replace(R.id.secondreplace,sm);
                transa.addToBackStack(null);
                transa.commit();
*/
            }
        });


        return midView;
    }


}