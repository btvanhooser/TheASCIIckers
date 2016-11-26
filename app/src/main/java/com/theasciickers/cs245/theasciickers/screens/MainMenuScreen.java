/**
 *
 */
package com.theasciickers.cs245.theasciickers.screens;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theasciickers.cs245.theasciickers.R;


public class MainMenuScreen extends Fragment {

    final Integer [] NUM_CARDS = {4,6,8,10,12,14,16,18,20};


    public static MainMenuScreen newInstance() {
        return new MainMenuScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_screen,container,false);
        ListView list =(ListView) view.findViewById(R.id.listview);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(list.getContext(),android.R.layout.simple_list_item_activated_1,NUM_CARDS);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                Log.e("" + NUM_CARDS[(int)itemId],"CLICKED");
            }
        });

        return view;
    }
}
