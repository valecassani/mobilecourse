package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TabHost;

/**
 * Created by Matteo on 04/09/2015.
 */
public class PrenotazioniDettagliFragment extends Fragment {

    private View view;
    private Context context;

    private TabHost th;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.prenotazione_dettagli_fragment, container, false);

        context = view.getContext();


        th = (TabHost) view.findViewById(R.id.tabHostP);
        th.setup();
        TabHost.TabSpec ts = th.newTabSpec("Dettagli");
        ts.setContent(R.id.ll);
        ts.setIndicator("Dettagli");
        th.addTab(ts);

        ts = th.newTabSpec("Mappa");
        ts.setContent(R.id.ll2);
        ts.setIndicator("Mappa");

        th.addTab(ts);


        return view;
    }



}
