package com.tebyan.my_reminder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Task _currentTask;
    RecyclerView recyclerView;
    TaskAlarmManager taskAlarmManager;
    CustomTaskAdapter customTaskAdapter;
    FloatingActionButton addBtn;
    SaveManager saveManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskAlarmManager=new TaskAlarmManager(this);
        saveManager=new SaveManager(this,"MyPrefs");
        taskAlarmManager.SetTasks(saveManager.LoadTasks());
        ViewsInit();
        ViewsAddListeners();
        LoadData();




        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        customTaskAdapter=new CustomTaskAdapter(this,taskAlarmManager.tasks);
        recyclerView.setAdapter(customTaskAdapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        SaveData();
    }

    public void LoadData(){
        taskAlarmManager.SetTasks(saveManager.LoadTasks());
        taskAlarmManager.SetRequestCodes(saveManager.LoadRequestCodes());
        Log.i(TAG, "LoadData: "+taskAlarmManager.requestCodeCount);

    }
    public void SaveData(){
        saveManager.SaveTasks(taskAlarmManager.tasks.toArray(new Task[0]));
        saveManager.SaveRequestCodes(taskAlarmManager.requestCodeCount);
    }
    public void ViewsInit(){
        addBtn=findViewById(R.id.addBtn);
        recyclerView=findViewById(R.id.recyclerView);



    }
    public void ViewsAddListeners(){

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=GetDialog();
                dialog.show();

            }
        });
    }

    public AlertDialog GetDialog(){
        Calendar calendar=Calendar.getInstance();
        return GetDialog(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
    }
    public AlertDialog GetDialog(int year,int month,int day,int hour,int minute){
        _currentTask=new Task();
        _currentTask.setEndTime(hour,minute);
        _currentTask.SetRequestCode(taskAlarmManager.requestCodeCount);
        taskAlarmManager.requestCodeCount+=1;
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

        View view=LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        EditText _edt=(view.findViewById(R.id.taskDescriptionEDT));
        EditText edt=view.findViewById(R.id.taskNameEDT);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(_currentTask.isDateEmpty()){
                    Toast.makeText(MainActivity.this, "Please Set The End Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                _currentTask.setName(edt.getText().toString());
                _currentTask.setTaskDescription(_edt.getText().toString());
                if(_currentTask.isNameEmpty()){
                    Toast.makeText(MainActivity.this, "Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                taskAlarmManager.AddTaskWithAlarm(_currentTask);
                customTaskAdapter.notifyItemInserted(taskAlarmManager.tasks.size());
                _currentTask=null;
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _currentTask=null;
            }
        });
        AlertDialog dial=builder.create();


        view.findViewById(R.id.chooseEndDate).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener listener =new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        _currentTask.setEndDate(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                    }
                };
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this,listener,year,month,day);
                datePickerDialog.show();
            }
        });
        view.findViewById(R.id.chooseEndTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        _currentTask.setEndTime(timePicker.getHour(),timePicker.getMinute());
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });



        dial.setView(view);
        return dial;
    }
}
