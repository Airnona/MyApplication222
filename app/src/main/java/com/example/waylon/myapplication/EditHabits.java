package com.example.waylon.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

public class EditHabits extends AppCompatActivity {

    EditText editTextName, editTextDesc;                                                            //Edit text boxes on the screen

    public Integer habit_id;                                                                        //Tells which habit we're editing by ID #, gained from intent.getextra
    private boolean habit_new;
    public Habit habit;

    private String habitName, habitDesc;                                                            //Gained from intent.getExtra -------- wait what?? I dont think that works that way


    SharedPreferences mPrefs;                                                                       //For saving data through shared preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                         //BASIC NEEDS
        setContentView(R.layout.activity_edit_habits);                                              //BASIC NEEDS

        mPrefs = getSharedPreferences("EditHabits", 0);

        Intent intent = getIntent();
        habit_id = intent.getIntExtra("Habit_ID", -1);                                              //Sets Habit_id, -1 by default
        habit_new = intent.getBooleanExtra("Habit_New", false);                                     //Sets if this is going to be a new habit, false otherwise
        Log.e("Logging", "" + habit_id + "   HAbit_new: " + habit_new);

        editTextName = (EditText) findViewById(R.id.editHabit_editTextName);
        editTextDesc = (EditText) findViewById(R.id.editHabit_editTextDesc);

        if(habit_new){
            generateNewHabit();
        }
        else{
            generateOldHabit();
        }


        editTextName = (EditText) findViewById(R.id.editHabit_editTextName);
        editTextDesc = (EditText) findViewById(R.id.editHabit_editTextDesc);
    }


    public void generateNewHabit(){
        int number = mPrefs.getInt("numHabits", 0);
        habit = new Habit(number);
    }
    public void generateOldHabit(){
        Gson gson = new Gson();
        String json = mPrefs.getString(String.valueOf(habit_id), "");
//        Log.e("Logging", "json: " + json);
        habit = gson.fromJson(json, Habit.class);
//        Log.e("Logging", "past habit?" + habit.getName());

        editTextName.setText(habit.getName());
        editTextDesc.setText(habit.getDesc());
    }
    public void confirmButtonMethod(View view){
        habit.setName(editTextName.getText().toString());
        habit.setDesc(editTextDesc.getText().toString());
        habit.setHabit_ID(habit_id);

        saveHabit(habit);
        finish();
    }
    public void saveHabit(Habit habit){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habit);                                                        //Saves habitObject to gson
        prefsEditor.putString(habit_id.toString(), json);
//        Log.e("Logging", "Habit ID to String: " + habit_id.toString());

//        Log.d("Logging", "Saving habit: " + habit.getName() + "  " + habit.getDesc());
        editHabitCounter(1);
        prefsEditor.commit();
    }
    public void editHabitCounter(int change){
        int currentNum = mPrefs.getInt("numHabits", 0);
        currentNum += change;

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putInt("numHabits", currentNum);
        prefsEditor.commit();
    }










}


//    public void addNewHabit(View view){
//        Habit habit = new Habit();
//
//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(habit);                                                        //Saves habitObject to gson
//        prefsEditor.putString(habit_id.toString(), json);
//
//        prefsEditor.commit();
//
//    }
//    public void testMethod(View view){
//        saveData();
//        getData();
//    }
//    public void saveData(){
//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(testHabit);
//        prefsEditor.putString(testHabit.getName(), json);
//        prefsEditor.commit();
//    }
//    public void getData(){
//        Gson gson = new Gson();
//        String json = mPrefs.getString("Testname", "");
//        Log.d("Logging", "Old Json is:" + json);
//        Habit myObj = gson.fromJson(json, Habit.class);
//        Log.d("Logging", "Object" + myObj.getName() + "  " + myObj.getDesc());
//    }
//    public void writeData(View view){
//        String habitName = editTextName.getText().toString();
//        String habitDesc = editTextDesc.getText().toString();
//        String habitFile = "habit_file";
//
//        try{
//            FileOutputStream fileOutputStream = openFileOutput(habitFile, MODE_PRIVATE);
//            fileOutputStream.write(habitName.getBytes());
//            fileOutputStream.write(testHabit);
//            fileOutputStream.close();
//            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    public void populateFields(){
//
//
//
//
//    }
//    public void setHabit_id(int num){
//        this.habit_id = num;
//    }