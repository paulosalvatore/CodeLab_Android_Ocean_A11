package br.com.paulosalvatore.codelab_android_ocean_a11;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by paulo on 01/05/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, "codelab_a11_db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE compromissos (id integer PRIMARY KEY, titulo varchar(255), latitude double, longitude double, data varchar(255));");
        }
        catch (Exception e) {
            Log.e("BANCO_DADOS", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
