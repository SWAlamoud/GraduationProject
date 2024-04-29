package com.students.rooh.guardian.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.students.rooh.R;
import com.students.rooh.adapters.DoctorsAdapter;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.UserType;
import com.students.rooh.databinding.FragmentGuardianDoctorsBinding;

import java.util.ArrayList;
import java.util.List;

public class GuardianDoctorsFragment extends Fragment {

    private FragmentGuardianDoctorsBinding binding;
    private DoctorsAdapter adapter;
    private ProgressDialog progressDialog;
    private User user;
    private List<User> list;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guardian_doctors,
                container, false);

        user = getArguments().getParcelable("USER");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = Utils.createProgressDialog(requireContext());

        adapter = new DoctorsAdapter(requireContext(), user);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void getData(){
        progressDialog.show();
        list = new ArrayList<>();

        FirebaseFirestore
                .getInstance()
                .collection(User.COLLECTION_NAME)
                .whereEqualTo("userType", UserType.toInteger(UserType.DOCTOR))
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            User doctor = new User();
                            doctor.documentToObject(document);
                            list.add(doctor);
                        }
                        adapter.setList(list);
                    }
                    else{
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
