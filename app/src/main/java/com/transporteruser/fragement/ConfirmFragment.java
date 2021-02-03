package com.transporteruser.fragement;

import android.app.AlertDialog;
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
import com.transporteruser.ChatActivity;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.ConfirmFragmentBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmFragment extends Fragment {
    ConfirmFragmentBinding binding;
    HomeAdapter adapter ;
    ArrayList<Lead> list;
    UserService.UserApi userApi ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ConfirmFragmentBinding.inflate(inflater);
        userApi = UserService.getUserApiInstance();
        userApi.getConfirmLeads(FirebaseAuth.getInstance().getUid()).enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                if(response.code() == 200){
                    list = response.body();
                    if (list.size() != 0) {
                        Collections.sort(list,  new Lead());
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.noData.setVisibility(View.GONE);
                        adapter = new HomeAdapter(list,true);
                        binding.rv.setAdapter(adapter);
                        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter.onConfirmedClick(new HomeAdapter.OnConfirmedListner() {
                            @Override
                            public void onClick(final Lead lead, final int position, String status) {
                                if(status.equalsIgnoreCase("cancel") && lead.getStatus().equalsIgnoreCase("confirmed")){
                                    final UserService.UserApi userApi = UserService.getUserApiInstance();
                                    AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                                    ab.setTitle("DELETE");
                                    ab.setMessage("Are You Sure ?");
                                    ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            userApi.deleteLeadById(lead.getLeadId()).enqueue(new Callback<ArrayList<Lead>>() {
                                                @Override
                                                public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                                                    Toast.makeText(getContext(), ""+response.code(), Toast.LENGTH_SHORT).show();
                                                    if(response.code() == 200){
                                                        Toast.makeText(getContext(), "Lead Deleted", Toast.LENGTH_SHORT).show();
                                                        list.remove(position);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    else
                                                        Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                                @Override
                                                public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "toast : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                    ab.setNegativeButton("No",null);
                                    ab.show();
                                }else if (status.equalsIgnoreCase("Chat With Client")){
                                    Intent i = new Intent(getContext(), ChatActivity.class);
                                    i.putExtra("transporterId",lead.getDealLockedWith());
                                    startActivity(i);
                                }
                            }
                        });
                    }else {
                        binding.rv.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });



        return binding.getRoot();
    }
}
