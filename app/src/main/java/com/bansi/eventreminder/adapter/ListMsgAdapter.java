package com.bansi.eventreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bansi.eventreminder.R;
import com.bansi.eventreminder.model.ListMsgs;
import com.bansi.eventreminder.model.MessageModel;

import java.util.ArrayList;

public class ListMsgAdapter extends RecyclerView.Adapter<ListMsgAdapter.ViewHolder> {
    ArrayList<MessageModel>msgsArrayList;
    static Context context;

    public ListMsgAdapter(ArrayList<MessageModel> msgsArrayList,Context context) {
        this.context=context;
        this.msgsArrayList = msgsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View ListItem=inflater.inflate(R.layout.list_msg,parent,false);
        ViewHolder viewHolder=new ViewHolder(ListItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MessageModel msgs=msgsArrayList.get(position);
        holder.textView.setText(msgs.getMsg());


    }

    @Override
    public int getItemCount() {
        return msgsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tvMsg);
        }
    }
}
