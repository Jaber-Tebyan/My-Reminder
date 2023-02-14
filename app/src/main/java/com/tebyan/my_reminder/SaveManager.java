package com.tebyan.my_reminder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SaveManager {

    public Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String prefName;
    private Gson gson;


    public SaveManager(Context context, String prefName) {
        this.context = context;
        this.prefName = prefName;
        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

    }

    public Task[] LoadTasks() {

        Task[] tasks = gson.fromJson(sharedPreferences.getString("Tasks", "[]"), Task[].class);
        Log.i(TAG, "LoadTasks: "+tasks.length);
        return tasks;
    }

    public void SaveTasks(Task[] tasks) {
        editor.putString("Tasks", gson.toJson(tasks));
        editor.apply();

    }
    public void SaveRequestCodes(int requestCodes){
        editor.putString("requestCodeCount",gson.toJson(requestCodes));
        editor.apply();
    }
    public int LoadRequestCodes(){
        return gson.fromJson(sharedPreferences.getString("requestCodeCount","0"), (Type) int.class);
    }
}
