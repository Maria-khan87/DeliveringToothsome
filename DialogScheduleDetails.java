package com.example.foodapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.MainActivity;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.CartItemsAdapter;
import com.example.foodapp.adapters.ScheduleItemsDetailsAdapter;
import com.example.foodapp.fragments.CookShowFragment;

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

public class DialogScheduleDetails extends Dialog {

    FoodieDashboard activity;
    ScheduleItemModel scheduleItemModel;

    public DialogScheduleDetails(FoodieDashboard activity, ScheduleItemModel scheduleItemModel) {
        super(activity);
        this.activity = activity;
        this.scheduleItemModel = scheduleItemModel;
    }


    TextView txt_bawarchi_name, txt_ordernumber, txt_deadline, txt_mealtype, txt_shipping_address, txt_order_desc, txt_totalbill, btn_cancel_order;
    RecyclerView rv_shedule_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule_details);


        txt_bawarchi_name = (TextView) findViewById(R.id.txt_bawarchi_name);
        txt_ordernumber = (TextView) findViewById(R.id.txt_ordernumber);
        txt_deadline = (TextView) findViewById(R.id.txt_deadline);
        txt_mealtype = (TextView) findViewById(R.id.txt_mealtype);
        txt_shipping_address = (TextView) findViewById(R.id.txt_shipping_address);
        txt_order_desc = (TextView) findViewById(R.id.txt_order_desc);
        txt_totalbill = (TextView) findViewById(R.id.txt_totalbill);


        txt_bawarchi_name.setText(scheduleItemModel.getBawarchi_UserEmail());
        txt_ordernumber.setText(scheduleItemModel.getOrder_ID());
        txt_deadline.setText(activity.formatDateTime(scheduleItemModel.getScheduleDate().replace("T"," ")));
        txt_mealtype.setText(scheduleItemModel.getScheduleSlot());
        txt_shipping_address.setText(scheduleItemModel.getShippingAddress());
        txt_order_desc.setText(scheduleItemModel.getOrderDesc());
        txt_totalbill.setText(scheduleItemModel.getGrantTotal());

        rv_shedule_details=(RecyclerView) findViewById(R.id.rv_shedule_details);
        LinearLayoutManager lm=new LinearLayoutManager(activity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shedule_details.setLayoutManager(lm);


        String[] orderarray=scheduleItemModel.getOrderedItemsDetail().split("#");

        List<String> list=new ArrayList<String>();

        for(int i=0;i<orderarray.length;i++){

            list.add(orderarray[i]);

        }



        rv_shedule_details.setAdapter(new ScheduleItemsDetailsAdapter(activity,list));


        btn_cancel_order = (TextView) findViewById(R.id.btn_cancel_order);
        btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity)
                        .setTitle("Cancel Order")
                        .setMessage("Are you sure you want to cancel the order?")


                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    requestCancel(scheduleItemModel.getOrder_ID(),"3");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });
        if (scheduleItemModel.getStatus_From_Bawarchi().equals("5")){
            btn_cancel_order.setVisibility(View.GONE);
        }
        else {
            btn_cancel_order.setVisibility(View.VISIBLE);
        }

    }



    void requestCancel(String Order_ID, String Status) throws Exception{

        activity.dialog.setTitle("Please Wait");
        activity.dialog.show();

        String postUrl= StaticVariables.API_LINK_DEFAULT+"Order_UpdateOrderStatusByFoodie";
        //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json=new JSONObject();
        json.put("Order_ID",Order_ID);
        json.put("Status",Status);

        String postBody=json.toString();

        //Log.d("loginargs",postBody);

        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                activity.dialog.dismiss();
                Log.i("loginrspns","failed to get response");


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Server not Responding")
                                .setContentText("Please try later")
                                .show();
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                activity.dialog.dismiss();

                if(response.isSuccessful()){
                    try {

                        String responseString=response.body().string();

                        Log.i("loginrspns",responseString);


                        JSONObject recievedjson=new JSONObject(responseString);

                        JSONObject datajson=new JSONObject(recievedjson.getString("d"));

                        if (datajson.get("RetVal").equals("1")){

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Order Canceled")
                                            .setContentText("You Order has been Canceled Sucessfully")
                                            .setConfirmText("OK")
                                            .show();
                                }
                            });
                            dismiss();
                            activity.getScheduledData();

                        }
                        else {
                            Log.i("loginrspns","user not found");
                            new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error Canceleing Order")
                                    .setContentText("Please Try Later")
                                    .setConfirmText("OK")
                                    .show();
                        }


                    } catch (Exception  e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Log.i("loginrspns","error getting response");



                }
            }
        });
    }



}