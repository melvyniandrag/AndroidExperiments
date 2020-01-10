package com.ballofknives.photogallery;

import androidx.fragment.app.Fragment;

public class PhotoPageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PhotoPageFragment();
    }
}
