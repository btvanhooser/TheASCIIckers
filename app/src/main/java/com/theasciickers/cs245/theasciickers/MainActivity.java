package com.theasciickers.cs245.theasciickers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private static AudioPlayer player;
    private boolean musicOn;
    String INITIALHIGHSCORELIST = "4\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "6\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "8\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "10\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "12\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "14\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "16\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "18\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0\n" +
            "20\n" +
            "---,0\n" +
            "---,0\n" +
            "---,0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        if(!SplashScreenFragment.checkIfShown()){
            intent = new Intent(this, SplashScreenFragment.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        // Initialize application state
        if (!fileExists(this,"highscore.txt")){
            try {
                Log.d("Test","File Created");
                FileOutputStream fos = openFileOutput("highscore.txt", Context.MODE_PRIVATE);
                fos.write(INITIALHIGHSCORELIST.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("Test","File Exists");
        }

    if (savedInstanceState == null){
            // Start music
            player = new AudioPlayer();
            player.play(this);
            musicOn = true;

            // ---------------------set initial game fragment
            replaceFrag(GameFragment.class);
        }

        // Set toolbar to replace ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);

        navDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer);
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    private ActionBarDrawerToggle setupDrawerToggle(){
        // make sure you pass in valid toolbar ref. ActionBarDrawToggle()
        // doesn't require but will not render hamburger icon without it
        return new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
    }

    // Display hamburger icon
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem){
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    //
    public void selectDrawerItem(MenuItem menuItem){

        // Create new fragment and specify the fragment to show based on nav item choosen
        Class fragmentClass;

        switch(menuItem.getItemId()){
            case (R.id.nav_new_game):
                replaceFrag(GameFragment.class);
                setTitle("Concentration Game");
                break;
            case(R.id.nav_high_scores):
                replaceFrag(HighScoresFragment.class);
                setTitle(menuItem.getTitle());
                break;
            case(R.id.nav_volume):
                toggleMusic(menuItem);
                break;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // close navigation drawer
        drawerLayout.closeDrawers();
    }

    private void replaceFrag(Class fragmentClass){
        Fragment fragment = null;
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Replace existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleMusic(MenuItem volumeItem){
        System.out.println("togglemusic: " + musicOn);
        if(musicOn){
            musicOn = !musicOn;
            player.stop();
            volumeItem.setIcon(R.drawable.ic_volume_off_black_24dp);
            volumeItem.setTitle("Music Off");
        }
        else{
            player.play(this);
            musicOn = !musicOn;
            volumeItem.setTitle("Music On");
            volumeItem.setIcon(R.drawable.ic_volume_up_black_24dp);
        }

    }

    private void saveTextViewState(ViewGroup rootView, Bundle bundle){
        int numChildren = rootView.getChildCount();

        for(int i = 0; i < numChildren; i++){
            View v = rootView.getChildAt(i);

            if(v instanceof TextView){
                TextView text = (TextView)v;
                if(text.getText() != null){
                    bundle.putString("textview"+text.getId(),text.getText().toString());
                }
            }else if(v instanceof ViewGroup){
                saveTextViewState((ViewGroup)v,bundle);
            }
        }
    }

    private void loadTextViewState(ViewGroup rootView, Bundle bundle){
        int numChildren = rootView.getChildCount();

        for (int i = 0; i < numChildren; i++) {
            View v = rootView.getChildAt(i);

            if (v instanceof TextView){
                TextView text = (TextView)v;
                String saved = bundle.getString("textview"+text.getId());
                text.setText(saved);

            }else if (v instanceof ViewGroup){
                loadTextViewState((ViewGroup)v, bundle);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){

        // View related
        if(getTitle() == "High Scores"){
            View root = findViewById(R.id.high_scores_root);
            saveTextViewState((ViewGroup) root,outState);
        }

        //hamburger related
        outState.putBoolean("musicOn",musicOn);
        outState.putString("title",getTitle().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        if(getTitle() == "High Scores") {
            View root = findViewById(R.id.high_scores_root); //find your root view
            loadTextViewState((ViewGroup) root, savedInstanceState); //load state
        }
        Menu m = navDrawer.getMenu();
        MenuItem mItem = m.getItem(2);
        musicOn = savedInstanceState.getBoolean("musicOn");
        if(musicOn){
            mItem.setTitle("Music On");
            mItem.setIcon(R.drawable.ic_volume_up_black_24dp);
        }else{
            mItem.setTitle("Music Off");
            mItem.setIcon(R.drawable.ic_volume_off_black_24dp);
        }

        setTitle(savedInstanceState.getString("title"));
        super.onRestoreInstanceState(savedInstanceState);
    }

}
