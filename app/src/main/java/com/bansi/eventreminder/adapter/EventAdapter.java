package com.bansi.eventreminder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bansi.eventreminder.activity.EventDetailActivity;
import com.bansi.eventreminder.R;

import com.bansi.eventreminder.model.EventModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    ArrayList<EventModel>eventModels;
    static Context context;

    public EventAdapter(ArrayList<EventModel> eventModels,Context context) {
        this.context=context;
        this.eventModels = eventModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View ListItem=inflater.inflate(R.layout.event_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(ListItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel model=eventModels.get(position);
        holder.name.setText(eventModels.get(position).getEventName());
        holder.description.setText(eventModels.get(position).getDescription());
        holder.starttime.setText(eventModels.get(position).getTime());
        holder.tvEventDate.setText(date(eventModels.get(position).getEventDate()));
        holder.tvEventDay.setText(day(eventModels.get(position).getEventDate()));

        if (eventModels.get(position).getIs_completed().equals("1")){
            holder. ivTint.setColorFilter(context.getColor(R.color.purple_700));
        }else if (eventModels.get(position).getIs_completed().equals("0")){
            holder.ivTint.setColorFilter(context.getColor(R.color.orange));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EventDetailActivity.class)
                        .putExtra("name",eventModels.get(position).getEventName())
                        .putExtra("des",eventModels.get(position).getDescription())
                        .putExtra("date",eventModels.get(position).getEventDate())
                        .putExtra("time",eventModels.get(position).getTime())
                        .putExtra("day",day2(eventModels.get(position).getEventDate())));
            }
        });
    }
    public String date(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat sd = new SimpleDateFormat("dd");
        Date eventDate = null;
        String str=null;
        try {
            eventDate = sdf.parse(strDate);
             str=sd.format(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

    public String day(String day){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat sd = new SimpleDateFormat("EEE");
        Date eventDate = null;
        String str=null;
        try {
            eventDate = sdf.parse(day);
            str=sd.format(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }
    public String day2(String day){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat sd = new SimpleDateFormat("EEEE");
        Date eventDate = null;
        String str=null;
        try {
            eventDate = sdf.parse(day);
            str=sd.format(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,starttime,description,tvEventDate,tvEventDay;
      ImageView ivTint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvEventName);
            starttime=itemView.findViewById(R.id.tvSDate);
            tvEventDate=itemView.findViewById(R.id.tvEventDate);
            description=itemView.findViewById(R.id.tvDes);
            ivTint=itemView.findViewById(R.id.ivTint);
            tvEventDay=itemView.findViewById(R.id.tvEventDay);
        }
    }
}
