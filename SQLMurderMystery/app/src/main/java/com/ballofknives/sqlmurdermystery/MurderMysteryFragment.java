package com.ballofknives.sqlmurdermystery;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MurderMysteryFragment extends Fragment {
    private static final String TAG = "MurderMysteryFragment";
    private static final int MAX_RESULTS = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_murder_mystery, container, false);

        final EditText queryText = (EditText)view.findViewById(R.id.queryEditText);
        queryText.setText("select * from person limit 100");
        final TableLayout tl = (TableLayout)view.findViewById(R.id.queryOutputTable);

        final Button runQuery = (Button)view.findViewById(R.id.runQueryButton);
        runQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runQuery.setEnabled(false);
                runQuery.setClickable(false);
                tl.removeAllViews();

                String queryString = queryText.getText().toString();
                Log.i(TAG, queryString);
                MainActivity m = (MainActivity)getActivity();

                if ( queryString.length() == 0){
                    runQuery.setEnabled(true);
                    runQuery.setClickable(true);
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
                        reportQueryError("NO RESULTS. POSSIBLE ILLEGAL QUERY.");
                        runQuery.setEnabled(true);
                        runQuery.setClickable(true);
                        return;
                    }
                    if(c.getCount() > MAX_RESULTS){
                        reportQueryError("Too many results in query. Limit query results using a more specific query, e.g. using a WHERE or LIMIT statement.");
                        runQuery.setEnabled(true);
                        runQuery.setClickable(true);
                        return;
                    }
                    if(c.moveToFirst()){
                        addTableHeader(tl, c);
                        do{
                            addTableRow(tl, c);
                        } while( c.moveToNext());
                    } else {
                        reportQueryError("NO RESULTS");
                    }
                }
                runQuery.setEnabled(true);
                runQuery.setClickable(true);
            }
        });

        return view;
    }

    private void reportQueryError(String s){
        AlertDialog.Builder queryErrorDialog = new AlertDialog.Builder(this.getActivity());
        queryErrorDialog.setTitle("Query Error");
        queryErrorDialog.setMessage(s);
        queryErrorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        queryErrorDialog.show();
    }

    private void addTableHeader(TableLayout tl, Cursor c){
        final String[] colNames = c.getColumnNames();
        TableRow tr = new TableRow(tl.getContext());

        for( String cn : colNames ){
            TextView tv = new TextView(tl.getContext());
            tv.setText(cn);
            TableRow.LayoutParams tableRowParams=
                    new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);

            tableRowParams.setMargins(10, 0, 10, 1);
            tv.setLayoutParams(tableRowParams);
            tr.addView(tv);
        }
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    private void addTableRow(TableLayout tl, Cursor c){
        TableRow tr = new TableRow(tl.getContext());

        int nCols = c.getColumnCount();
        for( int i = 0; i < nCols; ++i ){
            TextView tv = new TextView(tl.getContext());
            int colType = c.getType(i);
            switch (colType) {
                case Cursor.FIELD_TYPE_FLOAT:
                    tv.setText(String.valueOf(c.getFloat(i)));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    String rawData = c.getString(i);
                    String data = rawData.replace("\n", "").replace("\r", "").replace("\r\n", "");
                    tv.setText(data);
                    break;
                case Cursor.FIELD_TYPE_BLOB:
                    tv.setText(new String(c.getBlob(i)));
                    break;
                case Cursor.FIELD_TYPE_NULL:
                    Log.i(TAG, "Found null column");
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    tv.setText(String.valueOf(c.getInt(i)));
                    break;
                default:
                    break;
            }
            TableRow.LayoutParams tableRowParams=
                    new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);

            tableRowParams.setMargins(10, 0, 10, 10);
            tv.setLayoutParams(tableRowParams);
            tr.addView(tv);
        }
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

}
