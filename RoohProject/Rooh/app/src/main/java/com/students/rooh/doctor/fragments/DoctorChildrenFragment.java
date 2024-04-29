package com.students.rooh.doctor.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.students.rooh.R;
import com.students.rooh.adapters.DoctorChildrenAdapter;
import com.students.rooh.classes.Child;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.FragmentDoctorChildrenBinding;

import java.util.ArrayList;
import java.util.List;

public class DoctorChildrenFragment extends Fragment {

    private FragmentDoctorChildrenBinding binding;
    private DoctorChildrenAdapter adapter;
    private List<Child> fullList, filteredList;
    private ProgressDialog progressDialog;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null)
            container.removeAllViews();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_children,
                container, false);
        user = getArguments().getParcelable("USER");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) view.getContext())
                .getSupportFragmentManager().popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);

        progressDialog = Utils.createProgressDialog(requireContext());

        adapter = new DoctorChildrenAdapter(requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();

        binding.textSearch.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0){
                    filteredList = new ArrayList<>();
                    for(Child c : fullList){
                        if(c.name.contains(s)){
                            filteredList.add(c);
                        }
                    }
                    adapter.setList(filteredList);
                }
                else{
                    filteredList = fullList;
                    adapter.setList(filteredList);
                }
            }
        });
    }

    private void getData(){
        fullList = new ArrayList<>();
        filteredList = new ArrayList<>();
        progressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection(Child.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            Child child = new Child();
                            child.documentToObject(document);
                            fullList.add(child);
                        }
                        filteredList = fullList;
                        adapter.setList(filteredList);
                    }
                    else{
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
