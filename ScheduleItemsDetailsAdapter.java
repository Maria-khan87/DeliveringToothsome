package com.example.foodapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogCartView;

import java.util.List;

public class ScheduleItemsDetailsAdapter extends RecyclerView.Adapter<ScheduleItemsDetailsAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<String> datalist;


    public ScheduleItemsDetailsAdapter(FoodieDashboard activity, List<String> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist = data;

    }

    public ScheduleItemsDetailsAdapter(BawarchiDashboard activity, List<String> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist = data;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_itemname, txt_quantity, txt_unitprice   , txt_totalprice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_itemname = (TextView) itemView.findViewById(R.id.txt_itemname);
            txt_quantity = (TextView) itemView.findViewById(R.id.txt_quantity);
            txt_unitprice = (TextView) itemView.findViewById(R.id.txt_unitprice);
            txt_totalprice = (TextView) itemView.findViewById(R.id.txt_totalprice);


        }
    }


    @NonNull
    @Override
    public ScheduleItemsDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.item_schedule_dialog_item, viewGroup, false);

        return new ScheduleItemsDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleItemsDetailsAdapter.ViewHolder viewHolder, int i) {

        String[] str=datalist.get(i).split("\\?");
        viewHolder.txt_itemname.setText(str[3]);
        viewHolder.txt_quantity.setText(str[2]);
        viewHolder.txt_unitprice.setText(str[4]);
        viewHolder.txt_totalprice.setText(str[5]);

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
