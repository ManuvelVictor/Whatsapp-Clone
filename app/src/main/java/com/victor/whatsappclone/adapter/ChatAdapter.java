package com.victor.whatsappclone.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.victor.whatsappclone.R;
import com.victor.whatsappclone.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;

    String incomingId;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String incomingId) {
        this.messageModels = messageModels;
        this.context = context;
        this.incomingId = incomingId;
    }

    final int OUTGOING_VIEW_TYPE = 1;
    final int INCOMING_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == OUTGOING_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_outgoing, parent, false);
            return new OutgoingViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_incoming, parent, false);
            return new IncomingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(v -> {

            new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure, you want to delete this message")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String outgoingRoom = FirebaseAuth.getInstance().getUid() + incomingId;
                        database.getReference().child("chats")
                                .child(outgoingRoom)
                                .child(messageModel.getMessageId())
                                .setValue(null);
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
            return false;
        });
        if (holder.getClass() == OutgoingViewHolder.class) {
            ((OutgoingViewHolder) holder).outgoingMsg.setText(messageModel.getMessage());

            Date date = new Date(messageModel.getTimestamp());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            String strDate = simpleDateFormat.format(date);
            ((OutgoingViewHolder) holder).outgoingTime.setText(strDate.toString());
        } else {
            ((IncomingViewHolder) holder).incomingMsg.setText(messageModel.getMessage());

            Date date = new Date(messageModel.getTimestamp());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            String strDate = simpleDateFormat.format(date);
            ((IncomingViewHolder) holder).incomingTime.setText(strDate.toString());
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return OUTGOING_VIEW_TYPE;
        } else {
            return INCOMING_VIEW_TYPE;
        }
    }

    public class IncomingViewHolder extends RecyclerView.ViewHolder {

        TextView incomingMsg, incomingTime;

        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);

            incomingMsg = itemView.findViewById(R.id.incomingText);
            incomingTime = itemView.findViewById(R.id.incomingTime);
        }
    }

    public class OutgoingViewHolder extends RecyclerView.ViewHolder {

        TextView outgoingMsg, outgoingTime;

        public OutgoingViewHolder(@NonNull View itemView) {
            super(itemView);

            outgoingMsg = itemView.findViewById(R.id.outgoingText);
            outgoingTime = itemView.findViewById(R.id.outgoingTime);
        }
    }
}
