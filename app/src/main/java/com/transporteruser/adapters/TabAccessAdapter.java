package com.transporteruser.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.transporteruser.fragement.ConfirmFragment;
import com.transporteruser.fragement.CreateFragment;

public class TabAccessAdapter extends FragmentPagerAdapter {
    Context context;

    public TabAccessAdapter(@NonNull FragmentManager fm, int behavior,Context context) {
        super(fm, behavior);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new CreateFragment();
                break;
            case 1:
                fragment = new ConfirmFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Current Loads";
                break;

            case 1:
                title = "Confirmed Loads";
                break;
        }
        return title;
    }
}
