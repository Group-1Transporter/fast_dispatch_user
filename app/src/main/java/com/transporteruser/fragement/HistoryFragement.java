package com.transporteruser.fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.AddLoadActivity;
import com.transporteruser.R;
import com.transporteruser.adapters.CompletedLoadShowAdapter;
import com.transporteruser.adapters.CreatedLeadShowAdapter;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.adapters.TabAccessAdapter;
import com.transporteruser.adapters.TabAccessAdapter2;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.HistoryFragementBinding;
import com.transporteruser.databinding.HistoryFragmentBinding;
import com.transporteruser.databinding.HomeFragementBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
HistoryFragement extends Fragment {

    TabAccessAdapter2 tabAccessAdapter;
    HistoryFragementBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryFragementBinding.inflate(inflater);
        tabAccessAdapter = new TabAccessAdapter2(getChildFragmentManager(),1,getContext());
        binding.viewPager.setAdapter(tabAccessAdapter);
        binding.tb.setupWithViewPager(binding.viewPager);


        return binding.getRoot();

    }

}
