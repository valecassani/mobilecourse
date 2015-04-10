package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Matteo on 27/03/2015.
 */
public class WelcomeFragment extends Fragment {

    private View view;
    private LandingActivity activity;
    TextView welcome;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.welcome_fragment, container, false);
        welcome=(TextView)view.findViewById(R.id.welcomestring);
        System.out.println("Fragment set");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (LandingActivity) activity;
    }

    public void setText(String string){

        welcome.setText("Bentornato "+string+"!");


    }
}
