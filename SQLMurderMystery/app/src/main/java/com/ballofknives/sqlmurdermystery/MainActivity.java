package com.ballofknives.sqlmurdermystery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends SingleFragmentActivity {
    public Cursor c = null;
    public DatabaseHelper databaseHelper;
    private static final String TAG = "MainActivity";

    @Override
    protected Fragment createFragment() {
        return new MurderMysteryFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        try{
            databaseHelper.createDatabase();
        } catch ( IOException ioe ){
            throw new Error("Unable to create db!");
        }
        try{
            databaseHelper.openDataBase();
        } catch (SQLException sqle ){
            throw sqle;
        }

        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

        c = databaseHelper.query("interview", null, null, null, null, null, null);
        if(c.moveToFirst()){
                Log.i(TAG,
                        "id: " + c.getString(0) + " thing: " + c.getString(1));
        } else {
            Log.e(TAG, "Cant move to first");
        }

    }
}
