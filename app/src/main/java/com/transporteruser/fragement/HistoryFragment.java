package com.transporteruser.fragement;

import android.app.ProgressDialog;
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
import com.transporteruser.adapters.CompletedLoadShowAdapter;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.ConfirmFragmentBinding;
import com.transporteruser.databinding.HistoryFragementBinding;
import com.transporteruser.databinding.HistoryFragmentBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    HistoryFragmentBinding binding;
    ProgressDialog pd;
    CompletedLoadShowAdapter adapter;
    UserService.UserApi userApi ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater);
        pd = new ProgressDialog(getContext());
        pd.setMessage("please wait...");
        pd.show();
        userApi = UserService.getUserApiInstance();
        Call<ArrayList<Lead>> call = userApi.getAllCompletedLeadsByUserId(FirebaseAuth.getInstance().getUid());
        call.enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                pd.dismiss();
                if(response.code() == 200){
                    ArrayList<Lead> leadList = response.body();
                    if(leadList.size()!= 0){
                        Collections.sort(leadList, (Comparator<? super Lead>) new Lead());
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.noData.setVisibility(View.GONE);
                        adapter = new CompletedLoadShowAdapter(getContext(),leadList);
                        binding.rv.setAdapter(adapter);
                        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    }else{
                        binding.rv.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }
}
