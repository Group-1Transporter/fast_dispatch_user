package com.transporteruser.adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.transporteruser.BidShowActivity;
import com.transporteruser.TransporterProfile;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Bid;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.Transporter;
import com.transporteruser.databinding.BidShowBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidShowAdapter extends RecyclerView.Adapter<BidShowAdapter.BidsViewHolder> {
    Lead lead;
    ArrayList<Bid> bidList;
    OnRecycleViewClickListener listener;

    public BidShowAdapter(ArrayList<Bid> bidList,Lead lead) {
        this.bidList = bidList;
        this.lead = lead;
    }

    @NonNull
    @Override
    public BidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BidShowBinding binding = BidShowBinding.inflate(LayoutInflater.from(parent.getContext()));
        BidsViewHolder bidsViewHolder = new BidsViewHolder(binding);
        return bidsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BidsViewHolder holder, int position) {

        final Bid bid = bidList.get(position);
        Transporter t=new Transporter();
        Intent in=new Intent();
        in.putExtra("bid",  bid);
        holder.binding.transporterName.setText(bid.getTransporterName());
        holder.binding.amount.setText((String.valueOf(bid.getAmount())));
        holder.binding.profileShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(holder.itemView.getContext(), TransporterProfile.class);
                in.putExtra("bid",  bid);
                holder.itemView.getContext().startActivity(in);
            }
        });
        String transporterId=bid.getTransporterId();
        UserService.UserApi userApi = UserService.getUserApiInstance();
        userApi.getCurrentTransporter(transporterId).enqueue(new Callback<Transporter>() {

            @Override
            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                if (response.code()==200){
                    Transporter transporter = response.body();
                    Picasso.get().load(transporter.getImageUrl()).into(holder.binding.profileShow);
                    holder.binding.transporterAddress.setText(transporter.getAddress());
                }
            }

            @Override
            public void onFailure(Call<Transporter> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }


    public class BidsViewHolder extends RecyclerView.ViewHolder {
        BidShowBinding binding;

        public BidsViewHolder(BidShowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onClickListener(bidList.get(position), position);
                    }
                }
            });
        }
    }

    public interface OnRecycleViewClickListener {
        public void onClickListener(Bid bid, int position);

    }

    public void onBidShowClickListener(OnRecycleViewClickListener listener) {
        this.listener = listener;
    }


}
