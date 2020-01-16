package com.ballofknives.sqlmurdermystery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MurderMysteryFragment extends Fragment {
    private static final String TAG = "MurderMysteryFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_murder_mystery, container, false);

        final EditText queryText = (EditText)view.findViewById(R.id.queryEditText);
        queryText.setText("Enter your SQL query here...");
        final TextView queryOutput = (TextView)view.findViewById(R.id.queryOutput);
        queryOutput.setMovementMethod(new ScrollingMovementMethod());
        Button runQuery = (Button)view.findViewById(R.id.runQueryButton);
        runQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryString = queryText.getText().toString();
                Log.i(TAG, queryString);
                MainActivity m = (MainActivity)getActivity();

                if ( queryString.length() == 0){
                    queryOutput.setText("EMPTY QUERY PROVIDED");
                    return;
                }
                if(queryString.substring(queryString.length() - 1).equals(";")){
                    queryString = queryString.substring(0, queryString.length() - 1);
                }
                String[] queryStrings = queryString.split(";");

                for(String qs : queryStrings){
                    Log.e(TAG, "About to check string " + qs );
                    Cursor c = m.databaseHelper.rawQuery(qs);

                    if(c == null){
                        queryOutput.setText("NO RESULTS. POSSIBLE ILLEGAL QUERY.");
                        return;
                    }
                    if(c.moveToFirst()){
                        String fullString = "";
                        fullString += getColumnNames(c);
                        do{
                            final String rowString = makeRowString(c);
                            fullString += rowString;
                        } while( c.moveToNext());
                        queryOutput.setText(fullString);
                    } else {
                        queryOutput.setText("NO RESULTS");
                    }
                }
            }
        });

        return view;
    }

    private String makeRowString(Cursor c){
        String ret = "";

        int nCols = c.getColumnCount();
        for( int i = 0; i < nCols; ++i ){
            int colType = c.getType(i);
            try {
                switch (colType) {
                    case Cursor.FIELD_TYPE_FLOAT:
                        ret += String.valueOf(c.getFloat(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        ret += c.getString(i);
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        ret += new String(c.getBlob(i));
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        Log.i(TAG, "Found null column");
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        ret += String.valueOf(c.getInt(i));
                        break;
                    default:
                        break;
                }
            }
            catch (Exception e){
                Log.e(TAG, "Error reading column from DB", e);
            }
            ret += " ";
        }
        ret += "\n";
        return ret;
    }

    private String getColumnNames(Cursor c){
        String[] colNames = c.getColumnNames();
        String ret = "";
        for(String name : colNames){
            ret += (name + " ");
        }
        ret += "\n";
        return ret;
    }

}
