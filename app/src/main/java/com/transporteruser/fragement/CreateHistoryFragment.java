package com.transporteruser.fragement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.transporteruser.adapters.CreatedLeadShowAdapter;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.CreateFragmentBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateHistoryFragment extends Fragment {
    CreateFragmentBinding binding;
    UserService.UserApi userApi;
    ArrayList<Lead> list;
    CreatedLeadShowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CreateFragmentBinding.inflate(inflater);
        userApi = UserService.getUserApiInstance();
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("please wait...");
        pd.show();
        userApi.getAllCreatedLeadsByUserId(FirebaseAuth.getInstance().getUid()).enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                pd.dismiss();
                if(response.code() == 200){
                    list = response.body();
                    if (list.size() != 0) {
                        Collections.sort(list, (Comparator<? super Lead>) new Lead());
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.noData.setVisibility(View.GONE);
                        adapter = new CreatedLeadShowAdapter(list);
                        binding.rv.setAdapter(adapter);
                        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter.OnHomeClick(new HomeAdapter.OnHomeRecyclerListner() {
                            @Override
                            public void onClick(Lead lead, int position, String status) {
                                deleteLeadCreated(lead, position, status);
                            }
                        });
                    }else {
                        binding.rv.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }
                }
                else  if(response.code()==404){
                    Toast.makeText(getContext(), "error 404", Toast.LENGTH_SHORT).show();
                }
                else if(response.code()==500){
                    Toast.makeText(getContext(), "500", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        return binding.getRoot();
    }

    private void deleteLeadCreated(final Lead lead, final int position, String status) {
        if(status.equalsIgnoreCase("delete")){
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
        }else if(status.equalsIgnoreCase("Edit")){
            Intent i = new Intent(getContext(), AddLoadActivity.class);
            i.putExtra("lead", (Serializable) lead);
            startActivity(i);
        }
    }

}
