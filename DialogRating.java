package com.example.foodapp.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.resources.MaterialResources;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.HistoryModel;
import com.example.foodapp.R;
import com.example.foodapp.fragments.FoodieHistory;

import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DialogRating extends Dialog {

    FoodieDashboard activity;
    HistoryModel historyModel;
    FoodieHistory fragment;

    public DialogRating(FoodieDashboard activity,FoodieHistory fragment, HistoryModel historyModel) {
        super(activity);
        this.activity=activity;
        this.historyModel=historyModel;
        this.fragment=fragment;

    }

    MaterialRatingBar ratingBar;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rating);

        ratingBar=(MaterialRatingBar) findViewById(R.id.ratingbar);
        editText=(EditText) findViewById(R.id.input_remarks);
        button=(Button) findViewById(R.id.btn_rate);

        ratingBar.setRating(historyModel.getRating());



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ratingBar.getRating()==0 || editText.getText().equals("")) {

                    if (ratingBar.getRating() == 0) {
                        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Please Rate atleast a star")
                                .show();
                    }
                    if (editText.getText().equals("")) {
                        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Please write something in remarks")
                                .show();
                    }
                }
                else{
                    try{
                        placeRating();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }


            }
        });

    }




    void placeRating() throws Exception{



        String postUrl= StaticVariables.API_LINK_DEFAULT+"Order_InsertRatingRemarks";

        JSONObject json=new JSONObject();
        json.put("Order_ID",historyModel.getOrderNumber());
        json.put("Rating",ratingBar.getRating());
        json.put("RatingRemarks",editText.getText().toString());

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


                                        fragment.refreshHistory(fragment.spinner_months.getSelectedItemPosition()+1);

                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Order Sucessfully Rated")
                                                        .setConfirmText("OK")
                                                        .show();
                                            }
                                        });


                                        dismiss();



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
                                            .setContentText("connection error")
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