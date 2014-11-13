package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by Matteo on 26/10/2014.
 */
public class LoginActivity  extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }


}
