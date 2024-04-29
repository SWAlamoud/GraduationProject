package com.students.rooh.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.students.rooh.R;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.UserType;
import com.students.rooh.databinding.ActivityLoginBinding;
import com.students.rooh.doctor.DoctorHomeActivity;
import com.students.rooh.guardian.GuardianHomeActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        progressDialog = Utils.createProgressDialog(this);

        binding.btnLogin.setOnClickListener(view -> {
            if(binding.getEnteredEmailAddress() == null || binding.getEnteredEmailAddress().isEmpty())
                binding.textEmail.setError("Required");
            else if(binding.getEnteredPassword() == null || binding.getEnteredPassword().isEmpty())
                binding.textPassword.setError("Required");
            else{
                signIn();
            }
        });

        binding.btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }private void signIn(){
        progressDialog.show();

        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(
                        binding.getEnteredEmailAddress(),
                        binding.getEnteredPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getUserData();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getUserData() {
        FirebaseFirestore
                .getInstance()
                .collection(User.COLLECTION_NAME)
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            User user = new User();
                            user.documentToObject(task.getResult().getDocuments().get(0));

                            Intent intent;
                            if(user.userType == UserType.DOCTOR)
                                intent = new Intent(this, DoctorHomeActivity.class);
                            else
                                intent = new Intent(this, GuardianHomeActivity.class);
                            intent.putExtra("USER", user);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}