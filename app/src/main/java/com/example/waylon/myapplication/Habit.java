package com.example.waylon.myapplication;

import android.content.SharedPreferences;

/**
 * Created by Waylon on 5/28/2017.
 */

public class Habit {
    private boolean favorite;           //Determines if Habit is favorited
    private boolean isChecked;          //Determines if Habit has been marked off for the day
    private int contDays;               //Determines Strength of habit
    private int habit_ID;               //Determines NonVisible ID of habit, used in JSONs
    private int number;                 //Determines the age of habit. Higher the number, the later it was created
    private String name;                //Determines name of Habit
    private String desc;                //Determines Description of Habit

    public Habit(boolean favorite, boolean isChecked, String name, String desc, int number){
        this.favorite = favorite;
        this.isChecked = isChecked;
        this.contDays = 0;
        this.name = name;
        this.desc = desc;
        this.number = number;
    }    //Initialized Constructor
    public Habit(){
        this.favorite = false;
        this.isChecked = false;
        this.contDays = 0;
        this.name = null;
        this.desc = null;
        this.number = 0;
    }                                                                             //Default Constructor
    public Habit(Habit habit){
        this.name = habit.getName();
        this.desc = habit.getDesc();
        this.contDays = habit.getContDays();
        this.favorite = habit.getFavorite();
        this.habit_ID = habit.getHabit_ID();
        this.number = habit.getNumber();
        this.isChecked = habit.getIsChecked();
    }
    public Habit(int number){
        this.favorite = false;
        this.isChecked = false;
        this.contDays = 0;
        this.name = null;
        this.desc = null;
        this.number = number;

    }

                                                                                                    //Getter Methods
    public String getName(){
        return this.name;
    }
    public String getDesc(){
        return this.desc;
    }
    public int getContDays(){
        return this.contDays;
    }
    public boolean getFavorite(){
        return this.favorite;
    }
    public int getHabit_ID(){
        return this.habit_ID;
    }
    public int getNumber() {
        return this.number;
    }
    public boolean getIsChecked(){
        return this.isChecked;
    }

    //Setter Methods
    public void setName(String name){
        this.name = name;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setContDays(int contDays){
        this.contDays = contDays;
    }
    public void setFavorite(boolean favorite){
        this.favorite = favorite;
    }
    public void setHabit_ID(int habit_ID){
        this.habit_ID = habit_ID;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
}
