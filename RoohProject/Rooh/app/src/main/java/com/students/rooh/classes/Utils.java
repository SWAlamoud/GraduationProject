package com.students.rooh.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Utils {
    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Loading...");
        return progressDialog;
    }

    public static void replaceFragment(FragmentActivity activity,
                                       Fragment destinationFragment,
                                       Bundle bundle,
                                       int fragmentLayoutID,
                                       boolean BackToCurrentFragment,
                                       boolean isHome) {
        String HomeTag = "HomeTag";

        if (bundle != null)
            destinationFragment.setArguments(bundle);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (BackToCurrentFragment) {
            transaction.addToBackStack(null);
        } else {
            Fragment f = activity.getSupportFragmentManager().findFragmentByTag(HomeTag);
            if (f != null) {
                transaction.remove(f);
            }
            ClearAllFragments(activity);
        }
        if (isHome)
            transaction.replace(fragmentLayoutID, destinationFragment, HomeTag);
        else
            transaction.replace(fragmentLayoutID, destinationFragment);
        transaction.commit();
    }

    public static void ClearAllFragments(FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
