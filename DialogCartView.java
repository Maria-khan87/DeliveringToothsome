package com.example.foodapp.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.CartItemsAdapter;
import com.example.foodapp.adapters.OrderDetailsAdapter;
import com.example.foodapp.fragments.CookShowFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DialogCartView extends Dialog {

    FoodieDashboard activity;
    public CookShowFragment cookShowFragment;

    public DialogCartView(FoodieDashboard activity, CookShowFragment cookShowFragment) {
        super(activity);
        this.activity=activity;
        this.cookShowFragment=cookShowFragment;
    }


    RecyclerView recyclerViewOrders;
    CartItemsAdapter cartItemsAdapter;
    TextView btn_placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cart_show);




        recyclerViewOrders=(RecyclerView) findViewById(R.id.rv_cart);
        LinearLayoutManager lm=new LinearLayoutManager(activity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewOrders.setLayoutManager(lm);
        cartItemsAdapter=new CartItemsAdapter(activity,this,getOrdersList());
        recyclerViewOrders.setAdapter(cartItemsAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;


        btn_placeOrder=(TextView) findViewById(R.id.btn_place_order);
        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double total=0;
                for(int i=0;i<getOrdersList().size();i++){

                    total=total+(getOrdersList().get(i).getPrice()*getOrdersList().get(i).getQuantity());

                }




                DialogConfirmOrder dlg=new DialogConfirmOrder(activity,total+"",activity.profileModel.getFullAddress(),DialogCartView.this);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg.show();
            }
        });



    }


    public void removeItemFromCart(int position){
        activity.cart.remove(position);
        cartItemsAdapter.notifyItemChanged(position);
        cartItemsAdapter.notifyDataSetChanged();
        cookShowFragment.refreshCartBtn();
    }

    public void Quantity_Add(int position){
        activity.cart.get(position).setQuantity(activity.cart.get(position).getQuantity()+1);
        cartItemsAdapter.notifyItemChanged(position);
        cartItemsAdapter.notifyDataSetChanged();
    }


    public void Quantity_Remove(int position){

        if(activity.cart.get(position).getQuantity()-1>0) {

            activity.cart.get(position).setQuantity(activity.cart.get(position).getQuantity() - 1);
            cartItemsAdapter.notifyItemChanged(position);
            cartItemsAdapter.notifyDataSetChanged();
        }
    }


    public List<CartModel> getOrdersList(){


        return activity.cart;
    }
}