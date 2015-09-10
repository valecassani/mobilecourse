package it.polimi.mobilecourse.expenses;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by valeriocassani on 09/09/15.
 */
public class RichiestaNotificaActivity extends AppCompatActivity {

    private SearchTutorDetails stDetails;
    private String idTutor;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.richiesta_notifica_activity);
        stDetails = new SearchTutorDetails();

        FragmentManager fragmentManager = getFragmentManager();
        Bundle receivedBundle = getIntent().getExtras();
        idTutor = receivedBundle.getString("tutor_id");

        Log.d("RichiestaNotActivity", idTutor);

        Bundle bundle = new Bundle();
        bundle.putString("idt",idTutor);
        bundle.putString("parent","notifica");
        stDetails.setArguments(bundle);


        fragmentManager.beginTransaction().addToBackStack("back").replace(R.id.fragreplace, stDetails).commit();



    }
}
