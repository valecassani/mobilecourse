package it.polimi.mobilecourse.expenses;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.top_fragment, new TopFragmentHome())
                    .commit();
            getFragmentManager().beginTransaction()
                    .add(R.id.bottom_fragment, new BottomFragmentHome())
                    .commit();
            ListView lista=(ListView)findViewById(R.id.listAction);
            String [] actions=new String[]{"controlla conto"};
            ArrayList<String> azioni=new ArrayList<String>();
            azioni.addAll(Arrays.asList(actions));
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.simplerow,azioni);

            lista.setAdapter(arrayAdapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
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






}
