package com.sample.appmojo.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appmojo.sdk.AppMojo;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sample.appmojo.R.layout.activity_main);

        AppMojo.start(
                getApplicationContext(),
                getString(com.sample.appmojo.R.string.app_id),
                getString(com.sample.appmojo.R.string.app_secret));

        List<String> listValues = new ArrayList<>();
        listValues.add(AMBannerActivity.class.getSimpleName());
        listValues.add(AMInterstitialActivity.class.getSimpleName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                com.sample.appmojo.R.layout.list_item, com.sample.appmojo.R.id.list_item, listValues);

        // assign the list adapter
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(getApplicationContext(), AMBannerActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getApplicationContext(), AMInterstitialActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}

