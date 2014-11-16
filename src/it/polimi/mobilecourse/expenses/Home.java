package it.polimi.mobilecourse.expenses;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;



public class Home extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.top_fragment, new PlaceholderFragment())
                    .commit();
            getFragmentManager().beginTransaction()
                    .add(R.id.bottom_fragment, new BottomFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.top_fragment, container, false);

            RadioButton radio=(RadioButton)rootView.findViewById(R.id.radioButton);

            return rootView;
        }
    }

    public static class BottomFragment extends Fragment {

        public BottomFragment() {



        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View bottomView = inflater.inflate(R.layout.bottom_fragment, container, false);

            Button prova=(Button)bottomView.findViewById(R.id.button);


            return bottomView;
        }
    }


}
