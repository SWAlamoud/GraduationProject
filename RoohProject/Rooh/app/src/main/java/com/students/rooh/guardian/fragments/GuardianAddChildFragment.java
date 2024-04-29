package com.students.rooh.guardian.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.students.rooh.R;
import com.students.rooh.classes.Child;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.FragmentGuardianAddChildBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GuardianAddChildFragment extends Fragment {

    private FragmentGuardianAddChildBinding binding;
    private Child child;
    private User user;
    private ProgressDialog progressDialog;
    private boolean isNew = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guardian_add_child,
                container, false);

        user = getArguments().getParcelable("USER");
        if(getArguments() != null && getArguments().containsKey("CHILD")){
            child = getArguments().getParcelable("CHILD");
            isNew = false;
        }
        else{
            child = new Child();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = Utils.createProgressDialog(requireContext());
        binding.setChild(child);
        setSpinners();

        binding.switchOtherGuardian.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked){
                binding.textOtherGuardianName.setEnabled(true);
                binding.textOtherGuardianPhone.setEnabled(true);
            }
            else{
                binding.textOtherGuardianName.setText("");
                binding.textOtherGuardianPhone.setText("");
                binding.textOtherGuardianName.setEnabled(false);
                binding.textOtherGuardianPhone.setEnabled(false);
            }
        });

        binding.btnSave.setText((isNew ? "Add" : "Edit"));
        binding.btnSave.setOnClickListener(view1 -> {
            if (validateEntries()) {
                save();
            }
        });
    }

    private void setSpinners() {
        List<String> monthsList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthsList.add(String.valueOf(i));
        }
        String[] monthsArray = monthsList.toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, monthsArray);
        binding.spinnerMonth.setAdapter(spinnerArrayAdapter1);
        ////////////////////////////////////////////
        int startYear = 2000;
        List<String> yearsList = new ArrayList<>();
        for (int i = startYear; i <= (Calendar.getInstance().get(Calendar.YEAR)); i++) {
            yearsList.add(String.valueOf(i));
        }
        String[] yearsArray = yearsList.toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, yearsArray);
        binding.spinnerYear.setAdapter(spinnerArrayAdapter2);
        ////////////////////////////////////////////
        if(isNew){
            binding.spinnerMonth.setSelection(0);
            binding.spinnerYear.setSelection(0);
        }
        else{
            int monthPosition = spinnerArrayAdapter1.getPosition(String.valueOf(child.birthMonth));
            binding.spinnerMonth.setSelection(monthPosition);
            int yearPosition = spinnerArrayAdapter2.getPosition(String.valueOf(child.birthYear));
            binding.spinnerYear.setSelection(yearPosition);
        }
    }

    private boolean validateEntries() {
        if (binding.getChild().name == null || binding.getChild().name.isEmpty()) {
            binding.textChildName.setError("Required");
            return false;
        }
        if (binding.getChild().healthStatus == null || binding.getChild().healthStatus.isEmpty()) {
            binding.textHealthStatus.setError("Required");
            return false;
        }

        boolean hasOtherGuardian = binding.switchOtherGuardian.isChecked();
        if(hasOtherGuardian){
            if (binding.getChild().otherGuardianName == null || binding.getChild().otherGuardianName.isEmpty()) {
                binding.textOtherGuardianName.setError("Required");
                return false;
            }
            if (binding.getChild().otherGuardianPhone == null || binding.getChild().otherGuardianPhone.isEmpty()) {
                binding.textOtherGuardianPhone.setError("Required");
                return false;
            }
        }

        return true;
    }

    private void save(){
        child = binding.getChild();
        child.birthMonth = Integer.parseInt(binding.spinnerMonth.getSelectedItem().toString());
        child.birthYear = Integer.parseInt(binding.spinnerYear.getSelectedItem().toString());

        if(isNew)
            addNewChild();
        else
            editChild();
    }

    private void addNewChild(){
        child.guardianId = user.documentID;
        progressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection(Child.COLLECTION_NAME)
                .add(child.toMap())
                .addOnCompleteListener(task -> {
                   progressDialog.dismiss();

                   if(task.isSuccessful()){
                       Toast.makeText(requireContext(), "Child Added Successfully", Toast.LENGTH_LONG).show();
                       getFragmentManager().popBackStack();
                   }
                   else{
                       Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
                });
    }

    private void editChild(){
        progressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection(Child.COLLECTION_NAME)
                .document(child.id)
                .update(child.toMap())
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        Toast.makeText(requireContext(), "Child Edited Successfully", Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStack();
                    }
                    else{
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
