package com.example.foodapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.HistoryModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogHistoryDetail;
import com.example.foodapp.dialogs.DialogOrderDetails;
import com.example.foodapp.dialogs.DialogRating;
import com.example.foodapp.dialogs.DialogScheduleDetails;
import com.example.foodapp.fragments.FoodieHistory;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FoodieHistoryAdapter extends RecyclerView.Adapter<FoodieHistoryAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<HistoryModel> datalist;
    FoodieHistory fragment;

    FoodieDashboard activity;
    public FoodieHistoryAdapter(FoodieDashboard activity, FoodieHistory fragment, List<HistoryModel> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist=data;
        this.activity=activity;
        this.fragment=fragment;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txt_status,txt_scheduledate,txt_orderdate,txt_bawarchiname,txt_ordermunber;
        public MaterialRatingBar ratingBar;
        public LinearLayout ratinglayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
            txt_status=(TextView) itemView.findViewById(R.id.txt_status);
            txt_scheduledate=(TextView) itemView.findViewById(R.id.txt_scheduledate);
            txt_orderdate=(TextView) itemView.findViewById(R.id.txt_orderdate);
            txt_bawarchiname=(TextView) itemView.findViewById(R.id.txt_bawarchiname);
            txt_ordermunber=(TextView) itemView.findViewById(R.id.txt_ordermunber);

            ratingBar=(MaterialRatingBar) itemView.findViewById(R.id.ratingbar);
            ratinglayout=(LinearLayout) itemView.findViewById(R.id.ratinglayout);

        }
    }


    @NonNull
    @Override
    public FoodieHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.foodie_history_item, viewGroup, false);

        return new FoodieHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodieHistoryAdapter.ViewHolder viewHolder, int i) {


        viewHolder.txt_bawarchiname.setText(datalist.get(i).getBawarchi_UserEmail());
        viewHolder.txt_orderdate.setText(activity.formatDateTime(datalist.get(i).getOrderDate().replace("T"," ")));
        viewHolder.txt_scheduledate.setText(activity.formatDateTime(datalist.get(i).getScheduleDate().replace("T"," ")));
        viewHolder.txt_ordermunber.setText(datalist.get(i).getOrderNumber());

        if(datalist.get(i).getStatus_From_Bawarchi().equals("3")||datalist.get(i).getStaus_From_Foodie().equals("3"))
        {
            viewHolder.txt_status.setText("Canceled");
            viewHolder.cardView.setCardBackgroundColor(Color.RED);
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("5")){
            viewHolder.txt_status.setText("Completed");
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_green));
            viewHolder.ratinglayout.setVisibility(View.VISIBLE);
            viewHolder.ratingBar.setRating(datalist.get(i).getRating());
            if(datalist.get(i).getRating()>0){


            }
            else{
                viewHolder.ratinglayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogRating dlg=new DialogRating(activity,fragment,datalist.get(i));
                        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dlg.show();


                    }
                });

            }
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("1")){
            viewHolder.txt_status.setText("Approved");
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("2")){
            viewHolder.txt_status.setText("Cooking");
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_orange));
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("4")){
            viewHolder.txt_status.setText("Shipped");
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_blue));
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("0")){
            viewHolder.txt_status.setText("Pending");
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.colorlight));
        }


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHistoryDetail dlg=new DialogHistoryDetail(activity,datalist.get(i));
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return datalist.size() ;
    }
}

