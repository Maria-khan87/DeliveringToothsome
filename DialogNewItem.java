package com.example.foodapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.MainActivity;
import com.example.foodapp.Models.EventModel;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.R;
import com.example.foodapp.Utils.ImageUtil;
import com.example.foodapp.fragments.BawarchiMenu;
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

public class DialogNewItem extends Dialog {

    BawarchiDashboard activity;
    String Dlg_Type;
    MenuModel menuModel;
    BawarchiMenu bawarchiMenuFragment;

    public DialogNewItem(BawarchiDashboard activity, MenuModel menuModel, String Dlg_Type,BawarchiMenu bawarchiMenuFragment) {
        super(activity);
        this.activity = activity;
        this.Dlg_Type = Dlg_Type;
        this.menuModel = menuModel;
        this.bawarchiMenuFragment=bawarchiMenuFragment;
    }

    Button additem, btn_update,btn_deactivateitem,btn_activate;
    TextView b_nt_dialog_title, input_itemname_new, input_description, input_price;
    ImageView img_dlg_newitem,btn_addimg;

    LinearLayout update_layout;

    boolean isActivated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_b_newitem);

        b_nt_dialog_title = (TextView) findViewById(R.id.b_nt_dialog_title);
        btn_deactivateitem=(Button) findViewById(R.id.btn_deactivateitem);
        btn_activate=(Button) findViewById(R.id.btn_activate);

        input_itemname_new = (TextView) findViewById(R.id.input_itemname_new);
        input_description = (TextView) findViewById(R.id.input_description);
        input_price = (TextView) findViewById(R.id.input_price);
        img_dlg_newitem=(ImageView) findViewById(R.id.img_dlg_newitem);

        update_layout=(LinearLayout) findViewById(R.id.update_layout) ;


        btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    activity.dialog.setMessage("Please Wait");
                    activity.dialog.show();

                    isActivated=true;

                    Menu_UpdateItem(menuModel.getItem_ID(),
                            input_itemname_new.getText().toString(),
                            input_description.getText().toString(),
                            input_price.getText().toString(),
                            ImageUtil.convert(activity.getBitmapFromView(img_dlg_newitem)),
                            "0",   //1=hide,0=show to foodie
                            activity.UserName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });




        btn_deactivateitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    activity.dialog.setMessage("Please Wait");
                    activity.dialog.show();

                    isDeactivated=true;

                    Menu_UpdateItem(menuModel.getItem_ID(),
                            input_itemname_new.getText().toString(),
                            input_description.getText().toString(),
                            input_price.getText().toString(),
                            ImageUtil.convert(activity.getBitmapFromView(img_dlg_newitem)),
                            "1",   //1=hide,0=show to foodie
                            activity.UserName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        additem = (Button) findViewById(R.id.btn_additem);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    activity.dialog.setMessage("Please wait");
                    activity.dialog.show();

                    Menu_InsertItem(
                            input_itemname_new.getText().toString(),
                            input_description.getText().toString(),
                            input_price.getText().toString(),
                            ImageUtil.convert(activity.getBitmapFromView(img_dlg_newitem)),
                            menuModel.getBawarchi());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        btn_addimg= (ImageView) findViewById(R.id.btn_addimg);
        btn_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.currentStatus= EventModel.BAWARCHI_MENU;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setActivityTitle("Crop Image")
                        .start(activity);
            }
        });



        btn_update = (Button) findViewById(R.id.btn_updateitem);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    activity.dialog.setMessage("Please Wait");
                    activity.dialog.show();

                    Menu_UpdateItem(menuModel.getItem_ID(),
                            input_itemname_new.getText().toString(),
                            input_description.getText().toString(),
                            input_price.getText().toString(),
                            ImageUtil.convert(activity.getBitmapFromView(img_dlg_newitem)),
                            "0",   //1=hide,0=show to foodie
                            activity.UserName);
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });


        if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_NEW)) {
            additem.setVisibility(View.VISIBLE);
            update_layout.setVisibility(View.GONE);
            b_nt_dialog_title.setText("New Menu Item");
            btn_activate.setVisibility(View.GONE);
        } else {
            additem.setVisibility(View.GONE);

            if(menuModel.getDeleted().equals("1")){
                btn_activate.setVisibility(View.VISIBLE);
                update_layout.setVisibility(View.GONE);
            }
            else {
                btn_activate.setVisibility(View.GONE);
                update_layout.setVisibility(View.VISIBLE);
            }


            b_nt_dialog_title.setText("Update Menu Item");

            input_price.setText(menuModel.getItemPrice());
            input_description.setText(menuModel.getItemDesc());
            input_itemname_new.setText(menuModel.getItemName());
            img_dlg_newitem.setImageBitmap(menuModel.getItemPic());

        }


    }

    boolean isDeactivated=false;

    private String code=EventModel.BAWARCHI_MENU;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventModel event) {
        if (code.equals(event.getCode())){
            img_dlg_newitem.setImageBitmap(event.getBitmap());
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


    void Menu_InsertItem(String ItemName, String ItemDesc, String ItemPrice, String ItemPic, String UserEmail) throws Exception {
        String postUrl = StaticVariables.API_LINK_DEFAULT+"Menu_InsertItem";

        JSONObject json = new JSONObject();

        json.put("ItemName", ItemName);
        json.put("UserEmail", UserEmail);
        json.put("ItemPrice", ItemPrice);
        json.put("ItemDesc", ItemDesc);
        json.put("ItemPic", ItemPic);

        String postBody = json.toString();
        try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void Menu_UpdateItem(String Item_ID,String ItemName, String ItemDesc, String ItemPrice, String ItemPic,String Deleted, String UserEmail) throws Exception {
        String postUrl = StaticVariables.API_LINK_DEFAULT+"Menu_UpdateItem";

        JSONObject json = new JSONObject();

        json.put("Item_ID", Item_ID);
        json.put("ItemName", ItemName);
        json.put("ItemDesc", ItemDesc);
        json.put("ItemPrice", ItemPrice);
        json.put("ItemPic", ItemPic);
        json.put("Deleted", Deleted);
        json.put("UserEmail", UserEmail);
        String postBody = json.toString();

        Log.d("json",postBody);

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
                                    if (Dlg_Type.equals(StaticVariables.DIALOG_TYPE_NEW)) {

                                        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Item Added Successfully")
                                                .show();

                                    } else {


                                        if(isDeactivated){
                                            isDeactivated=false;
                                            new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Item Deactivated Successfully")
                                                    .show();

                                        }

                                        else if(isActivated){
                                            isActivated=false;
                                            new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Item Activated Successfully")
                                                    .show();

                                        }
                                        else{
                                            new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Item Updated Successfully")
                                                    .show();

                                        }

                                    }

                                    bawarchiMenuFragment.getBawarchiMenu();

                                }
                            });


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
