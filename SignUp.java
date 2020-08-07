package com.example.foodapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.Models.EventModel;
import com.example.foodapp.Utils.ImageUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {


    TextView input_bawarchiname, input_contactno, input_address, input_email, input_password, input_conformpassword;
    Button btn_signup;
    RadioButton radiofoodie, radiobawarchi;
    Spinner spinner;
    OkHttpClient okHttpClient;
    ImageUtil imageUtil;
    String role = "";

    ImageView profileimg,btn_addimg;

    int CityId = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        okHttpClient = new OkHttpClient();
        dialog = new SpotsDialog.Builder()
                .setContext(SignUp.this)
                .build();

        imageUtil = new ImageUtil();

        input_bawarchiname = (TextView) findViewById(R.id.input_bawarchiname);
        input_contactno = (TextView) findViewById(R.id.input_contactno);
        input_address = (TextView) findViewById(R.id.input_address);
        input_email = (TextView) findViewById(R.id.input_email);
        input_password = (TextView) findViewById(R.id.input_password);
        input_conformpassword = (TextView) findViewById(R.id.input_conformpassword);
        profileimg = (ImageView) findViewById(R.id.profileimg);
        btn_addimg= (ImageView) findViewById(R.id.btn_addimg);







        btn_signup = (Button) findViewById(R.id.btn_signup);

        radiofoodie = (RadioButton) findViewById(R.id.radiofoodie);
        radiobawarchi = (RadioButton) findViewById(R.id.radiobawarchi);

        spinner = (Spinner) findViewById(R.id.spinner_city);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CityId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (radiofoodie.isChecked()) {
            role = "3";
        } else {
            role = "2";
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                if (radiofoodie.isChecked()) {
                    role = "3";
                } else {
                    role = "2";
                }

                try {


                    //setting checks here

                    String message="";
                    if(input_bawarchiname.getText().length()==0){
                        message=message+"Add Your Name in the form\n";
                    }
                    if(input_contactno.getText().length()==0){
                        message=message+"Enter Valid Contact Number\n";
                    }
                    if(spinner.getSelectedItemPosition()==0){
                        message=message+"Select a City\n";
                    }
                    if(input_address.getText().length()==0){
                        message=message+"Enter Valid Address\n";
                    }
                    if(input_email.getText().length()==0 || !isValidEmail(input_email.getText())){
                        message=message+"Enter Valid Email Address\n";
                    }
                    if(input_password.getText().length()==0){
                        message=message+"Enter a password\n";
                    }
                    if(input_password.getText().length()<6){
                        message=message+"Password should have minimum 6 characters\n";
                    }

                    if(!input_password.getText().toString().equals(input_conformpassword.getText().toString())) {
                        message=message+"Passwords does not match\n";
                    }

                    if(message.length()==0){

                        profileimg.setDrawingCacheEnabled(true);
                        dialog.setMessage("Please Wait");
                        dialog.show();
                        signup(
                                input_email.getText().toString(),
                                input_password.getText().toString(),
                                role,
                                input_bawarchiname.getText().toString(),
                                input_address.getText().toString(),
                                CityId + "",
                                input_contactno.getText().toString(),
                                input_contactno.getText().toString(),
                                BitMapToString(profileimg.getDrawingCache()) );
                    }
                    else{

                        new AlertDialog.Builder(SignUp.this)
                                .setTitle("Please Complete the form")
                                .setMessage(message)

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();




                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btn_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setActivityTitle("Crop Image")
                        .start(SignUp.this);
            }
        });



        input_bawarchiname.setText("");

        input_contactno.setText("");
        spinner.setSelection(0);
        input_address.setText("");
        input_email.setText("");

        input_password.setText("");


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                profileimg.setImageBitmap(BitmapFactory.decodeFile(resultUri.getPath()));


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventModel event) {

        if (event.getCode().equals(EventModel.SIGN_UP)){
            profileimg.setImageBitmap(event.getBitmap());
        }

    };



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }*/


    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    void signup(String UserEmail, String Password, String Role_Id, String FullName, String FullAddress, String City_Id, String PersonalContactNumber, String OfficalContactNumber, String ProfilePic) throws Exception {
        String postUrl = StaticVariables.API_LINK_DEFAULT+ "User_InsertUser";

        JSONObject json = new JSONObject();


        json.put("UserEmail", UserEmail);
        json.put("Password", Password);
        json.put("Role_Id", Role_Id);
        json.put("FullName", FullName);
        json.put("FullAddress", FullAddress);
        json.put("City_Id", City_Id);
        json.put("PersonalContactNumber", PersonalContactNumber);
        json.put("OfficalContactNumber", OfficalContactNumber);
        json.put("ProfilePic", ProfilePic);

        String postBody = json.toString();

        Log.d("signupjson", postBody);


        try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public  boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    AlertDialog dialog;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    void postRequest(String postUrl, String postBody) throws IOException {

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
                Log.i("loginrspns", "failed to get response: ");

                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Server not Responding")
                                .setContentText("Please try later")
                                .show();
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                dialog.dismiss();

                if (response.isSuccessful()) {
                    try {

                        String responseString = convertStandardJSONString(response.body().string());

                        Log.i("loginrspns", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);

                        JSONObject datajson = new JSONObject(convertStandardJSONString(recievedjson.getString("d")));

                        if (datajson.get("RetVal").equals("1")) {


                           /* SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("username", input_email.getText().toString());
                            editor.putString("password", input_password.getText().toString());
                            editor.putString("usertype", role);
                            editor.apply();
*/

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SweetAlertDialog(SignUp.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Sign Up Sucessfull")
                                            .setContentText("Your new account has been created sucessfully")
                                            .setConfirmText("Sign In Now")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                            });


                        } else {
                            Log.i("loginrspns", "user not found");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sign Up Failed")
                                            .setContentText("User Already Exists")
                                            .setConfirmText("Try again")
                                            .show();
                                }
                            });
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("loginrspns", "error getting response");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sign Up Failed")
                                    .setContentText("There went something wrong. Please try later.")
                                    .setConfirmText("Try Again")
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
