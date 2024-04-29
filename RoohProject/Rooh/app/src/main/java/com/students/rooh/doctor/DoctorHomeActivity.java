package com.students.rooh.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.students.rooh.R;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.common.fragments.ChatsFragment;
import com.students.rooh.databinding.ActivityDoctorHomeBinding;
import com.students.rooh.doctor.fragments.DoctorChildrenFragment;
import com.students.rooh.doctor.fragments.DoctorRequestsFragment;

public class DoctorHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDoctorHomeBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_doctor_home);

        User user = getIntent().getParcelableExtra("USER");

        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);

        Utils.replaceFragment(this,
                new DoctorChildrenFragment(), bundle,
                binding.fragmentHolder.getId(), false, true);

        binding.menu.setOnItemSelectedListener(item -> {
            Fragment current = getSupportFragmentManager().
                    findFragmentById(binding.fragmentHolder.getId());

            if (item.getItemId() == R.id.menuItemChildren) {
                Utils.ClearAllFragments(this);
            } else if (item.getItemId() == R.id.menuItemRequests) {
                if (!(current instanceof DoctorRequestsFragment))
                    Utils.replaceFragment(DoctorHomeActivity.this,
                            new DoctorRequestsFragment(), bundle,
                            binding.fragmentHolder.getId(), true, false);
            }  else if (item.getItemId() == R.id.menuItemChat) {
                if (!(current instanceof ChatsFragment))
                    Utils.replaceFragment(DoctorHomeActivity.this,
                            new ChatsFragment(), bundle,
                            binding.fragmentHolder.getId(), true, false);
            }

            return true;
        });


        binding.menu.setSelectedItemId(R.id.menuItemChildren);
    }
}