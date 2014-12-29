package it.polimi.mobilecourse.expenses;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class HomeTutor extends ActionBarActivity {

    public static final String TAG = "Money Notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setContentView(R.layout.fragment_home);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (savedInstanceState == null) {
            MidFragmentHome mf=new MidFragmentHome();
            getFragmentManager().beginTransaction().add(R.id.layoutreplace,new TopFragmentHome()).commit();
            getFragmentManager().beginTransaction().add(R.id.secondreplace,mf).commit();

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
        // automatically handle clicks on the HomeTutor/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
