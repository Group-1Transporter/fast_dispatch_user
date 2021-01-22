package com.transporteruser.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.transporteruser.fragement.ConfirmFragment;
import com.transporteruser.fragement.CreateFragment;
import com.transporteruser.fragement.HistoryFragement;
import com.transporteruser.fragement.HistoryFragment;

public class TabAccessAdapter2 extends FragmentPagerAdapter {
    Context context;

    public TabAccessAdapter2(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new HistoryFragment();
                break;
            case 1:
                fragment = new CreateFragment();
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
                title = "Completed Loads";
                break;

            case 1:
                title = "Created Loads";
                break;
        }
        return title;
    }
}
