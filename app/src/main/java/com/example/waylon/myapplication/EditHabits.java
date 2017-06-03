package com.example.waylon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditHabits extends AppCompatActivity {

    private int habit_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habits);
    }

    public void setHabit_id(int num){
        this.habit_id = num;
    }
}
