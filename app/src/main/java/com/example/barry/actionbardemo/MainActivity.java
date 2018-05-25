package com.example.barry.actionbardemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;


//Create actionView button to handle search. res/layout/action_view_button.xml
// connect in menu, then call in main.java with onPrepareOptionsmenu()

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button betterDays = (Button) findViewById(R.id.betterDaysbtn);
        betterDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent betterDaysIntent = new Intent(MainActivity.this, BetterDaysActivity.class);
                startActivity(betterDaysIntent);
            }
        });

        //1. get the Action bar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar()
        getSupportActionBar().setTitle("Zini ActionBar"); // set the title
        String title = actionBar.getTitle().toString(); // get the title
       // actionBar.hide(); // to hide the actionbar

        //2. To display the Actionbar icon, (discouraged in Material Design)
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Display custom actionBar, this works with our custom actionbar_title.xml
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Above shows just title, below will display title + Home
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);


        // set the home icon as up so that user can go Home
        // Enable Up icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //3. populate the actionBar with onCreateOptionsMenu() method
    // see a 2nd one below
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Infalte the menu; this adds items to the bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu); // create menu_main in res/menu
        return true;
    }
*/
    //4. Add onComposeAction to handle clicks
    public void onComposeAction(MenuItem mi) {
        // handle click here
    }
    //4. 2nd approach, Add onOptionsItemSelected() to handle clicks
    // Using MenuItem passed, we can identify the action by calling getItemId()
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the aaction bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            case  R.id.miProfile:
                showProfileView();
                return true;
            //case R.layout.activity_better_days;

            //This is the Home or Up button
            // Do this then configure the manifest with <meta data>
            case  android.R.id.home:
                // Respond to the action bar's Up/Home button
                //a) If Up is from outside this app
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else { //b)
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                //NavUtils.navigateUpFromSameTask(this); repeated
                // override PendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;

            // handle search cases
            /* Giving me nullpointer error
            case R.id.action_search:
                searchView.setIconified(false);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void composeMessage() {
        // What to do
    }

    private void showProfileView() {
// What to do

    }

    // Connect the button with onPrepareOptionsMenu()
    //@Override // override is red lining
    public boolean onPrePareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.miActionButton);
        // Retrieve the action-view from menu
        View v = MenuItemCompat.getActionView(actionViewItem);
        // find the button within the action-view
        Button b = (Button) v.findViewById(R.id.btnCustomAction);
        // handle button click
        return super.onPrepareOptionsMenu(menu);
    }

    // add searchView to actionBar, add a searchView to the menu first
    // Then hookup a listener for the search action
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Handle search
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // onPrePareOptionsMenu(menu); I added this but it does nothing, needs fix
                //Toast.makeText(this, query, Toast.LENGTH_LONG).show();

                // work around to avoid issues with some emulators and keyboard devices
                // firing twice if a keyboard enter i
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Handle the share Providers option
        // 1. call getActionProvider() and pass the shareAction's MenuItem
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //2. Create the ACTION_SEND and attach thw content shared by activity
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("image/*");
        Uri myImageUri = Uri.fromFile(new File(getFilesDir(), "foo.jpg")); // added from dev shareIntent
        myShareIntent.putExtra(Intent.EXTRA_STREAM, myImageUri);
        //3. Call setShareIntent() to attach this intent to the action provider
        myShareActionProvider.setShareIntent(myShareIntent);
        //4. When the content changes, modify the intent or create a new one
        // and call setShareIntent()
        // Image has changed! Update the intent
        Uri myNewImageUri = Uri.fromFile(new File(getFilesDir(), "foo.jpg")); // adapted from myImageUri
        myShareIntent.putExtra(Intent.EXTRA_STREAM, myNewImageUri);
        myShareIntent.putExtra(Intent.EXTRA_TEXT, "Check This App Out");
        myShareActionProvider.setShareIntent(myShareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    /*
    // Expand searchView anytime calling expandActionView()
    // We can add this to our onCreate above to avoid duplication and error
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // ... lookup the search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Expand the search view and request focus
        searchItem.expandActionView();
        searchView.requestFocus();
        //return true; // I was having error but this solved it
    }
    // We can also customize search icon or text color, or extend the SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // ... lookup the search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Use a custom search icon for the SearchView in AppBar
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.search_btn);
        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.BLACK);
        et.setHintTextColor(Color.BLACK);
    }
*/

    // https://guides.codepath.com/android/Defining-The-ActionBar#actionbar-basics
    // https://developer.android.com/training/appbar/setting-up.html
}
