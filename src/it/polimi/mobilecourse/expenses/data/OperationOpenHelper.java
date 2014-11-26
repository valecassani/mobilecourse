package it.polimi.mobilecourse.expenses.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Matteo on 21/11/2014.
 */
public class OperationOpenHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "operazioni";
    public static final int TABLE_VERSION = 1;
    public static final String ID = "id";
    public static final String OP="op";
    public static final String VALUE = "value";
    public static final String TYPE="type";
    public static final String DATA = "data";


    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + OP + " TEXT, " + VALUE + " FLOAT," + TYPE + " TEXT, "+ DATA +" TEXT);";


    public OperationOpenHelper(Context context) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(TABLE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(arg0);
    }


    public void addOperation(Operation operation){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues val=new ContentValues();
        val.put(OP, operation.getOp());
        val.put(VALUE,operation.getValue());
        val.put(TYPE,operation.getType());
        val.put(DATA, String.valueOf(operation.getData()));

        db.insert(TABLE_NAME,null,val);
        db.close();
    }
    public Operation getOperation(long id){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,new String[]{ID,OP,VALUE,TYPE,DATA},ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();

        Operation oper=new Operation(cursor.getString(0),cursor.getString(1),cursor.getFloat(2),cursor.getString(3));
        return oper;


    }
}
