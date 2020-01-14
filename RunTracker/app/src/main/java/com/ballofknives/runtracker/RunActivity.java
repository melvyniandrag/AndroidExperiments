package com.ballofknives.runtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.location.LocationManager;
import android.os.Bundle;

public class RunActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RunFragment();
    }
}
