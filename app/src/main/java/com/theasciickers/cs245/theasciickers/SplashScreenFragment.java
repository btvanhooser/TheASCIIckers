package com.theasciickers.cs245.theasciickers;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alfredo on 11/28/2016.
 */


public class SplashScreenFragment extends AppCompatActivity{
    private static boolean alreadyShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alreadyShown = true;
        setContentView(R.layout.splash_screen);
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run()
            {
                finish();
            }
        };
        handler.postDelayed(r, 3000);
    }
    protected static boolean checkIfShown(){
        return alreadyShown;
    }

}

