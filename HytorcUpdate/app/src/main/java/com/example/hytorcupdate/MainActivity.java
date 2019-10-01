package com.example.hytorcupdate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MainActivityTag";
    public static String SAVED_STRING_KEY = "KEY";
    public int index = 0;
    public static String[] myStrings = { "A","B", "C", "D", "E", "F", "G"};
    private TextView myTextView;
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTextView = findViewById(R.id.myTextView);
                                            index = (( index + 1 ) % myStrings.length);
                                            myTextView.setText(myStrings[index]);

                                            myButton = findViewById(R.id.myButton);
                                            myButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                            Intent i = new Intent(MainActivity.this, ResultActivity.class);
                                            i.putExtra(SAVED_STRING_KEY, 1);
                                            startActivityForResult(i, 0);;
                                        }
                                    }
        );
        if(savedInstanceState != null){
            index = savedInstanceState.getInt(SAVED_STRING_KEY);
            myTextView.setText(myStrings[index]);
        }
        else{
            myTextView.setText(myStrings[0]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, data.getStringExtra(ResultActivity.KEY).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STRING_KEY, index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
