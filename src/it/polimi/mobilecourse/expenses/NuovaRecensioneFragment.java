package it.polimi.mobilecourse.expenses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Matteo on 04/08/2015.
 */
public class NuovaRecensioneFragment extends Fragment {

    NuovaRecensione activity;
    private View view;

    //prova commit
    ProgressBar progressView;
    String idstudente;
    String idtutor;
    TextView name;
    RatingBar disp;
    RatingBar puntual;
    RatingBar finale;
    RatingBar chiar;
    EditText commento;
    Button addRec;
    String nome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.nuova_recensione_fragment, container, false);
        Bundle get=getArguments();
        idstudente=get.getString("idstudente");
        idtutor=get.getString("idtutor");
        nome=get.getString("nome");
        field();



        addRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addRec.setVisibility(View.GONE);
                progress(true);
                saveMarks();
            }
        });



        return view;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        this.activity = (NuovaRecensione) activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstancestate) {
        super.onActivityCreated(savedInstancestate);

    }







    private void saveMarks(){

        String url="nuova_recensione.php?idstudente=".concat(idstudente).concat("&idtutor=").concat(idtutor).concat("&puntual=")
                .concat(String.valueOf(puntual.getRating())).concat("&disp=").concat(String.valueOf(disp.getRating())).
                concat("&chiar=").concat(String.valueOf(chiar.getRating())).concat("&finale=")
                .concat(String.valueOf(finale.getRating())).concat("&commento=").concat(commento.getText().toString().replace(" ", "%20"));

        new RequestFtp().setParameters(activity, url, "saveMarks", NuovaRecensioneFragment.this).execute();
        progress(false);
        addRec.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity().getApplicationContext(), "Recensione inserita", Toast.LENGTH_LONG).show();


    }

    private void progress(final boolean show){
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });



    }

    private void field(){

        float ratingstep=0.5f;
        progressView=(ProgressBar) view.findViewById(R.id.progressBar);

        name=(TextView)view.findViewById(R.id.textNome);
        name.setText("Qui puoi valutare il tutor " + nome);

        chiar=(RatingBar)view.findViewById(R.id.chiar);
        chiar.setStepSize(ratingstep);
        puntual=(RatingBar)view.findViewById(R.id.puntual);
        puntual.setStepSize(ratingstep);
        disp=(RatingBar)view.findViewById(R.id.disp);
        disp.setStepSize(ratingstep);
        finale=(RatingBar)view.findViewById(R.id.finale);
        finale.setStepSize(ratingstep);

        commento=(EditText)view.findViewById(R.id.comment);

        addRec=(Button)view.findViewById(R.id.addRec);







    }

}
