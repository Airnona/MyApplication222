package com.example.waylon.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private static ListView habitList;
    private static String[] HABITS = new String[] {"Study Japanese", "Workout", "Relax"};


    //Please work commit 2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView habitListView = (ListView) findViewById(R.id.habitListView);
        habitListView.setAdapter(new MyHabitsListAdapter(this, R.layout.habitslayout2, HABITS));

//        ToggleButton b = (ToggleButton)findViewById(R.id.starButton);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);             //Toolbar for top of screen
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById((R.id.drawerLayout));   //Drawer slider code
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);                       //Drawer button code
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        listView();
    }

    public void listView(){
        habitList = (ListView)findViewById(R.id.habitListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.habitslayout, HABITS);
        habitList.setAdapter(adapter);
//        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String value = (String)habitList.getItemAtPosition(position);
//            }
//        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyHabitsListAdapter extends ArrayAdapter<String>{
        private int layout;



        public MyHabitsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.toggle = (ToggleButton) convertView.findViewById(R.id.habitLayoutToggle);
                viewHolder.text = (TextView) convertView.findViewById(R.id.habitLayoutText);
                viewHolder.button = (Button) convertView.findViewById(R.id.habitLayoutEditButton);
                viewHolder.button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(getContext(), "Idk this is the edit button need to make window", Toast.LENGTH_SHORT).show();
                    }
                });
                convertView.setTag(viewHolder);
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.text.setText(getItem(position));


            return convertView;
        }
    }

    public class ViewHolder {
        ToggleButton toggle;
        TextView text;
        Button button;
    }

}
