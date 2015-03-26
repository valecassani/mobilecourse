package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Matteo on 17/11/2014.
 */
public class NewOperation extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_operation);
        ImageView back=(ImageView)findViewById(R.id.imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent(view.getContext(), HomeStudent2.class);
                startActivity(myintent);
            }
        });

    }

}