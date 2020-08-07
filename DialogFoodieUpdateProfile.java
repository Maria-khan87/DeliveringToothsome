package com.example.foodapp.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.MainActivity;
import com.example.foodapp.Models.EventModel;
import com.example.foodapp.Models.ProfileModel;
import com.example.foodapp.R;
import com.example.foodapp.SignUp;
import com.example.foodapp.Utils.ImageUtil;
import com.example.foodapp.fragments.FoodieProfile;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.foodapp.SignUp.getBitmapFromView;

public class DialogFoodieUpdateProfile extends Dialog {

    FoodieDashboard activity;
    ProfileModel model;
    FoodieProfile foodieProfileFragment;
    public DialogFoodieUpdateProfile(FoodieDashboard activity, ProfileModel model, FoodieProfile foodieProfileFragment) {
        super(activity);
        this.activity=activity;
        this.model=model;
        this.foodieProfileFragment=foodieProfileFragment;
    }



    Button update;

    TextView input_bawarchiname, input_contactno, input_address, input_email, input_password, input_conformpassword;
    Button btn_signup;
    Spinner spinner;
    OkHttpClient okHttpClient;
    ImageUtil imageUtil;
    String role="";

    ImageView profileimg,btn_addimg;
    int cityId=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_profile);

        okHttpClient = new OkHttpClient();
        imageUtil = new ImageUtil();

        input_bawarchiname = (TextView) findViewById(R.id.input_bawarchiname);
        input_contactno = (TextView) findViewById(R.id.input_contactno);
        input_address = (TextView) findViewById(R.id.input_address);
        //input_email = (TextView) findViewById(R.id.input_email);
        input_password = (TextView) findViewById(R.id.input_password);
        input_conformpassword = (TextView) findViewById(R.id.input_conformpassword);
        profileimg = (ImageView) findViewById(R.id.profileimg);

        btn_signup = (Button) findViewById(R.id.btn_signup);


        spinner=(Spinner) findViewById(R.id.spnr_city);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i!=0){
                    cityId=i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        input_bawarchiname.setText(model.getFullName());
        input_contactno.setText(model.getPersonalContactNumber());
        input_address.setText(model.getFullAddress());
        //input_email.setText(model.getUserEmail());
        input_password.setText(model.getPassword());

        spinner.setSelection(Integer.parseInt(model.getCity_Id()),true);

        profileimg.setImageBitmap(ImageUtil.convert(model.getProfilePic()));
        role = "3"; //fodiee


        btn_addimg= (ImageView) findViewById(R.id.btn_addimg);
        btn_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.currentStatus= EventModel.FOODIE_PROFILE;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setActivityTitle("Crop Image")
                        .start(activity);
            }
        });

        update=(Button) findViewById(R.id.btn_updateprofile);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(input_password.getText().toString().equals(input_conformpassword.getText().toString())) {

                        activity.dialog.setMessage("Updating");
                        activity.dialog.show();

                        update(


                                activity.UserName,
                                input_password.getText().toString(),
                                role,
                                input_bawarchiname.getText().toString(),
                                input_address.getText().toString(),
                                cityId + "",
                                input_contactno.getText().toString(),
                                input_contactno.getText().toString(),
                                ImageUtil.convert(getBitmapFromView(profileimg))

                        );

                    }
                    else{
                        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Passwords does not match")
                                .setContentText("Please review passwords in both fields")
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String code=EventModel.FOODIE_PROFILE;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventModel event) {
        if (code.equals(event.getCode())){
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
    }

    void update(String UserEmail, String Password, String Role_Id, String FullName, String FullAddress, String City_Id, String PersonalContactNumber, String OfficalContactNumber, String ProfilePic) throws Exception {
        String postUrl = StaticVariables.API_LINK_DEFAULT+"User_UpdateUser";

        JSONObject json = new JSONObject();


        json.put("User_ID", activity.UserID);

        json.put("UserEmail", activity.UserName);
        json.put("Password", Password);
        json.put("Role_Id", Role_Id);
        json.put("FullName", FullName);
        json.put("FullAddress", FullAddress);
        json.put("City_Id", City_Id);
        json.put("PersonalContactNumber", PersonalContactNumber);
        json.put("OfficalContactNumber", OfficalContactNumber);
        json.put("Deleted", "0");
        json.put("ProfilePic", ProfilePic);

        String postBody = json.toString();
        try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


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

                Log.i("updaterspns", "failed to get response");

                e.printStackTrace();
                activity.dialog.dismiss();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                activity.dialog.dismiss();

                if (response.isSuccessful()) {
                    try {

                        String responseString = convertStandardJSONString(response.body().string());

                        Log.i("updaterspns", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);

                        JSONObject datajson = new JSONObject(convertStandardJSONString(recievedjson.getString("d")));

                        if (datajson.get("RetVal").equals("1")) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Updated Successfully")
                                            .show();
                                }
                            });



                            SharedPreferences.Editor editor = getContext().getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                            //editor.putString("username", input_email.getText().toString());
                            editor.putString("password", input_password.getText().toString());
                            //editor.putString("usertype", role);
                            editor.apply();

                            foodieProfileFragment.getUserProfileData();

                            dismiss();

                        } else {
                            Log.i("updaterspns", "user not found");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("updaterspns", "error getting response");



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