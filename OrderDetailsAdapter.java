package com.example.foodapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.R;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<String> datalist;

    BawarchiDashboard activity;
    String Dlg_Type;
    public OrderDetailsAdapter(BawarchiDashboard activity, List<String> data,String Dlg_Type) {
        this.mInflater = LayoutInflater.from(activity);
        datalist=data;
        this.activity=activity;
        this.Dlg_Type=Dlg_Type;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public  TextView btn_positive,btn_negitive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_positive=(TextView) itemView.findViewById(R.id.btn_positive);
            btn_negitive=(TextView) itemView.findViewById(R.id.btn_negative);
        }
    }


    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.item_order_details, viewGroup, false);

        return new OrderDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder viewHolder, int i) {


        if(Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_PENDING)){

            viewHolder.btn_positive.setText("Accept Order");
            viewHolder.btn_negitive.setText("Reject Order");
        }
        else if(Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_ACCEPTED)){

            viewHolder.btn_positive.setText("Start Cooking");
            viewHolder.btn_negitive.setText("Cancel Order");
        }

        else if(Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_COOKING)){

            viewHolder.btn_positive.setText("Mark Shiped");
            viewHolder.btn_negitive.setText("");
        }

        else if(Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_SHIPPED)){

            viewHolder.btn_positive.setText("");
            viewHolder.btn_negitive.setText("");
        }

        else if(Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_COMPLETED)){

            viewHolder.btn_positive.setText("");
            viewHolder.btn_negitive.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return datalist.size() ;
    }
}
