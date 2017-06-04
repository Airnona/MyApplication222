package com.example.waylon.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int numHabits;

//    private static Button button_editHabit;

    private DrawerLayout mDrawerLayout;                                                             //Used for drawer stuff
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

//    private static String[] HABITS = new String[] {"Study Japaneseddddddddddd a", "Workout", "Relax"}; //Placeholder habit list, so is below

    public Habit habit = new Habit();
//    public Habit testHabit = new Habit(false, "Testname", "Testdesc");

    SharedPreferences mPrefs;
    public ArrayList<Habit> habitArrayList = new ArrayList<Habit>();
    private ArrayList<Habit> habitTestArray = new ArrayList<Habit>();//testing for adding habits from Habit class

    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                         //BASIC NEEDS
        setContentView(R.layout.activity_main);                                                     //BASIC NEEDS

//        mPrefs = getPreferences(MODE_PRIVATE);
        mPrefs = getSharedPreferences("EditHabits", 0);

//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(testHabit);
//        prefsEditor.putString(testHabit.getName(), json);
//        prefsEditor.commit();


        json = mPrefs.getString("Testname", "");
        Log.d("TESTING3333", "aaa"+json);


        createHabitList();
//        generateHabitArrayList();

        ListView habitListView = (ListView) findViewById(R.id.habitListView);
        habitListView.setAdapter(new MyHabitsListAdapter(this, R.layout.habitslayout2, habitTestArray));    //Makes the list view work

        mToolbar = (Toolbar) findViewById(R.id.nav_action);                                         //Toolbar for top of screen
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById((R.id.drawerLayout));                           //Drawer slider code
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);                                                   //Drawer button code
        mToggle.syncState();

        MainController controller = new MainController();                                           //Testing for when opening edit intent, which habit we're editing
        controller.setHabit_inFocus_id(2);

//        String json = mPrefs.getString("Testname", "");
//        Log.d("TESTING", json);
                                                                                 //Creates an arraylist with some habits. HARDCODED
//        printTestArray();                                                                         //Prints some values to check they're being added to arraylist correctly

        printHabitArray();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);                                      //Creates the Drawer Slider button (Top left)
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Testing", "onRestart Called");

        printHabitArray();
        generateHabitArrayList();
        printHabitArray();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.e("Testing", "onRestart Called");
//    }

    public void addHabit(View view){
        Intent intent = new Intent(MainActivity.this, EditHabits.class);
        intent.putExtra("Habit_ID", habitArrayList.size());
        intent.putExtra("Habit_New", true);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class MyHabitsListAdapter extends ArrayAdapter<Habit>{
        private int layout;


        public MyHabitsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Habit> objects) {
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
                        Log.e("Position", "" + position);
                        intent.putExtra("Habit_New", false);
                        startActivity(intent);
                    }
                });
                convertView.setTag(viewHolder);
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
//            mainViewHolder.text.setText(getItem(position));
            mainViewHolder.text.setText(habitTestArray.get(position).getName());


            return convertView;
        }
    }
    public class ViewHolder {
        ToggleButton toggle;
        TextView text;
        Button button;
    }


    private void generateHabitArrayList(){
        for(Integer i = -1; i < 10; i++){
            Gson gson = new Gson();
            try {
//                Log.e("Made it to Try", "IN TRY");
//                String json = mPrefs.getString(i.toString(), "");
                String json = mPrefs.getString("0", "");

//                Log.e("Made it to SECOND", "SECOND");
                Habit habit3 = gson.fromJson(json, Habit.class);
                Habit habit2 = new Habit(habit3);


                Log.e("Object", habit2.getName() + "  " + habit2.getDesc());
                habitArrayList.add(habit2);
            } catch(Exception e){
                Log.e("Missing json?", "YEA THERE WASNT ENOUGH HABITS I GUESS");
                continue;
            }
        }


//        Gson gson = new Gson();
//        String json = mPrefs.getString("Testname", "");
//        Log.d("JSON", "This is json: " + json);
//        Habit habit3 = gson.fromJson(json, Habit.class);
//        Log.d("test", habit3.getName());
//
//        Habit habit2 = new Habit(habit3);
////        Habit habit2 = new Habit(habit.getFavorite(), habit.getName(), habit.getDesc());
//        habitArrayList.add(habit2);
    }
    private void createHabitList(){
        Habit test = new Habit(true, "Study Japanese", "Attempt to finish review every day!");
        Habit test2 = new Habit(false, "Excercise", "Run a mile each day");
        Habit test3 = new Habit(false, "Take time to relax", "Spend at least 15 minutes each day relaxing your mind");

        habitTestArray.add(test);
        habitTestArray.add(test2);
        habitTestArray.add(test3);
    }
    private void printTestArray(){
        Log.d("MyApp", habitTestArray.get(0).getName());
        Log.d("MyApp", habitTestArray.get(2).getName());
    }

    private void printHabitArray(){
        for(int i = 0; i < habitArrayList.size(); i++){
            Log.d("Size??", "" + habitArrayList.size());
            Log.d("Names??", habitArrayList.get(i).getName());
        }
        if(habitArrayList.size() == 0){
//            Log.d("Names:", "EMPTY");
        }
    }


}


//        for(Integer i = -1; i < 10; i++){
//            Gson gson = new Gson();
//            try {
//                Log.e("Made it to Try", "IN TRY");
//                String json = mPrefs.getString(i.toString(), "");
//
//                Log.e("Made it to SECOND", "SECOND");
//                habit = gson.fromJson(json, Habit.class);
//
//
//                Log.e("Object", habit.getName() + "  " + habit.getDesc());
//                habitArrayList.add(habit);
//            } catch(Exception e){
//                //Log.e("Missing json?", "YEA THERE WASNT ENOUGH HABITS I GUESS");
//                continue;
//            }
//        }