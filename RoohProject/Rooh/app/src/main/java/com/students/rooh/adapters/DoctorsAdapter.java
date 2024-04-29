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
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.RowDoctorBinding;
import com.students.rooh.guardian.fragments.GuardianDoctorDetailsFragment;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> list;
    private final Context context;
    private final LayoutInflater inflater;
    private User user;

    public DoctorsAdapter(Context context, User user){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowDoctorBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.row_doctor, parent, false);
        return new CustomHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomHolder customHolder = (CustomHolder) holder;
        User current = list.get(position);
        customHolder.binding.textDoctorName.setText((current.firstName.concat(" ").concat(current.lastName)));

        customHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("DOCTOR", current);
            bundle.putParcelable("USER", user);
            Utils.replaceFragment((AppCompatActivity) context,
                    new GuardianDoctorDetailsFragment(), bundle,
                    R.id.fragmentHolder, true, false);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setList(List<User> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class CustomHolder extends RecyclerView.ViewHolder {
        private final RowDoctorBinding binding;

        public CustomHolder(RowDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
