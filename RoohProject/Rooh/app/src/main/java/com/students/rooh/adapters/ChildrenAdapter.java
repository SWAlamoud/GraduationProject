package com.students.rooh.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.students.rooh.classes.Child;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.RowChildBinding;
import com.students.rooh.R;
import com.students.rooh.guardian.fragments.GuardianAddChildFragment;

import java.util.List;

public class ChildrenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Child> list;
    private final Context context;
    private final LayoutInflater inflater;
    private User user;

    public ChildrenAdapter(Context context, User user){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowChildBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.row_child, parent, false);
        return new CustomHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomHolder customHolder = (CustomHolder) holder;
        Child current = list.get(position);
        customHolder.binding.setRecord(current);

        if(current.drowning){
            customHolder.binding.imageIndicator.setBackgroundResource(R.drawable.red_circle);
        }
        else{
            customHolder.binding.imageIndicator.setBackgroundResource(R.drawable.green_circle);
        }

        customHolder.binding.btnDetails.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("CHILD", current);
            bundle.putParcelable("USER", user);
            Utils.replaceFragment((AppCompatActivity) context,
                    new GuardianAddChildFragment(), bundle,
                    R.id.fragmentHolder, true, false);
        });

        customHolder.binding.btnCopyId.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("ChildID", current.id);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "ID copied to clipboard", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setList( List<Child> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class CustomHolder extends RecyclerView.ViewHolder {
        private final RowChildBinding binding;

        public CustomHolder(RowChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
