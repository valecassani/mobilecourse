package it.polimi.mobilecourse.expenses;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import it.polimi.mobilecourse.expenses.data.Operation;
import it.polimi.mobilecourse.expenses.data.OperationOpenHelper;

/**
 * Created by Matteo on 22/11/2014.
 */
public class ProvaShowResult extends Activity {

    private OperationOpenHelper db=new OperationOpenHelper(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provashowresult);
        Operation result=db.getOperation(1);
        String op=result.getOp();
        EditText testo=(EditText)findViewById(R.id.testo);
        testo.setText(op);
        Operation resultdue=db.getOperation(3);
        String opdue=resultdue.getOp();
        EditText testodue=(EditText)findViewById(R.id.testodue);
        testodue.setText(opdue);




    }
}