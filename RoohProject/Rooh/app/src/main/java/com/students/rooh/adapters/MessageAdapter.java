package com.students.rooh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.students.rooh.R;
import com.students.rooh.classes.Message;
import com.students.rooh.databinding.RowCurrentMessageBinding;
import com.students.rooh.databinding.RowOtherMessageBinding;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> list;
    private String id;
    private final LayoutInflater inflater;

    public MessageAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).senderID.equals(id) ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == 0) {
            RowCurrentMessageBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.row_current_message, parent, false);
            viewHolder = new CurrentCustomHolder(binding);
        } else {
            RowOtherMessageBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.row_other_message, parent, false);
            viewHolder = new OtherCustomHolder(binding);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            CurrentCustomHolder h = (CurrentCustomHolder) holder;
            h.binding.setMessage(list.get(position));
        }
        else{
            OtherCustomHolder h = (OtherCustomHolder) holder;
            h.binding.setMessage(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setList(List<Message> list){
        if(list != null){
            this.list = list;
            notifyDataSetChanged();
        }
    }

    public void setId(String id){
        this.id = id;
    }

    public void addItem(Message message){
        list.add(message);
        notifyItemInserted(list.size() - 1);
    }

    public static class CurrentCustomHolder extends RecyclerView.ViewHolder {
        private final RowCurrentMessageBinding binding;

        public CurrentCustomHolder(RowCurrentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class OtherCustomHolder extends RecyclerView.ViewHolder {
        private final RowOtherMessageBinding binding;

        public OtherCustomHolder(RowOtherMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
