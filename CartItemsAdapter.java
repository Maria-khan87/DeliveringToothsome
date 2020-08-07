package com.example.foodapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogCartView;

import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<CartModel> datalist;
    DialogCartView dialogCartView;
    FoodieDashboard activity;

    public CartItemsAdapter(FoodieDashboard activity, DialogCartView dialogCartView, List<CartModel> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist = data;
        this.dialogCartView=dialogCartView;
        this.activity = activity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_remove, txt_orderquantity, txt_price, item_cart_name;
        public ImageView btn_plus,btn_minus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_remove = (TextView) itemView.findViewById(R.id.btn_removefromcart);
            txt_orderquantity = (TextView) itemView.findViewById(R.id.txt_orderquantity);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            item_cart_name = (TextView) itemView.findViewById(R.id.item_cart_name);

            btn_plus=(ImageView) itemView.findViewById(R.id.qty_add);
            btn_minus=(ImageView) itemView.findViewById(R.id.qty_sbtrct);

        }
    }


    @NonNull
    @Override
    public CartItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.item_cart, viewGroup, false);

        return new CartItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsAdapter.ViewHolder viewHolder, int i) {


        viewHolder.txt_orderquantity.setText(datalist.get(i).getQuantity()+"");
        viewHolder.txt_price.setText(datalist.get(i).getPrice()+"");
        viewHolder.item_cart_name.setText(datalist.get(i).getItemName());

        viewHolder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCartView.Quantity_Add(i);
            }
        });


        viewHolder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCartView.Quantity_Remove(i);
            }
        });


        viewHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         dialogCartView.removeItemFromCart(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
