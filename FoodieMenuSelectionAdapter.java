package com.example.foodapp.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.ItemDescriptionDialog;
import com.example.foodapp.fragments.CookShowFragment;

import java.util.List;

public class FoodieMenuSelectionAdapter extends RecyclerView.Adapter<FoodieMenuSelectionAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<MenuModel> datalist;

    FoodieDashboard activity;
    CookShowFragment fragment;

    public FoodieMenuSelectionAdapter(FoodieDashboard activity, CookShowFragment fragment, List<MenuModel> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist = data;
        this.fragment=fragment;
        this.activity = activity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView btn_addtocart, txt_price, txt_item_name;
        public ImageView img_b_menu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            btn_addtocart = (TextView) itemView.findViewById(R.id.btn_addtocart);

            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_item_name = (TextView) itemView.findViewById(R.id.txt_item_name);

            img_b_menu=(ImageView) itemView.findViewById(R.id.img_b_menu);
        }
    }


    @NonNull
    @Override
    public FoodieMenuSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.f_menu_select_item, viewGroup, false);

        return new FoodieMenuSelectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodieMenuSelectionAdapter.ViewHolder viewHolder, int i) {



        viewHolder.txt_item_name.setText(datalist.get(i).getItemName());
        viewHolder.txt_price.setText(datalist.get(i).getItemPrice());
        viewHolder.img_b_menu.setImageBitmap(datalist.get(i).getItemPic());

        viewHolder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment.addItemtoCart(datalist.get(i).getItem_ID(),datalist.get(i).getItemName(),Double.parseDouble(datalist.get(i).getItemPrice()));

            }
        });


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ItemDescriptionDialog dlg = new ItemDescriptionDialog(activity,datalist.get(i).getItemName(),datalist.get(i).getItemDesc());
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
