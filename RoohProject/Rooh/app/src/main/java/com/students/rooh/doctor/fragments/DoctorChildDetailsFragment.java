package com.students.rooh.doctor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.students.rooh.R;
import com.students.rooh.classes.Child;
import com.students.rooh.databinding.FragmentDoctorChildDetailsBinding;

public class DoctorChildDetailsFragment extends Fragment {

    private FragmentDoctorChildDetailsBinding binding;
    private Child child;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_child_details,
                container, false);

        child = getArguments().getParcelable("CHILD");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setChild(child);

        binding.textMonth.setText(String.valueOf(child.birthMonth));
        binding.textYear.setText(String.valueOf(child.birthYear));
    }
}
