package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Matteo on 18/11/2014.
 */
public class Ripetizioni extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contView = inflater.inflate(R.layout.cont_fragment, container, false);


        return contView;
    }




    public static Fragment newInstance(){

        Ripetizioni nfg=new Ripetizioni();
        return nfg;

    }
}