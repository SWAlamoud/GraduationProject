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
import com.students.rooh.classes.Child;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.RowDoctorChildBinding;
import com.students.rooh.doctor.fragments.DoctorChildDetailsFragment;

import java.util.List;

public class DoctorChildrenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Child> list;
    private final Context context;
    private final LayoutInflater inflater;

    public DoctorChildrenAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowDoctorChildBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.row_doctor_child, parent, false);
        return new CustomHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomHolder customHolder = (CustomHolder) holder;
        Child current = list.get(position);

        customHolder.binding.textChildName.setText(current.name);

        customHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("CHILD", current);
            Utils.replaceFragment((AppCompatActivity) context,
                    new DoctorChildDetailsFragment(), bundle,
                    R.id.fragmentHolder, true, false);
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
        private final RowDoctorChildBinding binding;

        public CustomHolder(RowDoctorChildBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
