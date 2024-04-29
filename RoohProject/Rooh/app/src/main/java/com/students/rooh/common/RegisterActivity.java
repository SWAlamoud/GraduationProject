package com.students.rooh.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.students.rooh.R;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.classes.enums.UserType;
import com.students.rooh.databinding.ActivityRegisterBinding;
import com.students.rooh.doctor.DoctorHomeActivity;
import com.students.rooh.guardian.GuardianHomeActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        progressDialog = Utils.createProgressDialog(this);

        binding.btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnRegister.setOnClickListener(view -> {
            if (validateEntries()) {
                register();
            }
        });

        binding.setUser(new User());
    }

    private boolean validateEntries() {
        if (binding.getUser().firstName == null || binding.getUser().firstName.isEmpty()) {
            binding.textFirstName.setError("Required");
            return false;
        }
        if (binding.getUser().lastName == null || binding.getUser().lastName.isEmpty()) {
            binding.textLastName.setError("Required");
            return false;
        }
        if (binding.getUser().email == null || binding.getUser().email.isEmpty()) {
            binding.textEmail.setError("Required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.getUser().email).matches()) {
            binding.textEmail.setError("Invalid Email Address");
            return false;
        }
        if (binding.getEnteredPassword() == null || binding.getEnteredPassword().isEmpty()) {
            binding.textPassword.setError("Required");
            return false;
        }
        if (binding.getEnteredPassword().length() < 6) {
            binding.textPassword.setError("Password length must be at least 6 characters");
            return false;
        }
        if (binding.getEnteredConfirmPassword() == null || binding.getEnteredConfirmPassword().isEmpty()) {
            binding.textConfirmPassword.setError("Required");
            return false;
        }
        if (!binding.getEnteredPassword().equals(binding.getEnteredConfirmPassword())) {
            binding.textConfirmPassword.setError("Must be equal to Password");
            return false;
        }
        if(binding.spinnerSelectType.getSelectedItemPosition() == 0){
            Toast.makeText(this, "You must select User Type", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void register() {
        progressDialog.show();

        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(
                        binding.getUser().email,
                        binding.getEnteredPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addUser();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUser() {
        User user = binding.getUser();
        user.userType = UserType.fromInteger(binding.spinnerSelectType.getSelectedItemPosition());
        user.uid = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore
                .getInstance()
                .collection(User.COLLECTION_NAME)
                .add(user.toMap())
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        user.documentID = task.getResult().getId();

                        Intent intent;
                        if(user.userType == UserType.DOCTOR)
                            intent = new Intent(this, DoctorHomeActivity.class);
                        else
                            intent = new Intent(this, GuardianHomeActivity.class);
                        intent.putExtra("USER", user);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}