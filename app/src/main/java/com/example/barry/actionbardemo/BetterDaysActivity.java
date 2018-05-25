package com.example.barry.actionbardemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BetterDaysActivity extends AppCompatActivity {

    public static final String PACKAGE_NAME = "com.myapplication.";
    public static final String PARENT_NAME_EXTRA = "ParentClassName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_better_days);

        // set the home icon as up so that user can go Home
        // Enable Up icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Dynamically move Up to parent activity instead of using the Manifest
    @Override
    public Intent getSupportParentActivityIntent() {
        // Extract the class name of our parent
        Intent parentIntent = getIntent();
        String className = parentIntent.getStringExtra(PARENT_NAME_EXTRA);
        // Create intent based on the parent class name
        Intent newIntent = null;
        try {
            //you need to define the class with package name
            newIntent = new Intent(this, Class.forName(PACKAGE_NAME + className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Return the created intent as the "up" activity
        return newIntent;

        //Intent intent = new Intent(this, ChildActivity.class);
        //intent.putExtra(ChildActivity.PARENT_NAME_EXTRA, "ParentActivity");

        //Intent intent = new Intent(this, BetterDaysActivity.class);
        //intent.putExtra(BetterDaysActivity.PARENT_NAME_EXTRA, "ParentActivity");
        //startActivity(intent);
    }
}
