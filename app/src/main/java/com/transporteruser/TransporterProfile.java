package com.transporteruser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Bid;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.Transporter;
import com.transporteruser.databinding.ActivityCreateProfileBinding;
import com.transporteruser.databinding.AddLoadBinding;
import com.transporteruser.databinding.TransporterProfileBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransporterProfile extends AppCompatActivity {
    TransporterProfileBinding binding;
    Lead lead;
    Bid bid;
    ArrayList<Bid> bidList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TransporterProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
       Intent i = getIntent();
        bid = (Bid) i.getSerializableExtra("bid");
        String transporterId=bid.getTransporterId();
        UserService.UserApi userApi = UserService.getUserApiInstance();
        userApi.getCurrentTransporter(transporterId);
        userApi.getCurrentTransporter(transporterId).enqueue(new Callback<Transporter>() {
            @Override
            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                if (response.code()==200){
                    Transporter transporter = response.body();
                    Picasso.get().load(transporter.getImageUrl()).into(binding.civ);
                    binding.address.setText(transporter.getAddress());
                    binding.userName.setText(transporter.getName());
                    binding.phoneNumber.setText(transporter.getContactNumber());
                    binding.category.setText(transporter.getType());
                    binding.toolbar.setTitle("Transporter Profile");

                }
            }

            @Override
            public void onFailure(Call<Transporter> call, Throwable t) {

            }
        });


    }
}
