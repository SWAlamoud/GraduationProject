package com.students.rooh.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.students.rooh.R;
import com.students.rooh.classes.Chat;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.common.fragments.MessagesFragment;
import com.students.rooh.databinding.RowChatBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chat> list;
    private final Context context;
    private User user;
    private final LayoutInflater inflater;

    public ChatAdapter(Context context, User user){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowChatBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_chat, parent, false);
        return new CustomHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomHolder customHolder = (CustomHolder) holder;
        Chat current = list.get(position);
        customHolder.binding.setChat(current);

        customHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("USER", user);
            bundle.putParcelable("CHAT", current);
            Utils.replaceFragment((AppCompatActivity) context,
                    new MessagesFragment(), bundle,
                    R.id.fragmentHolder, true, false);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setList(List<Chat> list){
        if(list != null){
            this.list = list;
            notifyDataSetChanged();
        }
    }

    public static class CustomHolder extends RecyclerView.ViewHolder {
        private final RowChatBinding binding;

        public CustomHolder(RowChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
