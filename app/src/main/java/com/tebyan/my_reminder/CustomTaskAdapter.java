package com.tebyan.my_reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomTaskAdapter extends RecyclerView.Adapter<CustomTaskAdapter.CustomViewHolder> {
    private ArrayList<Task> tasks;
    public Context context;

    public CustomTaskAdapter(Context context,ArrayList<Task> tasks){
        this.context=context;
        this.tasks=tasks;

    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_recycle_view,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int index) {
        holder.taskDescriptionText.setText(tasks.get(index).getTaskDescription());
        holder.taskEndDateText.setText(tasks.get(index).GetEndDate());
        holder.taskNameText.setText(tasks.get(index).name);
        holder.taskEndTimeText.setText(tasks.get(index).GetEndTime());

        if(index%2==0){
            holder.root.setBackgroundResource(R.drawable.even_drawable);
        }else{
            holder.root.setBackgroundResource(R.drawable.odd_drawable);
        }
        holder.AddDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=new AlertDialog.Builder(context).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tasks.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }).setMessage("Are you sure you want to delete it").create();
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        LinearLayout root;
        Button deleteBtn;
        TextView taskNameText;
        TextView taskEndTimeText;
        TextView taskDescriptionText;
        TextView taskEndDateText;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            taskDescriptionText=itemView.findViewById(R.id.taskDescriptionTXT);
            taskNameText=itemView.findViewById(R.id.taskNameTXT);
            taskEndTimeText=itemView.findViewById(R.id.taskEndTime);
            root=itemView.findViewById(R.id.root);
            deleteBtn=itemView.findViewById(R.id.deleteBtn);
            taskEndDateText=itemView.findViewById(R.id.taskEndDate);

        }

        public void AddDeleteListener(View.OnClickListener listener){
            deleteBtn.setOnClickListener(listener);
        }
    }
}
