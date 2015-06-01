package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Valerio on 15/05/2015.
 */
public class HomeStudentFragment extends Fragment {
    private CardView cardView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);


        return view;
    }
}
