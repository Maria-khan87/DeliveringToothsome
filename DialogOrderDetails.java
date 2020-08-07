package com.example.foodapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.OrderDetailsAdapter;
import com.example.foodapp.adapters.ScheduleItemsDetailsAdapter;
import com.example.foodapp.fragments.BawarchiOrders;

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

public class DialogOrderDetails extends Dialog {

    BawarchiDashboard activity;
    String Dlg_Type;
    ScheduleItemModel model;
    LinearLayout layout_pending, layout_accepted, layout_cooking,layout_shipped;
    Button btn_toship, btn_startcooking, btn_reject, btn_accept,btn_tocomplete;
    BawarchiOrders fragment;

    String statusStr;

    public DialogOrderDetails(BawarchiDashboard activity, BawarchiOrders fragment, ScheduleItemModel model, String Dlg_Type) {
        super(activity);
        this.activity = activity;
        this.Dlg_Type = Dlg_Type;
        this.model = model;
        this.fragment=fragment;
    }


    TextView txt_bawarchi_name, txt_ordernumber, txt_deadline, txt_mealtype, txt_shipping_address, txt_order_desc, txt_totalbill;
    RecyclerView rv_shedule_details;
    RelativeLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_b_order_details);


        topLayout = (RelativeLayout) findViewById(R.id.top);

        layout_pending = (LinearLayout) findViewById(R.id.layout_pending);
        layout_accepted = (LinearLayout) findViewById(R.id.layout_accepted);
        layout_cooking = (LinearLayout) findViewById(R.id.layout_cooking);
        layout_shipped= (LinearLayout) findViewById(R.id.layout_shipped);

        txt_bawarchi_name = (TextView) findViewById(R.id.txt_bawarchi_name);
        txt_ordernumber = (TextView) findViewById(R.id.txt_ordernumber);
        txt_deadline = (TextView) findViewById(R.id.txt_deadline);
        txt_mealtype = (TextView) findViewById(R.id.txt_mealtype);
        txt_shipping_address = (TextView) findViewById(R.id.txt_shipping_address);
        txt_order_desc = (TextView) findViewById(R.id.txt_order_desc);
        txt_totalbill = (TextView) findViewById(R.id.txt_totalbill);


        txt_bawarchi_name.setText(model.getFoodie_UserEmail());
        txt_ordernumber.setText(model.getOrder_ID());
        txt_deadline.setText(activity.formatDateTime(model.getScheduleDate().replace("T"," ")));
        txt_mealtype.setText(model.getScheduleSlot());
        txt_shipping_address.setText(model.getShippingAddress());
        txt_order_desc.setText(model.getOrderDesc());
        txt_totalbill.setText(model.getGrantTotal());


        rv_shedule_details = (RecyclerView) findViewById(R.id.rv_shedule_details);
        LinearLayoutManager lm = new LinearLayoutManager(activity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shedule_details.setLayoutManager(lm);


        String[] orderarray = model.getOrderedItemsDetail().split("#");

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < orderarray.length; i++) {

            list.add(orderarray[i]);

        }


        rv_shedule_details.setAdapter(new ScheduleItemsDetailsAdapter(activity, list));


        if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_PENDING)) {

            topLayout.setBackgroundColor(Color.RED);
            layout_pending.setVisibility(View.VISIBLE);
            layout_accepted.setVisibility(View.GONE);
            layout_cooking.setVisibility(View.GONE);
            layout_shipped.setVisibility(View.GONE);

        } else if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_ACCEPTED)) {
            layout_pending.setVisibility(View.GONE);
            layout_accepted.setVisibility(View.VISIBLE);
            layout_cooking.setVisibility(View.GONE);
            layout_shipped.setVisibility(View.GONE);
            topLayout.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
        } else if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_COOKING)) {
            layout_pending.setVisibility(View.GONE);
            layout_accepted.setVisibility(View.GONE);
            layout_cooking.setVisibility(View.VISIBLE);
            layout_shipped.setVisibility(View.GONE);
            topLayout.setBackgroundColor(activity.getResources().getColor(R.color.mt_orange));
        } else if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_SHIPPED)) {
            layout_pending.setVisibility(View.GONE);
            layout_accepted.setVisibility(View.GONE);
            layout_cooking.setVisibility(View.GONE);
            layout_shipped.setVisibility(View.VISIBLE);
            topLayout.setBackgroundColor(activity.getResources().getColor(R.color.mt_blue));
        } else if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_ORDER_COMPLETED)) {
            layout_pending.setVisibility(View.GONE);
            layout_accepted.setVisibility(View.GONE);
            layout_cooking.setVisibility(View.GONE);
            layout_shipped.setVisibility(View.GONE);
            topLayout.setBackgroundColor(activity.getResources().getColor(R.color.mt_green));

        }


        btn_toship = (Button) findViewById(R.id.btn_toship);
        btn_startcooking = (Button) findViewById(R.id.btn_startcooking);
        btn_reject = (Button) findViewById(R.id.btn_reject);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_tocomplete = (Button) findViewById(R.id.btn_tocomplete);

        btn_tocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(activity)
                        .setTitle("Complete Order")
                        .setMessage("Are you sure you want to move order in completed state?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    setStatus(model.getOrder_ID(),"5");
                                    statusStr="Order Completed";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(activity)
                        .setTitle("Accept Order")
                        .setMessage("Are you sure you want to accept the order?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    setStatus(model.getOrder_ID(),"1");
                                    statusStr="Order Accepted";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });

        btn_startcooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity)
                        .setTitle("Start Cooking")
                        .setMessage("Are you sure you want to start cooking?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    setStatus(model.getOrder_ID(),"2");
                                    statusStr="Order moved to cooking";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new AlertDialog.Builder(activity)
                        .setTitle("Reject Order")
                        .setMessage("Are you sure you want reject the order?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    setStatus(model.getOrder_ID(),"3");
                                    statusStr="Order Rejected";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });

        btn_toship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity)
                        .setTitle("Ready to Ship")
                        .setMessage("Set order Ready to ship?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    setStatus(model.getOrder_ID(),"4");
                                    statusStr="Order is set as Shipped";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });


    }


    void setStatus(String Order_ID, String Status) throws Exception{

        activity.dialog.setTitle("Please Wait");
        activity.dialog.show();

        String postUrl= StaticVariables.API_LINK_DEFAULT+"Order_UpdateOrderStatusByBawarchi";
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
                                            .setTitleText("Status Set Sucessfully")
                                            .setContentText(statusStr)
                                            .setConfirmText("OK")
                                            .show();
                                }
                            });
                            dismiss();
                            fragment.getBawarchiSchedule();

                        }
                        else {
                            Log.i("loginrspns","user not found");

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
