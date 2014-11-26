package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import it.polimi.mobilecourse.expenses.data.Operation;
import it.polimi.mobilecourse.expenses.data.OperationOpenHelper;

/**
 * Created by Matteo on 22/11/2014.
 */
public class ProvaInsert extends Activity implements View.OnClickListener {


    private OperationOpenHelper oh=new OperationOpenHelper(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provainsert);
        final Button but=(Button)findViewById(R.id.button);
        but.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        Float fl= Float.valueOf(23);
        switch(v.getId()){

            case R.id.button:
                final EditText op=(EditText)findViewById(R.id.op);
                final EditText data=(EditText)findViewById(R.id.data);
                final EditText ty=(EditText)findViewById(R.id.type);
                final EditText val=(EditText)findViewById(R.id.val);

                Operation ooop=new Operation(data.getText().toString(),op.getText().toString(),fl,ty.getText().toString());
                oh.addOperation(ooop);
                Intent myint=new Intent(v.getContext(),Home.class);
                startActivity(myint);
                Context context = getApplicationContext();
                CharSequence text = "Operazione salvata correttamente!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
            default:break;


        }


    }


}