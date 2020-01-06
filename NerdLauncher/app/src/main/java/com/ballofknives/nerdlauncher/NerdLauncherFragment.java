package com.ballofknives.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NerdLauncherFragment extends ListFragment {

    private final String TAG = "NerdLauncherFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startupIntent = new Intent(Intent.ACTION_MAIN);

        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Log.i(TAG, "I've found " + activities.size() + " activities");

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo resolveInfo, ResolveInfo t1) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                    resolveInfo.loadLabel(pm).toString(),
                    t1.loadLabel(pm).toString()
                );
            }
        });

        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>( getActivity(), android.R.layout.simple_list_item_1, activities ) {
            public View getView(int pos, View convertView, ViewGroup parent){
                PackageManager pm = getActivity().getPackageManager();
                //View v = super.getView(pos, convertView, parent);
                if(convertView == null) {
                    Log.d(TAG, "convertView null");
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_app, null);
                }
                else {
                    Log.d(TAG, "convertView not null");
                }
                ResolveInfo ri = getItem(pos);
                TextView tv = (TextView)convertView.findViewById(R.id.app_name);
                tv.setText(ri.loadLabel(pm));
                ImageView image = (ImageView)convertView.findViewById(R.id.app_icon);
                Drawable drawable = ri.loadIcon(pm);
                Log.d(TAG, drawable.toString());
                image.setImageDrawable(drawable);
                return convertView;
            }
        };

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        if(activityInfo == null) return;
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
