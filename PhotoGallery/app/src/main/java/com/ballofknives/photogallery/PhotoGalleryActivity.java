package com.ballofknives.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class PhotoGalleryActivity extends SingleFragmentActivity {
    private static final String TAG = "PhotoGalleryActivity";

    @Override
    public Fragment createFragment() {
        return new PhotoGalleryFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent); // not mentioned in the book
        PhotoGalleryFragment fragment = (PhotoGalleryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "Received new search query: " + query);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
                    .commit();
        }
        fragment.updateItems();
    }
}
