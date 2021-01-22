package com.transporteruser.fragement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.AddLoadActivity;
import com.transporteruser.ChatActivity;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.adapters.TabAccessAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.HomeFragementBinding;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomeFragement extends Fragment {
    TabAccessAdapter tabAccessAdapter;
    HomeFragementBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragementBinding.inflate(inflater);
        tabAccessAdapter = new TabAccessAdapter(getChildFragmentManager(),1,getContext());
        binding.viewPager.setAdapter(tabAccessAdapter);
        binding.tb.setupWithViewPager(binding.viewPager);

        binding.floatingActionButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddLoadActivity.class);
                getActivity().startActivity(i);
            }
        });
        return binding.getRoot();

    }


}
