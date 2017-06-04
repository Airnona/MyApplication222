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
    Habit testHabit = new Habit(true, "Testname", "Testdesc");                                      //For testing purposes before dynamic saving is created

    public Integer habit_id;                                                                        //Tells which habit we're editing by ID #, gained from intent.getextra
    private boolean habit_new;
    public Habit habit;

    private String habitName, habitDesc;                                                            //Gained from intent.getExtra -------- wait what?? I dont think that works that way


    SharedPreferences mPrefs;                                                                       //For saving data through shared preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                                                         //BASIC NEEDS
        setContentView(R.layout.activity_edit_habits);                                              //BASIC NEEDS

        mPrefs = getPreferences(MODE_PRIVATE);
        String json = mPrefs.getString("Testname", "");
        Log.d("TESTING2222", json);

        Intent intent = getIntent();
        habit_id = intent.getIntExtra("Habit_ID", -1);                                              //Sets Habit_id, -1 by default
//        Log.e("Intent.getExtra", "" + habit_id);
        habit_new = intent.getBooleanExtra("Habit_New", false);                                     //Sets if this is going to be a new habit, false otherwise
//        Log.d("Habit_ID", ""+ habit_id);

        if(habit_new){
            generateNewHabit();
        }
        else{
            generateOldHabit();
        }






        editTextName = (EditText) findViewById(R.id.editHabit_editTextName);
        editTextDesc = (EditText) findViewById(R.id.editHabit_editTextDesc);

//        populateFields();



    }


    public void generateNewHabit(){
        habit = new Habit();
    }
    public void generateOldHabit(){

    }
    public void confirmButtonMethod(View view){
        habit.setName(editTextName.getText().toString());
        habit.setDesc(editTextDesc.getText().toString());

        saveHabit(habit);
    }
    public void saveHabit(Habit habit){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(habit);                                                        //Saves habitObject to gson
        prefsEditor.putString(habit_id.toString(), json);
        Log.e("Habit_ID to string", habit_id.toString());

        Log.d("SavingHabit", habit.getName() + "  " + habit.getDesc());

        prefsEditor.commit();
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
    public void testMethod(View view){
        saveData();
        getData();
    }
    public void saveData(){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(testHabit);
        prefsEditor.putString(testHabit.getName(), json);
        prefsEditor.commit();
    }
    public void getData(){
        Gson gson = new Gson();
        String json = mPrefs.getString("Testname", "");
        Log.d("Old JSON", "Old Json is:" + json);
        Habit myObj = gson.fromJson(json, Habit.class);
        Log.d("Object", myObj.getName() + "  " + myObj.getDesc());
    }
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
}
