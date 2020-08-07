package com.example.foodapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.HistoryModel;
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.ScheduleItemsDetailsAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DialogHistoryDetail extends Dialog {

    FoodieDashboard activity;
    HistoryModel historyModel;

    public DialogHistoryDetail(FoodieDashboard activity, HistoryModel historyModel) {
        super(activity);
        this.activity = activity;
        this.historyModel = historyModel;
    }


    TextView txt_bawarchi_name, txt_ordernumber, txt_deadline, txt_mealtype, txt_shipping_address, txt_order_desc, txt_totalbill;
    RecyclerView rv_shedule_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_history_detail);


        txt_bawarchi_name = (TextView) findViewById(R.id.txt_bawarchi_name);
        txt_ordernumber = (TextView) findViewById(R.id.txt_ordernumber);
        txt_deadline = (TextView) findViewById(R.id.txt_deadline);
        txt_mealtype = (TextView) findViewById(R.id.txt_mealtype);
        txt_shipping_address = (TextView) findViewById(R.id.txt_shipping_address);
        txt_order_desc = (TextView) findViewById(R.id.txt_order_desc);
        txt_totalbill = (TextView) findViewById(R.id.txt_totalbill);


        txt_bawarchi_name.setText(historyModel.getBawarchi_UserEmail());
        txt_ordernumber.setText(historyModel.getOrderNumber());
        txt_deadline.setText(activity.formatDateTime(historyModel.getScheduleDate().replace("T"," ")));
        txt_mealtype.setText(historyModel.getScheduleSlot());
        txt_shipping_address.setText(historyModel.getShippingAddress());
        txt_order_desc.setText(historyModel.getOrderDesc());
        txt_totalbill.setText(historyModel.getGrantTotal());


        rv_shedule_details=(RecyclerView) findViewById(R.id.rv_shedule_details);
        LinearLayoutManager lm=new LinearLayoutManager(activity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shedule_details.setLayoutManager(lm);


        String[] orderarray=historyModel.getOrderedItemsDetail().split("#");

        List<String> list=new ArrayList<String>();

        for(int i=0;i<orderarray.length;i++){

            list.add(orderarray[i]);

        }



        rv_shedule_details.setAdapter(new ScheduleItemsDetailsAdapter(activity,list));




    }





}