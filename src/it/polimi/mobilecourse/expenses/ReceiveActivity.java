package it.polimi.mobilecourse.expenses;
/**
 * Created by Matteo on 30/05/2015.
 */
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

//activity dopo il click sulla notifica

public class ReceiveActivity extends Activity {

    TextView type;

    JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        Intent intent = getIntent();

        type = (TextView) findViewById(R.id.name);

        String message = intent.getExtras().getString("message");
        try {
            json = new JSONObject(message);
            //tipo di notifica cliccata(il tipo e una stringa)
            String notif = json.getString("notif");
            //viene mostrato il tipo di notifica in una textview...si puo togliere
            type.setText(notif);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        //idealmente da qui in poi si chiamano i fragment in base al tipo di notifica


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.receive, menu);
        return true;
    }

}