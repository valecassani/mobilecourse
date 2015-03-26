package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polimi.mobilecourse.expenses.data.Student;

/**
 * Created by Valerio on 30/12/2014.
 */
public class StudentData extends Fragment {


    private View view;
    private HelpActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_data_layout, container, true);

        String url="student_data.php";
        new RequestFtp().setParameters(activity, url,"userData" , StudentData.this).execute();
        Toast.makeText(getActivity().getApplicationContext(), "Dati Caricati", Toast.LENGTH_LONG).show();

        
        
        

        
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (HelpActivity) activity;
    }


    public void displayResults(ArrayList<ObjDb> result) {
        Student student = new Student(result);
        TextView name = (TextView) view.findViewById(R.id.username);
        name.setText(student.getName());

    }
   
}
