package com.students.rooh.guardian.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.students.rooh.R;
import com.students.rooh.databinding.FragmentGuardianEvaluationTestBinding;

public class GuardianEvaluationTestFragment extends Fragment {

    private FragmentGuardianEvaluationTestBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guardian_evaluation_test,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnResult.setOnClickListener(view1 -> {
            binding.resultContainer.setVisibility(View.VISIBLE);

            int count = getCheckedCount();
            if(count > 3)
                binding.textResult.setText("The situation requires going to the hospital immediately");
            else if(count > 0)
                binding.textResult.setText("The situation requires the child to get rested");
            else
                binding.textResult.setText("The situation is good the child can continue swimming");
        });
    }

    private int getCheckedCount(){
        int count = 0;

        if(binding.checkbox1.isChecked())
            count++;
        if(binding.checkbox2.isChecked())
            count++;
        if(binding.checkbox3.isChecked())
            count++;
        if(binding.checkbox4.isChecked())
            count++;
        if(binding.checkbox5.isChecked())
            count++;
        if(binding.checkbox6.isChecked())
            count++;

        return count;
    }
}
