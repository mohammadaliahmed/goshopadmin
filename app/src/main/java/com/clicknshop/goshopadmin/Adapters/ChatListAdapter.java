package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Activities.LiveChat;
import com.clicknshop.goshopadmin.Models.ChatModel;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.clicknshop.goshopadmin.Utils.Constants;

import java.util.ArrayList;

/**
 * Created by AliAh on 25/06/2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatModel> itemList;


    public ChatListAdapter(Context context, ArrayList<ChatModel> itemList) {
        this.context = context;
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, parent, false);
        ChatListAdapter.ViewHolder viewHolder = new ChatListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatModel model = itemList.get(position);

        holder.username.setText(model.getName());
//        holder.message.setText(model.getText());
        if (model.getMessageType().equals(Constants.MESSAGE_TYPE_IMAGE)) {
            holder.message.setText("" + "\uD83D\uDCF7 Image");
        } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_AUDIO)) {
            holder.message.setText("" + "\uD83C\uDFB5 Audio");
        } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_DOCUMENT)) {
            holder.message.setText("" + "\uD83D\uDCC4 Document");
        } else if (model.getMessageType().equals(Constants.MESSAGE_TYPE_TEXT)) {
            holder.message.setText(model.getText());
        }


        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LiveChat.class);
                i.putExtra("username", model.getInitiator());
                i.putExtra("name", model.getName());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, message, time, count;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            count = itemView.findViewById(R.id.count);

        }
    }
}
