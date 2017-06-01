package com.example.waylon.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Waylon on 5/29/2017.
 */

public class HabitListView extends ListActivity {
    ArrayList<Habits> listHabits = new ArrayList<Habits>();

    ArrayAdapter<Habits> adapter;

    int numHabits;

    @Override
    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<Habits>(this, android.R.layout.simple_list_item_1, listHabits);
        setListAdapter(adapter);
    }

    public void addItems(View v, Habits item){
        listHabits.add(item);
        adapter.notifyDataSetChanged();
    }
}

