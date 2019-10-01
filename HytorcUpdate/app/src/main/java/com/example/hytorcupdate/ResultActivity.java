package com.example.hytorcupdate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    public Button button;
    public static String KEY = "MyKey";

    private void setupReturnInformation(){
        Intent data = new Intent();
        data.putExtra(KEY, "Hello World");
        setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int number = getIntent().getIntExtra(MainActivity.SAVED_STRING_KEY, 0);
        Toast.makeText(this, "Received: "+ String.valueOf(number), Toast.LENGTH_LONG).show();

        button = findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setupReturnInformation();
                    }
                }
        );

    }
}
