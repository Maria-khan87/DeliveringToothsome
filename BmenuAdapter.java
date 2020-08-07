package com.example.foodapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogNewItem;
import com.example.foodapp.dialogs.DialogUpdateProfile;
import com.example.foodapp.fragments.BawarchiMenu;

import java.util.List;

public class BmenuAdapter extends RecyclerView.Adapter<BmenuAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<MenuModel> datalist;
    BawarchiDashboard activity;
    BawarchiMenu bawarchiMenuFragment;
    public BmenuAdapter(BawarchiDashboard activity, List<MenuModel> data, BawarchiMenu bawarchiMenuFragment) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity=activity;
        datalist=data;
        this.bawarchiMenuFragment=bawarchiMenuFragment;
        //Log.i("menurspns",datalist.size()+"");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemimage;
        TextView name,price,txt_deactivated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemimage=(ImageView) itemView.findViewById(R.id.img_b_menu);
            name=(TextView) itemView.findViewById(R.id.txt_item_name);
            txt_deactivated=(TextView) itemView.findViewById(R.id.txt_deactivated);
            price=(TextView) itemView.findViewById(R.id.txt_price);

        }
    }


    @NonNull
    @Override
    public BmenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.rv_b_menu_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BmenuAdapter.ViewHolder viewHolder, int i) {


        viewHolder.name.setText(datalist.get(i).getItemName());
        viewHolder.price.setText(datalist.get(i).getItemPrice());
        viewHolder.itemimage.setImageBitmap(datalist.get(i).getItemPic());

        if(datalist.get(i).getDeleted().equals("1")){
            viewHolder.txt_deactivated.setVisibility(View.VISIBLE);
        }

        viewHolder.itemimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogNewItem dialogNewItem=new DialogNewItem(activity,datalist.get(i), StaticVariables.DIALOG_TYPE_UPDATE,bawarchiMenuFragment);
                dialogNewItem.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size() ;
    }
}
