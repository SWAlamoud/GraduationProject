package com.students.rooh.doctor.fragments;

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
import com.students.rooh.adapters.RequestAdapter;
import com.students.rooh.classes.Request;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.RequestStatus;
import com.students.rooh.databinding.FragmentDoctorRequestsBinding;

import java.util.ArrayList;
import java.util.List;

public class DoctorRequestsFragment extends Fragment {

    private FragmentDoctorRequestsBinding binding;
    private RequestAdapter adapter;
    private User user;
    private List<Request> list;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_requests,
                container, false);

        user = getArguments().getParcelable("USER");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = Utils.createProgressDialog(requireContext());

        adapter = new RequestAdapter(requireContext(), user);
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
                .collection(Request.COLLECTION_NAME)
                .whereEqualTo("doctorId", user.documentID)
                .whereEqualTo("status", RequestStatus.toInteger(RequestStatus.PENDING))
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            Request request = new Request();
                            request.documentToObject(document);
                            list.add(request);
                        }
                        adapter.setList(list);
                    }
                    else{
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
