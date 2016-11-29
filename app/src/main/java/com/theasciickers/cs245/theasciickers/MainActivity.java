package com.theasciickers.cs245.theasciickers;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private static AudioPlayer player;
    private boolean musicOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start music
        player = new AudioPlayer();
        player.play(this);
        musicOn = true;

        // ---------------------set initial game fragment
        Class fragmentClass = GameFragment.class;
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
        //---------------------------------------

        // Set toolbar to replace ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);

        navDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer);
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
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()){
            case (R.id.nav_resume_game):
                fragmentClass = GameFragment.class;
                setTitle("Concentration Game");
                break;
            case (R.id.nav_new_game):
                fragmentClass = GameFragment.class;
                setTitle("Concentration Game");
                break;
            case(R.id.nav_end_game):
                fragmentClass = GameFragment.class;
                setTitle("Concentration Game");
                break;
            case(R.id.nav_high_scores):
                fragmentClass = HighScoresFragment.class;
                setTitle(menuItem.getTitle());
                break;
            case(R.id.nav_volume):
                fragmentClass = GameFragment.class;
                toggleMusic(menuItem);
                setTitle("Concentration Game");
                break;
            default:
                fragmentClass = GameFragment.class;
                setTitle("Concentration Game");
                break;
        }

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Replace existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // close navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleMusic(MenuItem volumeItem){
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

    @Override
    protected void onSaveInstanceState(Bundle state){
        state.putInt("musicPosn", player.getPosn());
        player.stop();
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        int posn = savedInstanceState.getInt("posn");
        player.seek(posn);
        super.onRestoreInstanceState(savedInstanceState);
    }

}
