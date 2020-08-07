package com.example.foodapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.foodapp.Constants.StaticVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String MY_PREFS_NAME = "myprefname";
    TextView link_signup, btn_alreadylogin;
    RadioButton radio_foodie, radio_bawarchi;
    boolean isAlreadyLogin = false;
    String username = "";
    Button btn_login;
    EditText et_username,et_password;
    CheckBox chk_rememberme;

    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient=new OkHttpClient();
        dialog=new SpotsDialog.Builder()
                .setContext(MainActivity.this)
                .build();

        link_signup = (TextView) findViewById(R.id.link_signup);
        link_signup.setOnClickListener(this);
        radio_foodie = (RadioButton) findViewById(R.id.radiofoodie);
        radio_bawarchi = (RadioButton) findViewById(R.id.radiobawarchi);
        btn_alreadylogin = (TextView) findViewById(R.id.btn_alreadylogin);
        btn_alreadylogin.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_username=(EditText) findViewById(R.id.input_username);
        et_password=(EditText) findViewById(R.id.input_password);
        chk_rememberme=(CheckBox) findViewById(R.id.chk_remeberme);


        isAlreadyLogin = true;
        username = "Bawarchi";
        if (isAlreadyLogin) {
            btn_alreadylogin.setText(btn_alreadylogin.getText().toString() + username);
        }

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user=shared.getString("username","");
        String pass=shared.getString("password","");
        String role=shared.getString("usertype","");

        if(!user.equals("") && !pass.equals("") && !role.equals("")){
            dialog.setMessage("Please Wait");
            dialog.show();
            et_username.setText(user);
            et_password.setText(pass);

            if (role.equals("bawarchi")){
                radio_bawarchi.setChecked(true);
                radio_foodie.setChecked(false);
            }
            else{
                radio_bawarchi.setChecked(false);
                radio_foodie.setChecked(true);
            }


            try {
                loginpost(et_username.getText().toString(),et_password.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Exit From Application")
                .setMessage("Are you sure you want to exit from the Application?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.link_signup:

                Intent ii = new Intent(getApplicationContext(), SignUp.class);
                startActivity(ii);

                break;

            case R.id.btn_alreadylogin:


                break;

            case R.id.btn_login:

                dialog.setMessage("Please Wait");
                dialog.show();

                try {
                    loginpost(et_username.getText().toString(),et_password.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }


    }

    AlertDialog dialog;



    void loginpost(String username,String password) throws Exception{

         String postUrl= StaticVariables.API_LINK_DEFAULT+"User_Login";

       //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json=new JSONObject();
        json.put("UserEmail",username);
        json.put("Password",password);

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
                dialog.dismiss();
                Log.i("loginrspns","failed to get response");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Server not Responding")
                                .setContentText("Please try later")
                                .show();
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                dialog.dismiss();

                if(response.isSuccessful()){
                    try {

                        String responseString=convertStandardJSONString(response.body().string());

                        Log.i("loginrspns",responseString);


                       /* if(radio_foodie.isChecked()  ){
                            Intent i = new Intent(getApplicationContext(), FoodieDashboard.class);
                            startActivity(i);
                        }
                        else{
                            Intent i = new Intent(getApplicationContext(), BawarchiDashboard.class);
                            startActivity(i);
                        }
*/


                        JSONObject recievedjson=new JSONObject(responseString);

                        JSONObject datajson=new JSONObject(convertStandardJSONString(recievedjson.getString("d")));

                        if (datajson.get("RetVal").equals("1")){




                            if( datajson.get("RoleName").equals("bawarchi") ){

                                if(chk_rememberme.isChecked()){
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("username", et_username.getText().toString());
                                    editor.putString("password", et_password.getText().toString());
                                    editor.putString("User_ID",  datajson.get("User_ID").toString());
                                    editor.putString("usertype","bawarchi");
                                    editor.apply();
                                }


                                Intent i = new Intent(getApplicationContext(), BawarchiDashboard.class);
                                i.putExtra("username", et_username.getText().toString());
                                i.putExtra("User_ID",  datajson.get("User_ID").toString());
                                i.putExtra("pass",  et_password.getText().toString());
                                startActivity(i);
                            }
                            else if( datajson.get("RoleName").equals("foodie") ){

                                if(chk_rememberme.isChecked()){
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("username", et_username.getText().toString());
                                    editor.putString("password", et_password.getText().toString());
                                    editor.putString("usertype","foodie");
                                    editor.putString("User_ID",  datajson.get("User_ID").toString());
                                    editor.apply();
                                }


                                Intent i = new Intent(getApplicationContext(), FoodieDashboard.class);
                                i.putExtra("username", et_username.getText().toString());
                                i.putExtra("User_ID",  datajson.get("User_ID").toString());
                                i.putExtra("pass",  et_password.getText().toString());

                                startActivity(i);
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Log In Failed")
                                                .setContentText("Wrong username or password")
                                                .setConfirmText("OK")
                                                .show();
                                    }
                                });
                            }

                        }
                        else {
                            Log.i("loginrspns","user not found");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Log In Failed")
                                            .setContentText("Wrong username or password")
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Log In Failed")
                                        .setContentText("Wrong username or password")
                                        .setConfirmText("OK")
                                        .show();
                        }
                    });

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
