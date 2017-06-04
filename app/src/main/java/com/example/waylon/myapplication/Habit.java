package com.example.waylon.myapplication;

/**
 * Created by Waylon on 5/28/2017.
 */

public class Habit {
    private boolean favorite;
    private int contDays, habit_ID;
    private String name, desc;

    public Habit(boolean favorite, String name, String desc){
        this.favorite = favorite;
        this.contDays = 0;
        this.name = name;
        this.desc = desc;
    }                                  //Initialized Constructor
    public Habit(){
        this.favorite = false;
        this.contDays = 0;
        this.name = null;
        this.desc = null;
    }                                                                            //Default Constructor
    public Habit(Habit habit){
        this.name = habit.getName();
        this.desc = habit.getDesc();
        this.contDays = habit.getContDays();
        this.favorite = habit.getFavorite();
        this.habit_ID = habit.getHabit_ID();
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

}
