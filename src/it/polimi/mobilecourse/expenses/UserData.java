package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Valerio on 30/12/2014.
 */
public class UserData extends Fragment {


    private View view;
    private HelpActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_data_layout, container, true);
        TextView name = (TextView) view.findViewById(R.id.userData);
        String url="user_data.php";
        new RequestFtp().setParameters(activity, url,"userData" , UserData.this).execute();
        Toast.makeText(getActivity().getApplicationContext(), "Dati Caricati", Toast.LENGTH_LONG).show();
        name.setText("Valerio");
        
        
        

        
        return view;
    }
    
    public void setActivity(HelpActivity activity) {
        this.activity = activity;
        
    }
   
}
