package com.students.rooh.guardian.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.students.rooh.R;
import com.students.rooh.classes.Chat;
import com.students.rooh.classes.Request;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.RequestStatus;
import com.students.rooh.common.fragments.MessagesFragment;
import com.students.rooh.databinding.FragmentGuardianDoctorDetailsBinding;

public class GuardianDoctorDetailsFragment extends Fragment {

    private FragmentGuardianDoctorDetailsBinding binding;
    private User user;
    private User doctor;
    private ProgressDialog progressDialog;
    private Chat chat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guardian_doctor_details,
                container, false);

        user = getArguments().getParcelable("USER");
        doctor = getArguments().getParcelable("DOCTOR");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = Utils.createProgressDialog(requireContext());

        binding.textName.setText((doctor.firstName.concat(" ").concat(doctor.lastName)));

        getChat();

        binding.btnStartChat.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("CHAT", chat);
            bundle.putParcelable("USER", user);
            Utils.replaceFragment((AppCompatActivity) requireContext(),
                    new MessagesFragment(), bundle,
                    R.id.fragmentHolder, true, false);
        });

        binding.btnSendChatRequest.setOnClickListener(view1 -> {
            Request request = new Request();
            request.doctorId = doctor.documentID;
            request.guardianId = user.documentID;
            request.status = RequestStatus.PENDING;
            request.guardianName = user.firstName + " " + user.lastName;

            FirebaseFirestore
                    .getInstance()
                    .collection(Request.COLLECTION_NAME)
                    .add(request.toMap());

            Toast.makeText(requireContext(), "Request sent to the doctor", Toast.LENGTH_LONG).show();
        });
    }

    private void getChat(){
        progressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection(Chat.COLLECTION_NAME)
                .whereEqualTo("guardianId",user.documentID)
                .whereEqualTo("doctorId",doctor.documentID)
                .get()
                .addOnCompleteListener(task -> {
                   progressDialog.dismiss();

                   if(task.isSuccessful()){
                       if(task.getResult().size() > 0){
                           DocumentSnapshot document = task.getResult().getDocuments().get(0);
                           chat = new Chat();
                           chat.documentToObject(document);
                           binding.btnStartChat.setVisibility(View.VISIBLE);
                       }
                       else{
                           binding.btnSendChatRequest.setVisibility(View.VISIBLE);
                       }
                   }
                   else{
                       progressDialog.dismiss();
                       Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
                });
    }
}
