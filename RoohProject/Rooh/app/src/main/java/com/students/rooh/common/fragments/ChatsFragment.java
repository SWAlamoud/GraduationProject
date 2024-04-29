package com.students.rooh.common.fragments;

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
import com.students.rooh.adapters.ChatAdapter;
import com.students.rooh.classes.Chat;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.UserType;
import com.students.rooh.databinding.FragmentChatsBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatAdapter adapter;
    private User user;
    private List<Chat> list;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats,
                container, false);

        user = getArguments().getParcelable("USER");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = Utils.createProgressDialog(requireContext());

        adapter = new ChatAdapter(requireContext(), user);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        getChats();
    }

    private void getChats() {
        list = new ArrayList<>();
        progressDialog.show();

        String condition = user.userType == UserType.GUARDIAN ? "guardianId" : "doctorId";

        FirebaseFirestore
                .getInstance()
                .collection(Chat.COLLECTION_NAME)
                .whereEqualTo(condition, user.documentID)
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Chat chat = new Chat();
                            chat.documentToObject(document);
                            if(user.userType == UserType.GUARDIAN)
                                chat.otherName = chat.doctorName;
                            else
                                chat.otherName = chat.guardianName;
                            list.add(chat);
                        }
                        adapter.setList(list);
                    } else {
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
