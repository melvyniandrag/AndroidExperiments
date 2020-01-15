package com.ballofknives.sqlmurdermystery;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String TAG = "DatabaseHelper";
    String DB_PATH = null;
    private static String DB_NAME = "sql-murder-mystery.db";
    private SQLiteDatabase database;
    private final Context context;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
        this.context= context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.e("Path 1", DB_PATH);
    }

    public void createDatabase() throws  IOException {
        boolean exists = checkDatabase();
        if(exists){
           Log.i(TAG, "Database exists");
        }else{
            this.getReadableDatabase();
            try{
                copyDatabase();
            } catch (IOException e){
                Log.e(TAG, "Error", e);
                throw new Error("Error copying database!");
            }
        }
    }

    private boolean checkDatabase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e ){
            Log.e(TAG, "Error in checkdatabase", e);
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDatabase() throws IOException{
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer, 0, length);
            Log.i(TAG, buffer.toString());
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(database != null){
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 > i){
            try{
                copyDatabase();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return database.query("interview", null, null, null, null, null, null);
    };

    public Cursor rawQuery(String queryString){
        try{
            return database.rawQuery(queryString, null, null);
        } catch ( Exception e ){
            return null;
        }

    }
}
