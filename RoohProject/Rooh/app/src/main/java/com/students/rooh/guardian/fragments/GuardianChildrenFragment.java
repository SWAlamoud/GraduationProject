package com.students.rooh.guardian.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.students.rooh.R;
import com.students.rooh.adapters.ChildrenAdapter;
import com.students.rooh.classes.Child;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.FragmentGuardianChildrenBinding;

import java.util.ArrayList;
import java.util.List;

public class GuardianChildrenFragment extends Fragment {

    private FragmentGuardianChildrenBinding binding;
    private ChildrenAdapter adapter;
    private ProgressDialog progressDialog;
    private User user;
    private List<Child> children;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null)
            container.removeAllViews();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guardian_children,
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

        adapter = new ChildrenAdapter(requireContext(), user);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        binding.btnAddNewChild.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("USER", user);
            Utils.replaceFragment((AppCompatActivity) requireContext(),
                    new GuardianAddChildFragment(), bundle,
                    R.id.fragmentHolder, true, false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void getData(){
        progressDialog.show();
        children = new ArrayList<>();

        FirebaseFirestore
                .getInstance()
                .collection(Child.COLLECTION_NAME)
                .whereEqualTo("guardianId", user.documentID)
                .get()
                .addOnCompleteListener(task -> {
                   progressDialog.dismiss();

                   if(task.isSuccessful()){
                       int redCount = 0;
                       for(QueryDocumentSnapshot document : task.getResult()){
                           Child child = new Child();
                           child.documentToObject(document);

                           if(child.drowning)
                               redCount++;
                           children.add(child);
                       }
                       adapter.setList(children);
                       displayCounts(redCount);
                   }
                   else{
                       Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
                });
    }

    private void displayCounts(int count){
        binding.textGreenCount.setText(String.valueOf(children.size() - count));
        binding.textRedCount.setText(String.valueOf(count));

        if(count > 0)
            showAlertDialog(count);
    }

    private void showAlertDialog(int count){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_alert);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = dialog.findViewById(R.id.labelTitleCount);

        text.setText(("There are ".concat(String.valueOf(count)).concat(" drowning children")));
        Button btnCall = dialog.findViewById(R.id.btnCall);
        Button btnCancel = dialog.findViewById(R.id.btnNoNeed);

        btnCall.setOnClickListener(view -> {
            dialog.dismiss();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "997"));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        10);
            } else {
                try {
                    startActivity(callIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}
