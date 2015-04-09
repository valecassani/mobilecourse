package it.polimi.mobilecourse.expenses.tutor;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polimi.mobilecourse.expenses.HelpActivity;
import it.polimi.mobilecourse.expenses.ObjDb;
import it.polimi.mobilecourse.expenses.R;
import it.polimi.mobilecourse.expenses.RequestFtp;
import it.polimi.mobilecourse.expenses.data.Student;
import it.polimi.mobilecourse.expenses.data.Tutor;

/**
 * Created by valeriocassani on 04/01/15.
 */
public class TutorData extends Fragment {


    private View view;
    private HelpActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.student_data_fragment, container, true);

        String url="tutor_data.php";

        new RequestFtp().setParameters(activity, url,"prova" , TutorData.this).execute();
        Toast.makeText(getActivity().getApplicationContext(), "Dati Caricati", Toast.LENGTH_LONG).show();






        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (HelpActivity) activity;
    }


    public void displayResults(ArrayList<ObjDb> result) {
        Tutor tutor = new Tutor(result);
        TextView name = (TextView) view.findViewById(R.id.username);
        name.setText(tutor.getName());
    }

}
