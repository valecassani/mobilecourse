package it.polimi.mobilecourse.expenses.data;

import android.provider.ContactsContract;
import android.text.format.DateFormat;

import java.sql.Time;

/**
 * Created by Matteo on 21/11/2014.
 */


public class Operation {

    private String data;
    private String op;
    private float value;
    private String type;


    public Operation(String data, String op, float value,String type) {
        this.data=data;
        this.op=op;
        this.value=value;
        this.type=type;

    }


    public String getData(){
        return this.data;
    }

    public String getOp(){
        return this.op;
    }

    public float getValue(){
        return this.value;
    }

    public String getType(){
        return this.type;
    }

}
