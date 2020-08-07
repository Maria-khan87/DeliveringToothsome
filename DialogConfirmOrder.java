package com.example.foodapp.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.CartItemsAdapter;
import com.example.foodapp.fragments.CookShowFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DialogConfirmOrder extends Dialog {

    FoodieDashboard activity;
    String bill, Address;
    DialogCartView dialogCartView;

    public DialogConfirmOrder(FoodieDashboard activity, String bill, String Address, DialogCartView dialogCartView) {
        super(activity);
        this.bill = bill;
        this.Address = Address;
        this.activity = activity;
        this.dialogCartView=dialogCartView;
    }


    EditText input_address,input_remarks;
    TextView btn_confirm_order, txt_bill_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_order);


        input_address = (EditText) findViewById(R.id.input_address);
        input_remarks = (EditText) findViewById(R.id.input_remarks);

        txt_bill_total = (TextView) findViewById(R.id.txt_bill_total);
        btn_confirm_order = (TextView) findViewById(R.id.btn_confirm_order);




        txt_bill_total.setText(bill);
        input_address.setText(Address);

//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);


//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                try {
//
//                    String time = ((selectedHour > 12) ? selectedHour % 12 : selectedHour) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((selectedHour >= 12) ? "PM" : "AM");
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        }, hour, minute, false);
//        mTimePicker.setTitle("Select Deadline");
//        mTimePicker.show();



        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activity.cart.size()>0) {

                    String orders = "";

                    for (int i = 0; i < activity.cart.size(); i++) {

                        orders = orders + ","  +activity.cart.get(i).getItemId() + "#" + activity.cart.get(i).getQuantity();

                    }


                    try {
                        placeOrder(orders.substring(1,orders.length()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });


    }





    void placeOrder(String OrderedItemsCSV) throws Exception{

        String postUrl= StaticVariables.API_LINK_DEFAULT+"Order_InsertOrder";

        JSONObject json=new JSONObject();
        json.put("ShippingAddress",input_address.getText().toString());
        json.put("ScheduleDate",activity.SelectedDate+" "+activity.SelectedTime);
        json.put("ScheduleSlot",activity.SelectedSlot);
        json.put("Foodie_Id",activity.UserID);
        json.put("Bawarchi_Id",dialogCartView.cookShowFragment.CookId);
        json.put("Status_From_Bawarchi","0");
        json.put("Status_From_Foodie","1");
        json.put("OrderedItemsCSV",OrderedItemsCSV);
        json.put("OrderDesc",input_remarks.getText().toString());

        String postBody=json.toString();


        Log.d("json",postBody);

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
                Log.i("loginrspns","failed to get response");



            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if(response.isSuccessful()){
                    try {

                        String responseString=convertStandardJSONString(response.body().string());

                        Log.i("loginrspns",responseString);



                        JSONObject recievedjson=new JSONObject(responseString);

                        JSONObject datajson=new JSONObject(convertStandardJSONString(recievedjson.getString("d")));

                        if (datajson.get("RetVal").equals("1")){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {


                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Order Placed Successfully")
                                                        .setConfirmText("OK")
                                                        .show();
                                            }
                                        });

                                        activity.getScheduledData();

                                        dismiss();
                                        dialogCartView.dismiss();



                                        activity.onBackPressed();
                                        activity.onBackPressed();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                        else {
                            Log.i("loginrspns","user not found");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Error")
                                            .setContentText("Wrong input")
                                            .setConfirmText("OK")
                                            .show();
                                }
                            });
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

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
//        data_json = data_json.replace("\"{", "{");
        //       data_json = data_json.replace("}\",", "},");
        //      data_json = data_json.replace("}\"", "}");
        return data_json;
    }


}