package com.example.foodapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.carteasy.v1.lib.Carteasy;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.Models.EventModel;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.Models.ProfileModel;
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.adapters.BmenuAdapter;
import com.example.foodapp.adapters.FoodieScheduleAdapter;
import com.example.foodapp.dialogs.DialogFoodieUpdateProfile;
import com.example.foodapp.fragments.FoodieHistory;
import com.example.foodapp.fragments.FoodieProfile;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.foodapp.MainActivity.MY_PREFS_NAME;

public class FoodieDashboard extends AppCompatActivity {


    RecyclerView rv_f_schedule;
    DrawerLayout drawerLayout;
    ImageView btn_nav;
    FoodieProfile foodieProfile;
    FoodieHistory foodieHistory;
    public FrameLayout frame_main_foodie;
    public TextView main_title;

    public AlertDialog dialog;

    public String BawarchiId = "";

    public String SelectedDate = "";
    public String SelectedSlot = "";
    public String SelectedTime = "";

    public List<CartModel> cart;

    public String UserName="",UserID="",Password="";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putString("usertype","");
            editor.apply();

            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_dashboard);

        cart = new ArrayList<CartModel>();
        dialog = new SpotsDialog.Builder()
                .setContext(FoodieDashboard.this)
                .build();

        profileModel = new ProfileModel();

        UserName=getIntent().getStringExtra("username");
        UserID=getIntent().getStringExtra("User_ID");
        Password=getIntent().getStringExtra("pass");

        frame_main_foodie = (FrameLayout) findViewById(R.id._frame_main_foodie);
        main_title = (TextView) findViewById(R.id.foodie_home_title);

        rv_f_schedule = (RecyclerView) findViewById(R.id.rv_f_schedule);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_f_schedule.setLayoutManager(lm);



        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        // menuItem.setChecked(true);
                        // close drawer when item is tapped

                        switch (menuItem.getItemId()) {

                            case R.id.nav_profile:
                                main_title.setText("My Profile");
                                frame_main_foodie.setVisibility(View.VISIBLE);
                                foodieProfile = new FoodieProfile(FoodieDashboard.this);
                                getSupportFragmentManager().beginTransaction().add(
                                        R.id._frame_main_foodie, foodieProfile, "Fprofile"
                                ).addToBackStack(null).commit();
                                break;

                            case R.id.nav_schedule:
                                main_title.setText("My Schedule");
                                Fragment fprofile = getSupportFragmentManager().findFragmentByTag("Fprofile");
                                if (fprofile != null) {
                                    frame_main_foodie.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().remove(fprofile).commit();
                                }


                                Fragment bawarchis = getSupportFragmentManager().findFragmentByTag("bawarchis");
                                if (bawarchis != null) {
                                    frame_main_foodie.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().remove(bawarchis).commit();
                                }


                                Fragment hstry = getSupportFragmentManager().findFragmentByTag("Fhistory");
                                if (hstry != null) {
                                    frame_main_foodie.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().remove(hstry).commit();
                                }

                                break;

                            case R.id.nav_history:

                                main_title.setText("My History");
                                frame_main_foodie.setVisibility(View.VISIBLE);
                                foodieHistory = new FoodieHistory(FoodieDashboard.this);
                                getSupportFragmentManager().beginTransaction().add(
                                        R.id._frame_main_foodie, foodieHistory, "Fhistory"
                                ).addToBackStack(null).commit();


                                break;


                        }


                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        btn_nav = (ImageView) findViewById(R.id.btn_nav);
        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });




       getScheduledData();


    }



    public void getScheduledData(){
        dialog.setMessage("Loading");
        dialog.show();
        try {
            getSchedule(UserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int totalItemsCount = 0;
    public ProfileModel profileModel;
    public boolean gettingSchedule=false;


    void getUserData(String username) throws Exception {

        String postUrl = StaticVariables.API_LINK_DEFAULT+"User_GetUserbyUserEmail";
        //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json = new JSONObject();
        json.put("UserEmail", username);

        String postBody = json.toString();
        try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void getSchedule(String username) throws Exception {

        gettingSchedule=true;

        String postUrl = StaticVariables.API_LINK_DEFAULT+"Orders_Get7DaysOrdersbyFoodie";
        //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json = new JSONObject();
        json.put("UserEmail", username);

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
                Log.i("loginrspns", "failed to get response");
                e.printStackTrace();

                gettingSchedule=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()) {
                    try {

                        String responseString = convertStandardJSONString(response.body().string());

                        Log.i("rspns", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);


                        if(gettingSchedule){
                            gettingSchedule=false;


                            JSONArray jsonarray = new JSONArray(recievedjson.getString("d"));


                                setScheduleData(jsonarray);

                            getProfileData();
                        }
                        else{

                            JSONObject datajson = new JSONObject(convertStandardJSONString(recievedjson.getString("d")));

                            if (datajson.get("RetVal").equals("1")) {

                                gettingSchedule=false;
                                profileModel.setUserEmail(datajson.get("UserEmail").toString());
                                profileModel.setCity_Id(datajson.get("CityName").toString());
                                profileModel.setFullAddress(datajson.get("FullAddress").toString());
                                profileModel.setPersonalContactNumber(datajson.get("PersonalContactNumber").toString());
                                profileModel.setFullName(datajson.get("FullName").toString());
                                profileModel.setPassword("pass");

                                dialog.dismiss();

                            } else {

                                Log.i("loginrspns", "user not found");
                            }



                        }

                    } catch (Exception e) {
                        gettingSchedule=false;
                        e.printStackTrace();
                    }
                } else {
                    Log.i("loginrspns", "error getting response");
                    gettingSchedule=false;

                }
            }
        });
    }


    List<ScheduleItemModel> list_schedule_model=new ArrayList<ScheduleItemModel>();
    List<LinkedHashMap<String,ScheduleItemModel>> finalList=new ArrayList<LinkedHashMap<String,ScheduleItemModel>>();

    public void setScheduleData(JSONArray jsonArray){

        list_schedule_model.clear();


            try {


                //get all sheduled models in a list

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    ScheduleItemModel model=new ScheduleItemModel();

                    model.setOrder_ID(jsonObject.getString("Order_ID"));
                    model.setOrderDate(jsonObject.getString("OrderDate"));
                    model.setShippingAddress(jsonObject.getString("ShippingAddress"));
                    model.setScheduleDate(jsonObject.getString("ScheduleDate"));
                    model.setScheduleSlot(jsonObject.getString("ScheduleSlot"));
                    model.setFoodie_Id(jsonObject.getString("Foodie_Id"));
                    model.setFoodie_UserEmail(jsonObject.getString("Foodie_UserEmail"));
                    model.setBawarchi_Id(jsonObject.getString("Bawarchi_Id"));
                    model.setBawarchi_UserEmail(jsonObject.getString("Bawarchi_UserEmail"));
                    model.setStatus_From_Bawarchi(jsonObject.getString("Status_From_Bawarchi"));
                    model.setStaus_From_Foodie(jsonObject.getString("Status_From_Foodie"));
                    model.setOrderDesc(jsonObject.getString("OrderDesc"));
                    model.setOrderedItemsDetail(jsonObject.getString("OrderedItemsDetail"));
                    model.setGrantTotal(jsonObject.getString("GrandTotal"));

                    if(!model.getStaus_From_Foodie().equals("3") &&  !model.getStatus_From_Bawarchi().equals("3")){
                        list_schedule_model.add(model);
                    }



                }

                //get dates

                Date date= Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dt = df.format(date);

                String[] dateArray=new String[7];

                for(int i=0;i<7;i++){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(dt));
                    c.add(Calendar.DATE, i);  // number of days to add
                    dateArray[i] = sdf.format(c.getTime());  // dt is now the new date
                    Log.d("date",dateArray[i]);
                }


                //set meal types of each date


                finalList.clear();

                for(int j=0;j<7;j++){

                    String countingDate=dateArray[j];
                    LinkedHashMap<String,ScheduleItemModel> slot_map=new LinkedHashMap<String, ScheduleItemModel>();
                    slot_map.put("BreakFast",null);
                    slot_map.put("Lunch",null);
                    slot_map.put("Dinner",null);

                    for(int id=0;id<list_schedule_model.size();id++){

                        String itemdate=list_schedule_model.get(id).getScheduleDate().split("T")[0];

                        if(itemdate.equals(countingDate) && list_schedule_model.get(id).getScheduleSlot().equals("BreakFast")){
                                slot_map.put("BreakFast",list_schedule_model.get(id));
                        }

                        if(itemdate.equals(countingDate) && list_schedule_model.get(id).getScheduleSlot().equals("Lunch")){
                            slot_map.put("Lunch",list_schedule_model.get(id));
                        }

                        if(itemdate.equals(countingDate) && list_schedule_model.get(id).getScheduleSlot().equals("Dinner")){
                            slot_map.put("Dinner",list_schedule_model.get(id));
                        }

                    }


                    finalList.add(slot_map);

                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv_f_schedule.setAdapter(new FoodieScheduleAdapter(FoodieDashboard.this,finalList,dateArray));

                    }
                });




            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    public String formatDateTime(String datetime){


        String displayValue="";
        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;

            date = dateFormatter.parse(datetime);


// Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
            displayValue = timeFormatter.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return displayValue;

    }


    public void getProfileData() {
        try {
            getUserData(UserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
//        data_json = data_json.replace("\"{", "{");
        //       data_json = data_json.replace("}\",", "},");
        //      data_json = data_json.replace("}\"", "}");
        return data_json;
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            // frame_main_foodie.setVisibility(View.GONE);

        } else {


            new AlertDialog.Builder(this)
                    .setTitle("Exit From Foodie Dashboard")
                    .setMessage("Are you sure you want to exit from Foodie Dashboard?")

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

    }


    public String currentStatus;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                EventModel model=new EventModel();
                model.setCode(currentStatus);
                model.setBitmap(BitmapFactory.decodeFile(resultUri.getPath()));

                EventBus.getDefault().post(model);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}
