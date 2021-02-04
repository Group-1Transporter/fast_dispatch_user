package com.transporteruser.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.MainActivity;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.Rating;
import com.transporteruser.bean.Transporter;
import com.transporteruser.bean.User;
import com.transporteruser.databinding.CompletedLoadBinding;
import com.transporteruser.databinding.RatingDialogBinding;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedLoadShowAdapter extends RecyclerView.Adapter<CompletedLoadShowAdapter.CompletedViewHolder> {
    ArrayList<Lead> leadList;
    UserService.UserApi userApi ;
    Context context;
    float ra = 2.5f;

    public CompletedLoadShowAdapter(Context context,ArrayList<Lead> leadList){
        this.leadList = leadList;
        userApi = UserService.getUserApiInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CompletedLoadBinding binding = CompletedLoadBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CompletedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompletedViewHolder holder, int position) {

        final Lead lead = leadList.get(position);
        userApi.getTransporter(lead.getDealLockedWith()).enqueue(new Callback<Transporter>() {
            @Override
            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                if(response.code() == 200){
                    holder.binding.tvTransporterName.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Transporter> call, Throwable t) {
                Toast.makeText(holder.itemView.getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.binding.tvrate.setText(lead.getAmount());

        holder.binding.tvDate.setText(lead.getDateOfCompletion());
        holder.binding.tvTypeOfmaterial.setText(lead.getTypeOfMaterial());
        holder.binding.tvQuntity.setText(lead.getWeight());
        holder.binding.morevertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(),holder.binding.morevertical);
                final Menu menu= popupMenu.getMenu();
                menu.add("Delete");
                menu.add("Rate Us");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String title = menuItem.getTitle().toString();
                        if (title.equalsIgnoreCase("Delete")){
                            final UserService.UserApi userApi = UserService.getUserApiInstance();
                            AlertDialog.Builder ab = new AlertDialog.Builder(holder.itemView.getContext());
                            ab.setTitle("DELETE");
                            ab.setMessage("Are You Sure ?");
                            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    userApi.deleteLeadById(lead.getLeadId()).enqueue(new Callback<ArrayList<Lead>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                                            if(response.code() == 200){
                                                Toast.makeText(holder.itemView.getContext(), "Lead Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(holder.itemView.getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                                            Toast.makeText(holder.itemView.getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            ab.setNegativeButton("No",null);
                            ab.show();
                        }
                       else if (title.equalsIgnoreCase("Rate Us")){

                           getRatingDialog(lead);

                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        String[] pickupAddress = lead.getPickUpAddress().split(",");
        String pickup= (pickupAddress[1]);
        String[] deliveryAddress = lead.getDeliveryAddress().split(",");
        String delivery= (deliveryAddress[1]);
        holder.binding.tvAddress.setText(pickup+" To "+delivery);
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }


    public class CompletedViewHolder extends RecyclerView.ViewHolder {
        CompletedLoadBinding binding;
        public CompletedViewHolder(CompletedLoadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private void getRatingDialog(final Lead lead ){
        final AlertDialog ab = new AlertDialog.Builder(context).create();
        final RatingDialogBinding ratingDialogBinding =  RatingDialogBinding.inflate(LayoutInflater.from(context));
        ab.setView(ratingDialogBinding.getRoot());
        ab.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ratingDialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ab.dismiss();
            }
        });
        ratingDialogBinding.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ra = rating;
            }
        });
        ratingDialogBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                final ProgressDialog pd = new ProgressDialog(context);
                pd.show();
                userApi.checkProfile(FirebaseAuth.getInstance().getUid()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200){
                            final Rating r = new Rating();
                            String feedback = ratingDialogBinding.etOverview.getText().toString();
                            r.setRating(""+ra);
                            long timestamp = Calendar.getInstance().getTimeInMillis();
                            r.setFeedback(feedback);
                            r.setUserId(lead.getUserId());
                            r.setTimestamp(timestamp);
                            r.setUserName(response.body().getName());
                            r.setImageUrl(response.body().getImageUrl());
                            userApi.getNumberOfRating(lead.getDealLockedWith()).enqueue(new Callback<ArrayList<Float>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Float>> call, Response<ArrayList<Float>> response) {
                                    if (response.code() ==200){
                                        ArrayList<Float>al = response.body();
                                        final float numberOfPersons = al.get(0) + 1;
                                        final float totalNumberOfRating = al.get(1)+ra;
                                        userApi.getTransporter(lead.getDealLockedWith()).enqueue(new Callback<Transporter>() {
                                            @Override
                                            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                                if(response.code() == 200){
                                                    Transporter transporter = response.body();
                                                    transporter.setRating(""+totalNumberOfRating/numberOfPersons);
                                                    userApi.updateTransporter(transporter).enqueue(new Callback<Transporter>() {
                                                        @Override
                                                        public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                                            pd.dismiss();
                                                            if(response.code() == 200){
                                                                userApi.createRating(lead.getDealLockedWith(),lead.getLeadId(),r).enqueue(new Callback<Rating>() {
                                                                    @Override
                                                                    public void onResponse(Call<Rating> call, Response<Rating> response) {
                                                                        if(response.code() == 200){
                                                                            lead.setRating(true);
                                                                            userApi.updateLead(lead).enqueue(new Callback<Lead>() {
                                                                                @Override
                                                                                public void onResponse(Call<Lead> call, Response<Lead> response) {
                                                                                    if(response.code() == 200){
                                                                                        Toast.makeText(context, "Thanks For Giving us Rating", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<Lead> call, Throwable t) {

                                                                                }
                                                                            });
                                                                            ab.dismiss();
                                                                        }else{
                                                                            Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<Rating> call, Throwable t) {
                                                                        pd.dismiss();
                                                                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Transporter> call, Throwable t) {
                                                            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Transporter> call, Throwable t) {
                                                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Float>> call, Throwable t) {
                                    Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(context , ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ab.show();
    }
}
