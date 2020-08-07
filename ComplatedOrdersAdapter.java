package com.example.foodapp.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogOrderDetails;
import com.example.foodapp.fragments.BawarchiOrders;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ComplatedOrdersAdapter extends RecyclerView.Adapter<ComplatedOrdersAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<ScheduleItemModel> datalist;
    BawarchiOrders fragment;

    BawarchiDashboard activity;
    public ComplatedOrdersAdapter(BawarchiDashboard activity, BawarchiOrders fragment, List<ScheduleItemModel> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist=data;
        this.activity=activity;
        this.fragment=fragment;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

            public CardView cardView;
        public TextView txt_scheduledate,txt_orderdate,txt_bawarchiname,txt_ordermunber,txt_remarks;
        public MaterialRatingBar ratingbar;
        public LinearLayout ratinglayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
            txt_scheduledate=(TextView) itemView.findViewById(R.id.txt_scheduledate);
            txt_orderdate=(TextView) itemView.findViewById(R.id.txt_orderdate);
            txt_bawarchiname=(TextView) itemView.findViewById(R.id.txt_bawarchiname);
            txt_ordermunber=(TextView) itemView.findViewById(R.id.txt_ordermunber);
            ratingbar=(MaterialRatingBar) itemView.findViewById(R.id.ratingbar);
            ratinglayout=(LinearLayout) itemView.findViewById(R.id.ratinglayout);
            txt_remarks=(TextView) itemView.findViewById(R.id.txt_remarks);
        }
    }


    @NonNull
    @Override
    public ComplatedOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.b_order_item, viewGroup, false);

        return new ComplatedOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplatedOrdersAdapter.ViewHolder viewHolder, int i) {


        viewHolder.txt_bawarchiname.setText(datalist.get(i).getFoodie_UserEmail());
        viewHolder.txt_orderdate.setText(activity.formatDateTime(datalist.get(i).getOrderDate().replace("T"," ")));
        viewHolder.txt_scheduledate.setText(activity.formatDateTime(datalist.get(i).getScheduleDate().replace("T"," ")));

        viewHolder.ratinglayout.setVisibility(View.VISIBLE);
        viewHolder.ratingbar.setRating(datalist.get(i).getRating());
        viewHolder.txt_remarks.setText(datalist.get(i).getRemarks());


        viewHolder.txt_ordermunber.setText(datalist.get(i).getOrder_ID());

        if(datalist.get(i).getStatus_From_Bawarchi().equals("3")||datalist.get(i).getStaus_From_Foodie().equals("3"))
        {
            viewHolder.cardView.setCardBackgroundColor(Color.RED);
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("5")){
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_green));
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("1")){
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("2")){
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_orange));
        }
        else if(datalist.get(i).getStatus_From_Bawarchi().equals("4")){
            viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_blue));
        }

        viewHolder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.mt_green));
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogOrderDetails dlg=new DialogOrderDetails(activity,fragment, datalist.get(i),StaticVariables.DIALOG_TYPE_ORDER_COMPLETED);
                dlg.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size() ;
    }
}
