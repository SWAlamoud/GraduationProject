package com.students.rooh.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.students.rooh.R;
import com.students.rooh.classes.Chat;
import com.students.rooh.classes.Request;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.RequestStatus;
import com.students.rooh.common.fragments.MessagesFragment;
import com.students.rooh.databinding.RowRequestBinding;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Request> list;
    private final Context context;
    private User user;
    private final LayoutInflater inflater;
    private ProgressDialog progressDialog;

    public RequestAdapter(Context context, User user){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.user = user;
        progressDialog = Utils.createProgressDialog(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRequestBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.row_request, parent, false);
        return new CustomHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomHolder customHolder = (CustomHolder) holder;
        Request current = list.get(position);

        customHolder.binding.textGuardianName.setText(current.guardianName);

        customHolder.binding.btnDecline.setOnClickListener(view -> {
            current.status = RequestStatus.REJECTED;
            FirebaseFirestore
                    .getInstance()
                    .collection(Request.COLLECTION_NAME)
                    .document(current.id)
                    .update("status", RequestStatus.toInteger(RequestStatus.REJECTED));
            Toast.makeText(context, "Request Rejected", Toast.LENGTH_LONG).show();

            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });

        customHolder.binding.btnAccept.setOnClickListener(view -> {
            current.status = RequestStatus.ACCEPTED;
            FirebaseFirestore
                    .getInstance()
                    .collection(Request.COLLECTION_NAME)
                    .document(current.id)
                    .update("status", RequestStatus.toInteger(RequestStatus.ACCEPTED));

            createChat(current);

            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
    }

    private void createChat(Request request){
        Chat chat = new Chat();
        chat.doctorId = user.documentID;
        chat.doctorName = user.firstName + " " + user.lastName;
        chat.guardianId = request.guardianId;
        chat.guardianName = request.guardianName;

        progressDialog.show();

        FirebaseFirestore
                .getInstance()
                .collection(Chat.COLLECTION_NAME)
                .add(chat.toMap())
                .addOnCompleteListener(task -> {
                   progressDialog.dismiss();

                   if(task.isSuccessful()){
                       chat.id = task.getResult().getId();

                       Bundle bundle = new Bundle();
                       bundle.putParcelable("CHAT", chat);
                       bundle.putParcelable("USER", user);
                       Utils.replaceFragment((AppCompatActivity) context,
                               new MessagesFragment(), bundle,
                               R.id.fragmentHolder, true, false);
                   }
                   else{
                       Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
                });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setList( List<Request> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class CustomHolder extends RecyclerView.ViewHolder {
        private final RowRequestBinding binding;

        public CustomHolder(RowRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
