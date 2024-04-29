package com.students.rooh.guardian;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.students.rooh.R;
import com.students.rooh.classes.User;
import com.students.rooh.classes.Utils;
import com.students.rooh.databinding.ActivityGuardianHomeBinding;
import com.students.rooh.common.fragments.ChatsFragment;
import com.students.rooh.guardian.fragments.GuardianChildrenFragment;
import com.students.rooh.guardian.fragments.GuardianDoctorsFragment;
import com.students.rooh.guardian.fragments.GuardianEvaluationTestFragment;

public class GuardianHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGuardianHomeBinding binding = DataBindingUtil.
                setContentView(this, R.layout.activity_guardian_home);

        User user = getIntent().getParcelableExtra("USER");

        Bundle bundle = new Bundle();
        bundle.putParcelable("USER", user);

        Utils.replaceFragment(this,
                new GuardianChildrenFragment(), bundle,
                binding.fragmentHolder.getId(), false, true);

        binding.menu.setOnItemSelectedListener(item -> {
            Fragment current = getSupportFragmentManager().
                    findFragmentById(binding.fragmentHolder.getId());

            if (item.getItemId() == R.id.menuItemChildren) {
                Utils.ClearAllFragments(this);
            } else if (item.getItemId() == R.id.menuItemEvaluationTest) {
                if (!(current instanceof GuardianEvaluationTestFragment))
                    Utils.replaceFragment(GuardianHomeActivity.this,
                            new GuardianEvaluationTestFragment(), bundle,
                            binding.fragmentHolder.getId(), true, false);
            } else if (item.getItemId() == R.id.menuItemDoctors) {
                if (!(current instanceof GuardianDoctorsFragment))
                    Utils.replaceFragment(GuardianHomeActivity.this,
                            new GuardianDoctorsFragment(), bundle,
                            binding.fragmentHolder.getId(), true, false);
            } else if (item.getItemId() == R.id.menuItemChat) {
                if (!(current instanceof ChatsFragment))
                    Utils.replaceFragment(GuardianHomeActivity.this,
                            new ChatsFragment(), bundle,
                            binding.fragmentHolder.getId(), true, false);
            }

            return true;
        });


        binding.menu.setSelectedItemId(R.id.menuItemChildren);
    }
}