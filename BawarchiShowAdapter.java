package com.example.foodapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.BawarchizModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogOrderDetails;
import com.example.foodapp.fragments.CookShowFragment;
import com.example.foodapp.fragments.ShowBawarchizFrag;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BawarchiShowAdapter extends RecyclerView.Adapter<BawarchiShowAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<BawarchizModel> datalist;

    FoodieDashboard activity;
    public BawarchiShowAdapter(FoodieDashboard activity, List<BawarchizModel> data) {
        this.mInflater = LayoutInflater.from(activity);
        datalist=data;
        this.activity=activity;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

       public CardView cardView;
       public TextView txt_b_name;
       public ImageView img_bshow;
       public MaterialRatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView=(CardView) itemView.findViewById(R.id.cardview);
            txt_b_name=(TextView ) itemView.findViewById(R.id.txt_b_name);
            img_bshow=(ImageView) itemView.findViewById(R.id.img_bshow);
            ratingBar=(MaterialRatingBar) itemView.findViewById(R.id.ratingbar);

        }
    }


    @NonNull
    @Override
    public BawarchiShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.item_bshow, viewGroup, false);

        return new BawarchiShowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BawarchiShowAdapter.ViewHolder viewHolder, int i) {


        String name=datalist.get(i).getName();
        if(name.length()>12){
            viewHolder.txt_b_name.setText(name.substring(0,11));
        }
        else{
            viewHolder.txt_b_name.setText(name);
        }


        viewHolder.img_bshow.setImageBitmap(datalist.get(i).getPic());
        viewHolder.ratingBar.setRating(datalist.get(i).getRating());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.main_title.setText("Select Menu");
                activity.getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id._frame_main_foodie,new CookShowFragment(activity
                                ,datalist.get(i).getName()
                                ,datalist.get(i).getFullName(),datalist.get(i).getBawarchiId(),datalist.get(i).getPic())).commit();

                //activity.BawarchiId=datalist.get(i).

            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size() ;
    }
}
