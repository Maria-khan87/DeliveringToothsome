package com.example.foodapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.R;
import com.example.foodapp.dialogs.DialogScheduleDetails;
import com.example.foodapp.fragments.ShowBawarchizFrag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class FoodieScheduleAdapter extends RecyclerView.Adapter<FoodieScheduleAdapter.ViewHolder> {


    LayoutInflater mInflater;
    List<LinkedHashMap<String,ScheduleItemModel>> datalist;
    FoodieDashboard activity;
    String[] dateArray;

    public FoodieScheduleAdapter(FoodieDashboard activity, List<LinkedHashMap<String,ScheduleItemModel>> data,String[] dateArray) {
        this.mInflater = LayoutInflater.from(activity);
        datalist = data;
        this.activity = activity;
        this.dateArray=dateArray;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, btn_bf_book, btn_ln_book, btn_dn_book,txt_bf_city, txt_ln_city, txt_dn_city;
        public RelativeLayout layout_bf_np, layout_ln_np, layout_dn_np;
        public LinearLayout layout_bf_plnd, layout_ln_plnd, layout_dn_plnd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_f_s_dayname);
            btn_bf_book = (TextView) itemView.findViewById(R.id.btn_bf_book);
            btn_ln_book = (TextView) itemView.findViewById(R.id.btn_ln_book);
            btn_dn_book = (TextView) itemView.findViewById(R.id.btn_dn_book);


            txt_bf_city= (TextView) itemView.findViewById(R.id.fdi_bf_plnd_city);
            txt_ln_city= (TextView) itemView.findViewById(R.id.fdi_ln_plnd_city);
            txt_dn_city= (TextView) itemView.findViewById(R.id.fdi_dn_plnd_city);

            layout_bf_np = (RelativeLayout) itemView.findViewById(R.id.layout_fshd_bf_np);
            layout_ln_np = (RelativeLayout) itemView.findViewById(R.id.layout_fshd_ln_np);
            layout_dn_np = (RelativeLayout) itemView.findViewById(R.id.layout_fshd_dn_np);

            layout_bf_plnd = (LinearLayout) itemView.findViewById(R.id.layout_fshd_bf_plnd);
            layout_ln_plnd = (LinearLayout) itemView.findViewById(R.id.layout_fshd_ln_plnd);
            layout_dn_plnd = (LinearLayout) itemView.findViewById(R.id.layout_fshd_dn_plnd);

        }
    }


    @NonNull
    @Override
    public FoodieScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.foodie_schedule_item, viewGroup, false);

        return new FoodieScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodieScheduleAdapter.ViewHolder viewHolder, int i) {


        try {

            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dt=dateArray[i];
            Date date  = inFormat.parse(dt);

            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String dayname = outFormat.format(date);

            String title=dayname;




        switch (i) {
            case 0:
                FirstDay(viewHolder,datalist.get(i));
                viewHolder.title.setText(title+" "+dt);

                break;

            case 1:
                viewHolder.title.setText(title+" "+dt);
                SecondDay(viewHolder,datalist.get(i));
                break;

            case 2:
                viewHolder.title.setText(title+" "+dt);
                ThirdDay(viewHolder,datalist.get(i));
                break;

            case 3:
                viewHolder.title.setText(title+" "+dt);
                ForthDay(viewHolder,datalist.get(i));
                break;

            case 4:
                viewHolder.title.setText(title+" "+dt);
                FifthDay(viewHolder,datalist.get(i));
                break;

            case 5:
                viewHolder.title.setText(title+" "+dt);
                SixthDay(viewHolder,datalist.get(i));
                break;

            case 6:
                viewHolder.title.setText(title+" "+dt);
                SeventhDay(viewHolder,datalist.get(i));
                break;


        }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.btn_bf_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.SelectedSlot="BreakFast";
                activity.SelectedDate=dateArray[i];
                activity.SelectedTime="08:00 AM";

                activity.frame_main_foodie.setVisibility(View.VISIBLE);
                activity.main_title.setText("Select Cook");
                activity.getSupportFragmentManager().beginTransaction().add(
                        R.id._frame_main_foodie, new ShowBawarchizFrag(activity), "bawarchis"
                ).addToBackStack(null)
                        .commit();
            }
        });
        viewHolder.btn_ln_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.SelectedSlot="Lunch";
                activity.SelectedDate=dateArray[i];
                activity.SelectedTime="01:00 PM";

                activity.frame_main_foodie.setVisibility(View.VISIBLE);
                activity.main_title.setText("Select Cook");
                activity.getSupportFragmentManager().beginTransaction().add(
                        R.id._frame_main_foodie, new ShowBawarchizFrag(activity), "bawarchis"
                ).addToBackStack(null)
                        .commit();
            }
        });
        viewHolder.btn_dn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.SelectedSlot="Dinner";
                activity.SelectedTime="09:00 PM";
                activity.SelectedDate=dateArray[i];
                activity.frame_main_foodie.setVisibility(View.VISIBLE);
                activity.main_title.setText("Select Cook");
                activity.getSupportFragmentManager().beginTransaction().add(
                        R.id._frame_main_foodie, new ShowBawarchizFrag(activity), "bawarchis"
                ).addToBackStack(null)
                        .commit();
            }
        });


    }

    private void FirstDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {

        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });

        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }


    }

    private void SecondDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {
        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });

        }

    }

    private void ThirdDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {
        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }
    }

    private void ForthDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {
        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }
    }

    private void FifthDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {
        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }
    }

    private void SixthDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {
        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

    }

    private void SeventhDay(FoodieScheduleAdapter.ViewHolder viewHolder,LinkedHashMap<String,ScheduleItemModel> mainMap) {
        if (mainMap.get("BreakFast")==null){
            viewHolder.layout_bf_np.setVisibility(View.VISIBLE);
            viewHolder.layout_bf_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_bf_np.setVisibility(View.GONE);
            viewHolder.layout_bf_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_bf_city.setText(mainMap.get("BreakFast").getShippingAddress());
            viewHolder.layout_bf_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("BreakFast"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Lunch")==null){
            viewHolder.layout_ln_np.setVisibility(View.VISIBLE);
            viewHolder.layout_ln_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_ln_np.setVisibility(View.GONE);
            viewHolder.layout_ln_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_ln_city.setText(mainMap.get("Lunch").getShippingAddress());
            viewHolder.layout_ln_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Lunch"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }

        if (mainMap.get("Dinner")==null){
            viewHolder.layout_dn_np.setVisibility(View.VISIBLE);
            viewHolder.layout_dn_plnd.setVisibility(View.GONE);
        }
        else{
            viewHolder.layout_dn_np.setVisibility(View.GONE);
            viewHolder.layout_dn_plnd.setVisibility(View.VISIBLE);
            viewHolder.txt_dn_city.setText(mainMap.get("Dinner").getShippingAddress());
            viewHolder.layout_dn_plnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogScheduleDetails dlg=new DialogScheduleDetails(activity,mainMap.get("Dinner"));
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
