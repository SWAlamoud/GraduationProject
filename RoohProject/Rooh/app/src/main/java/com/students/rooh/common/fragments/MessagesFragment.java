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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.students.rooh.R;
import com.students.rooh.adapters.MessageAdapter;
import com.students.rooh.classes.Chat;
import com.students.rooh.classes.Message;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.FragmentMessagesBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private Chat chat;
    private User user;
    private MessageAdapter adapter;
    private List<Message> list;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages,
                container, false);

        user = getArguments().getParcelable("USER");
        chat = getArguments().getParcelable("CHAT");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = Utils.createProgressDialog(requireContext());

        binding.btnSend.setOnClickListener(view1 -> sendMessage());

        adapter = new MessageAdapter(requireContext());
        adapter.setId(user.documentID);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        getMessages();
    }

    private void getMessages() {
        list = new ArrayList<>();
        progressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection(Chat.COLLECTION_NAME)
                .document(chat.id)
                .collection(Message.COLLECTION_NAME)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Message message = new Message();
                            message.documentToObject(document);
                            list.add(message);
                        }
                        adapter.setList(list);
                    } else {
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendMessage() {
        if (binding.getNewMessage() != null && !binding.getNewMessage().isEmpty()) {
            Message message =
                    new Message(user.documentID, binding.getNewMessage(), new Date());
            binding.setNewMessage("");

            adapter.addItem(message);
            binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);

            FirebaseFirestore
                    .getInstance()
                    .collection(Chat.COLLECTION_NAME)
                    .document(chat.id)
                    .collection(Message.COLLECTION_NAME)
                    .add(message.toMap());
        }
    }
}
