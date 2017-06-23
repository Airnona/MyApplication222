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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;                                                             //Used for drawer stuff
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    SharedPreferences mPrefs;
    private ArrayList<Habit> habitArrayList = new ArrayList<Habit>();                                //Hopefully actual Habit Array

    ViewHolder viewHolder;

    ListView habitListView;
    MyHabitsListAdapter myAdapter;
    boolean[] hasHabit = new boolean[50];

    boolean checkEdit = false;
    int idToCheck;
    Habit habitToCheck;

    int currentView;
    final int NUM_VIEWS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                         //BASIC NEEDS
        setContentView(R.layout.activity_main);//BASIC NEEDS



        mPrefs = getSharedPreferences("EditHabits", 0);
        if(!(mPrefs.contains("numHabits")))
            editHabitCounter(0);
        if(!(mPrefs.contains("maxID"))){
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putInt("maxID", 50);
            prefsEditor.apply();
        }
        for(int i = 0; i < 50; i++){
            hasHabit[i] = false;
        }

//        createHabitList();

        generateHabitArrayList();

        printHabitArray();

        myAdapter = new MyHabitsListAdapter(this, R.layout.habitslayout2, habitArrayList);
        habitListView = (ListView) findViewById(R.id.habitListView);
        habitListView.setAdapter(myAdapter);
        registerForContextMenu(habitListView);


//        habitListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                MainActivity.super.onCreateContextMenu(menu, v, menuInfo);
//            }
//        });

        mToolbar = (Toolbar) findViewById(R.id.nav_action);                                         //Toolbar for top of screen
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById((R.id.drawerLayout));                           //Drawer slider code
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);                                                   //Drawer button code
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);                                      //Creates the Drawer Slider button (Top left)
    }

    @Override
    protected void onRestart() {
        if(generateHabitArrayList()) {
            myAdapter.notifyDataSetChanged();
            Log.d("Logging", "Notifydatachanged is called");
        }
        if(checkEdit){
            if(checkHabit())
                myAdapter.notifyDataSetChanged();
        }

        super.onRestart();
        myAdapter.notifyDataSetChanged();
        Log.e("Logging", "onRestart Called");
//        printHabitArray();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_context_menu, menu);

    }   //Self explanatory
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        Log.d("Logging", "Position: " + info.position);
        switch(item.getItemId())
        {
            case R.id.delete_id:
                deleteHabit(info.position);
                myAdapter.notifyDataSetChanged();
                return true;
            case R.id.edit_id:
//                Log.d("Logging", "Position: " + info.position);



                Intent intent = new Intent(MainActivity.this, EditHabits.class);
                intent.putExtra("Habit_ID", habitArrayList.get(info.position).getHabit_ID());
                Log.e("Position", "" + info.position);
                intent.putExtra("Habit_New", false);
                checkEdit = true;
                idToCheck = habitArrayList.get(info.position).getHabit_ID();
                startActivity(intent);



            default:
                return super.onContextItemSelected(item);
        }
    }                                       //Deals with Contextual Menu items.
    public void deleteHabit(int pos){
        Log.d("Logging", "In Delete Habit, pos: " + pos);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.remove(String.valueOf(habitArrayList.get(pos).getHabit_ID()));
        prefsEditor.apply();
        habitArrayList.remove(pos);

        myAdapter.notifyDataSetChanged();
    }                                                           //When user clicks 'Delete Habit' from Contextual Menu
    public void addHabit(View view){
        Intent intent = new Intent(MainActivity.this, EditHabits.class);
        int habitIDPass = 0;
        for(int i = 0; i < habitArrayList.size(); i++){
            habitIDPass = i;
            if(habitArrayList.get(i).getHabit_ID() != i)
                break;
        }
        if(habitIDPass == habitArrayList.size() - 1 && habitIDPass == habitArrayList.get(habitArrayList.size() - 1).getHabit_ID())
            habitIDPass++;

        intent.putExtra("Habit_ID", habitIDPass);
        intent.putExtra("Habit_New", true);
        startActivity(intent);
    }                                                            //When user clicks 'Add Habit' Button
    public boolean checkHabit(){
//        Log.d("Logging", "In check habit");
        Gson gson = new Gson();

        int index = -1;
        for(int i = 0; i < habitArrayList.size(); i++){
            if(habitArrayList.get(i).getHabit_ID() == idToCheck) {
                index = i;
                break;
            }
        }                                                                                     //Sets index to HAbit in HabitArrayList we're checking.
        Habit holder = habitArrayList.get(index);
//        Log.d("Logging", "After holder is set");

        String json = mPrefs.getString(String.valueOf(idToCheck),"");
        Habit compare = gson.fromJson(json, Habit.class);
//        Log.d("Logging", "Before if");
        if(compare.getName() == holder.getName() && compare.getDesc() == holder.getDesc()) {
//            Log.d("Logging", "Returning false - checkHabit");
            return false;
        }
        else {
//            Log.d("Logging", "Returning true - checkHabit");
//            printHabit(compare);
            habitArrayList.get(index).setName(compare.getName());
            habitArrayList.get(index).setDesc(compare.getDesc());
//            printHabit(habitArrayList.get(index));
            return true;
        }
    }                                                                //Checks specified habit to see if any changes were made


    public void changeView(View view){
        currentView++;
        if(currentView >= NUM_VIEWS)
            currentView = 0;

        Log.d("Logging", "changeView - Start of Change View. CurrentView: " + currentView);

        switch (currentView)
        {
            case 0: //Age View
//                Log.d("Logging", "changeView - Changing to defaultView (case: 0");
                ageView();
//                Log.d("Logging", "changeView - Finished Age View");
//                printHabitArray();
//                myAdapter.notifyDataSetChanged();
                break;
            case 1: //Strength View
                strongestView();
//                myAdapter.notifyDataSetChanged();

                break;
            case 2: //Favorite - Age View
//                favoriteView();
//                myAdapter.notifyDataSetChanged();

                break;

            case 3: //Favorite - Strength View

                break;

            default:
                Log.d("Logging", "changeView - Changing to Default View (ageView)");
                ageView();
        }
        myAdapter.notifyDataSetChanged();
    }
    public void ageView(){
        Log.d("Logging", "ageView - Start");
        ArrayList<Habit> holder = new ArrayList<>();

        int smallest, index;

        int sizeStatic = habitArrayList.size();
        int sizeDynamic = habitArrayList.size();

        for(int i = 0; i < sizeStatic; i++){
            smallest = habitArrayList.get(0).getNumber();
            index = 0;
            for(int j = 1; j < sizeDynamic; j++){
                if(habitArrayList.get(j).getNumber() < smallest){
                    index = j;
                    smallest = habitArrayList.get(j).getNumber();
                }
            }
            holder.add(habitArrayList.get(index));
            habitArrayList.remove(index);
            sizeDynamic--;
        }
//        habitArrayList = holder;          //Doesnt work, lame. Leaves screen empty
        for(int i = 0; i < holder.size(); i++){     //Need this for loop to populate screen. L A M E
            habitArrayList.add(holder.get(i));
        }
    }
    public void strongestView(){
        Log.d("Logging", "strongestView - Start");

        ArrayList<Habit> holder = new ArrayList<>();

        int largest, index;

        int sizeStatic = habitArrayList.size();
        int sizeDynamic = habitArrayList.size();

        for(int i = 0; i < sizeStatic; i++){
            index = 0;
            largest = habitArrayList.get(0).getContDays();

            for(int j = 1; j < sizeDynamic; j++){
                if(largest < habitArrayList.get(j).getContDays()) {
                    largest = habitArrayList.get(j).getContDays();
                    index = j;
                }
            }
            holder.add(habitArrayList.get(index));
            habitArrayList.remove(index);
            sizeDynamic--;
        }
        for(int i = 0; i < holder.size(); i++){
            habitArrayList.add(holder.get(i));
        }
    }
    public void favoriteAgeView(){

    }
    public void favoriteStrongestView(){

    }
    public void updateView(){
        switch (currentView)
        {
            case 0: //Age View
                ageView();
                break;
            case 1: //Strength View
                strongestView();
                break;
            case 2: //Favorite - Age View
//                favoriteAgeView();
                break;

            case 3: //Favorite - Strength View
//                favoriteStrengthView();
                break;

            default:
                Log.d("Logging", "updateView - Changing to Default View (ageView)");
                ageView();
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }                                       //Top Left Menu button



    private class MyHabitsListAdapter extends ArrayAdapter<Habit>{
        int habitSelectedID;
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
                viewHolder = new ViewHolder();

                viewHolder.toggle = (ToggleButton) convertView.findViewById(R.id.habitLayoutToggle);
                if(habitArrayList.get(position).getFavorite()) {
                    viewHolder.toggle.setChecked(true);
                }
                viewHolder.toggle.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){                                        //OnClickListener for Toggle button (Changes favorite value in JSON file)
                        Gson gson = new Gson();
                        String pos = String.valueOf(habitArrayList.get(position).getHabit_ID());
                        Log.d("Logging", "Psotion: " + position + "  Habit id: " + pos);
                        String json = mPrefs.getString(pos, "");

                        Habit holder = gson.fromJson(json, Habit.class);                            //Loading Habit object to holder
                        printHabit(holder);
                        if(holder.getFavorite()){
                            holder.setFavorite(false);
                            habitArrayList.get(position).setFavorite(false);

                        } else{
                            holder.setFavorite(true);
                            habitArrayList.get(position).setFavorite(true);
                        }

                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        json = gson.toJson(holder);                                                 //Saves habitObject back to json
                        prefsEditor.putString(pos, json);
                        Log.d("Logging", "Habit ID to String: " + String.valueOf(holder.getHabit_ID()));
                        prefsEditor.commit();

                    }
                });

                viewHolder.text = (TextView) convertView.findViewById(R.id.habitLayoutText);

                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.habitLayout2CheckBox);
                viewHolder.checkBox.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        int arrListPos = Integer.parseInt(v.getTag().toString());
                        Log.d("Logging", "MyHabitsListAdapter - Checkbox Checked");
                        if((!habitArrayList.get(arrListPos).getIsChecked())) {
                            Log.d("Logging", "if: Checked, Position: " + arrListPos);
                            incrementCounter(habitArrayList.get(arrListPos), arrListPos);           //Increment the ContDays and save to file
                            saveHabitIsChecked(arrListPos, true);
                        }
                        else {
                            Log.d("Logging", "else: Position: " + arrListPos);
                            decrementCounter(habitArrayList.get(arrListPos), arrListPos);           //Decrement the ContDays and save to file
                            saveHabitIsChecked(arrListPos, false);
                        }
                        updateView();
//                        myAdapter.notifyDataSetChanged();                                           //ISSUE: This causes all views on Screen to be updated. Fix later if it's a problem
                    }
                });

                convertView.setTag(viewHolder);
            }

            else{ viewHolder = (ViewHolder)convertView.getTag(); }

            viewHolder.checkBox.setTag(position);

//            Log.d("Logging", "MyHabitsListAdapter-GetView: position: " + position);
            int contDays = habitArrayList.get(position).getContDays();
//            Log.d("Logging", "MyHabitsListAdapter-GetView: contDays: " + contDays);


            if(contDays < 1)
                convertView.setBackgroundResource(R.drawable.bghabitrank4);                         //NEED TO CHANGE TO DEFAULT / GREY COLOR
            if(contDays > 0 && contDays <= 5)
                convertView.setBackgroundResource(R.drawable.bghabitrank1);
            if(contDays > 5 && contDays <= 10)
                convertView.setBackgroundResource(R.drawable.bghabitrank2);
            if(contDays > 10 && contDays <= 15)
                convertView.setBackgroundResource(R.drawable.bghabitrank3);
            if(contDays > 15 && contDays <= 20)
                convertView.setBackgroundResource(R.drawable.bghabitrank4);
            if(contDays > 20)
                convertView.setBackgroundResource(R.drawable.bghabitrank5);                                                                                     //Determines background color for each item in ListView

            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.text.setText(habitArrayList.get(position).getName());                    //THESE NEXT THREE LINES UPDATE THE LISTVIEW IF ANYTHING IS CHANGED
            mainViewHolder.checkBox.setChecked(habitArrayList.get(position).getIsChecked());        //SUPER ESSENTIAL HOLY MOLY
            mainViewHolder.toggle.setChecked(habitArrayList.get(position).getFavorite());           //I think you have to call notifyDataChangeD() if these are changed at all, not sure though
            return convertView;
        }
    }                              //Custom adapter for Listview that holds Habits
    public class ViewHolder {
        ToggleButton toggle;
        TextView text;
        CheckBox checkBox;
    }                                                                   //Viewholder so Custom adapter (^) doesn't need to keep creating these.
    public void incrementCounter(Habit habit, int index){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        habitArrayList.get(index).setContDays(habit.getContDays() + 1);                             //Sets contdays to contdays++
        String json = gson.toJson(habitArrayList.get(index));
        prefsEditor.putString(String.valueOf(habit.getHabit_ID()), json);                           //Saves it back into the json
        prefsEditor.commit();

        Log.d("Logging", "incrementCoutner - Final contdays value: " + habitArrayList.get(index).getContDays());
    }
    public void decrementCounter(Habit habit, int index){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        habitArrayList.get(index).setContDays(habit.getContDays() - 1);                             //Sets contdays to contdays++
        String json = gson.toJson(habitArrayList.get(index));
        prefsEditor.putString(String.valueOf(habit.getHabit_ID()), json);//Saves it back into the json
        prefsEditor.commit();

        Log.d("Logging", "decrementCoutner - Final contdays value: " + habitArrayList.get(index).getContDays());
    }
    public void saveHabitIsChecked(int pos, boolean checked){
        Gson gson = new Gson();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        habitArrayList.get(pos).setIsChecked(checked);
        String json = gson.toJson(habitArrayList.get(pos));
        prefsEditor.putString(String.valueOf(habitArrayList.get(pos).getHabit_ID()), json);
        prefsEditor.commit();
    }


    private boolean generateHabitArrayList(){
        boolean passed = false, addHabit = true;
        Gson gson = new Gson();
        Habit habitHold = new Habit();

        int numOfHabits = mPrefs.getInt("numHabits", 10);
        int maxID = mPrefs.getInt("maxID", 0);

        for(int i = 0; i < maxID; i++){
            String json = mPrefs.getString(String.valueOf(i), "");
//            Log.d("Logging", "First for: " + i + "Json: " + json);
            if(json == "")
                continue;
            habitHold = gson.fromJson(json, Habit.class);
            if(habitHold.getHabit_ID() == i) {
                addHabit = true;
                for(int j = 0; j < habitArrayList.size(); j++){
//                    Log.d("Logging", "Second for: " + j);
                    if(habitArrayList.get(j).getHabit_ID() == habitHold.getHabit_ID()){
//                        Log.d("Logging", "getj habitid: " + habitArrayList.get(j).getHabit_ID() + "\nhabithold id: " + habitHold.getHabit_ID());
                        addHabit = false;
                        break;                                                                      //THIS MIGHT BREAK OUT OF BOTH FOR LOOPS
                    }
                }
                if(addHabit){
                    habitArrayList.add(habitHold);
                    hasHabit[habitHold.getHabit_ID()] = true;
                    passed = true;

                    sortHabitArrayList(currentView);
//                    Log.d("Logging", "Adding habit!");
                }
            }
//            Log.d("Logging", "i: " + i + "\njson: " + json + "\nHabit id: " + habitHold.getHabit_ID());
        }





//        for(Integer i = 0; i < numOfHabits; i++){
//            if(habitArrayList.size() > i) {
//                Log.d("Logging", "Habit array size: " + habitArrayList.size());
//                Log.d("Logging", "  i: " + i);
//                continue;
//            }
//            Log.d("Logging", "Setting passed to true. i = " + i);
//            passed = true;
//            try {
//                String json = mPrefs.getString(i.toString(), "");                                   //Gets the object info based on the ID / Name it was saved under (uses I to do so)
//
//                Habit habitHold = gson.fromJson(json, Habit.class);
//                Habit habitAdd = new Habit(habitHold);
//
////                Log.e("Logging", "HabitID: " + habitAdd.getHabit_ID() + "Name:" + habitAdd.getName() + "  " + habitAdd.getDesc() + " True/False: " + habitAdd.getFavorite());
//                habitArrayList.add(habitAdd);
//            } catch(Exception e){
////                Log.e("Logging", "Missing json? YEA THERE WASNT ENOUGH HABITS I GUESS");
//                continue;
//            }
//        }
        return passed;
    }                                                   //Checks differences in current arrayList and adds habits from json
    public void sortHabitArrayList(int view){
//        Log.d("Logging", "made it hereee");
        boolean doDefault = false;

        switch(view)
        {
            case 0: //Default view
                doDefault = true;
                break;
            case 1: //Favorite view

                break;

            case 2: //Strongest view

                break;
            default: //Same as Default
                Log.d("Logging", "sortHabitArrayList - In default case, shouldn't be here??");
                doDefault = true;
                break;
        }

        if(doDefault){
            Habit holder = habitArrayList.get(habitArrayList.size() - 1);
            for(int i = 0; i < habitArrayList.size(); i++){
//            Log.d("Logging", "For Loop: " + i);
                if(holder.getHabit_ID() < habitArrayList.get(i).getHabit_ID()) {
                    habitArrayList.add(i, holder);
                    habitArrayList.remove(habitArrayList.size() - 1);
                    return;
                }
            }
        }
    }                                                           //Sorts based on ID

    private void printHabitArray(){
        for(int i = 0; i < habitArrayList.size(); i++){
            Log.d("Logging", "Name: " + habitArrayList.get(i).getName() +
                  "\nID: " + habitArrayList.get(i).getHabit_ID() +
                  "\nFavorite: " + habitArrayList.get(i).getFavorite() +
                  "\nNumber: " + habitArrayList.get(i).getNumber() +
                  "\nisChecked: " + habitArrayList.get(i).getIsChecked() +
                  "\ncontDays: " + habitArrayList.get(i).getContDays());
        }
        if(habitArrayList.size() == 0){
//            Log.d("Names:", "EMPTY");
        }
    }                                                             //Prints all Habits in the ArrayList
    private void printHabit(Habit habit){
        Log.d("Logging", "Name: " + habit.getName() + "\nDesc: " + habit.getDesc() +
              "\nFavorite: " + habit.getFavorite() + "\nID: " + habit.getHabit_ID() + "\nContDays: " +habit.getContDays());
        Log.d("Logging", "Name: " + habit.getName() + "    ID: " + habit.getHabit_ID());
    }                                                       //Prints all the info from a specific habit


    public void editHabitCounter(int change){
        int currentNum = mPrefs.getInt("numHabits", 0);

        currentNum += change;

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putInt("numHabits", currentNum);
        prefsEditor.commit();
    }                                                   //Not sure if this is even used anymore
}
