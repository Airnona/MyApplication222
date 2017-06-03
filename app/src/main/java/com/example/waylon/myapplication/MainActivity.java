package com.example.waylon.myapplication;

import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Button button_editHabit;

    private DrawerLayout mDrawerLayout;         //Used for drawer stuff
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private static ListView habitList;          //Placeholder habit list, so is below
    private static String[] HABITS = new String[] {"Study Japaneseddddddddddd dddddddda ddddddddddddb", "Workout", "Relax"};

    private ArrayList<Habits> habitTestArray = new ArrayList<Habits>(); //testing for adding habits from Habits class

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

        MainController controller = new MainController();               //Testing for when opening edit intent, which habit we're editing
        controller.setHabit_inFocus_id(2);

        createHabitList();      //Creates an arraylist with some habits. HARDCODED
//        printTestArray();     //Prints some values to check they're being added to arraylist correctly

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
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
//                        Toast.makeText(getContext(), "Idk this is the edit button need to make window" + position, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, EditHabits.class);
                        intent.putExtra("Habit_ID", position);
                        startActivity(intent);
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


    private void createHabitList(){
        Habits test = new Habits(true, "Study Japanese", "Attempt to finish review every day!");
        Habits test2 = new Habits(false, "Excercise", "Run a mile each day");
        Habits test3 = new Habits(false, "Take time to relax", "Spend at least 15 minutes each day relaxing your mind");

        habitTestArray.add(test);
        habitTestArray.add(test2);
        habitTestArray.add(test3);
    }
    private void printTestArray(){
        Log.d("MyApp", habitTestArray.get(0).getName());
        Log.d("MyApp", habitTestArray.get(2).getName());
    }


}
