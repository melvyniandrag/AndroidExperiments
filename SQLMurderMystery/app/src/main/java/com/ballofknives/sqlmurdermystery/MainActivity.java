package com.ballofknives.sqlmurdermystery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        AlertDialog.Builder startupPrompt = new AlertDialog.Builder(this);
        startupPrompt.setTitle("Hello Detective!");
        startupPrompt.setMessage("A crime has taken place and the detective needs your help. The detective gave you the crime scene report, but you somehow lost it. You vaguely remember that the crime was a murder that occurred sometime on Jan.15, 2018 and that it took place in SQL City. Start by retrieving the corresponding crime scene report from the police department’s database.");
        startupPrompt.setPositiveButton("I'm on the case!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        startupPrompt.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mystery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.checkSolution_menu_item:
                AlertDialog.Builder solutionDialog = new AlertDialog.Builder(this);
                solutionDialog.setTitle("How to Check Solution");
                solutionDialog.setMessage("To check your solution run the following query:\n" +
                        "INSERT INTO solution VALUES (1, 'Insert the suspect\'s name here');\n" +
                        "SELECT value FROM solution;"
                        );
                solutionDialog.setPositiveButton("Got it.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // do nothing
                    }
                });
                solutionDialog.setNegativeButton("Copy query to clipboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label",
                                "INSERT INTO solution VALUES (1, 'Insert name here'); " +
                                "SELECT value FROM solution;");
                        clipboard.setPrimaryClip(clip);
                    }
                });

                solutionDialog.show();
                return true;
            case R.id.aboutApp_menu_item:
                return true;
            case R.id.help_menu_item:
                return true;
            case R.id.showSchema_menu_item:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
