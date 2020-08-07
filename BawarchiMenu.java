package com.example.foodapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.R;
import com.example.foodapp.Utils.ImageUtil;
import com.example.foodapp.adapters.BmenuAdapter;
import com.example.foodapp.dialogs.DialogNewItem;

import org.json.JSONArray;
import org.json.JSONException;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BawarchiMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BawarchiMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BawarchiMenu extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BawarchiMenu() {
        // Required empty public constructor
    }

    BawarchiDashboard activity;

    @SuppressLint("ValidFragment")
    public BawarchiMenu(BawarchiDashboard activity) {
        this.activity = activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BawarchiMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static BawarchiMenu newInstance(String param1, String param2) {
        BawarchiMenu fragment = new BawarchiMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bawarchi_menu, container, false);
        initView(view);
        return view;
    }

    RecyclerView rv_items;
    ImageView btn_new_b_item;
    OkHttpClient okHttpClient;

    TextView txt_nomenu;

    private void initView(View view) {

        okHttpClient = new OkHttpClient();

        rv_items = (RecyclerView) view.findViewById(R.id.rv_b_menu);
        txt_nomenu=(TextView) view.findViewById(R.id.txt_nomenu);

        btn_new_b_item = (ImageView) view.findViewById(R.id.btn_new_b_item);
        btn_new_b_item.setOnClickListener(this);

       getBawarchiMenu();

    }


    public void getBawarchiMenu(){
        try {
            getMenuItemData(activity.UserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_new_b_item:

                MenuModel model = new MenuModel();
                model.setBawarchi(activity.UserName);

                DialogNewItem dialogNewItem = new DialogNewItem(activity, model, StaticVariables.DIALOG_TYPE_NEW,BawarchiMenu.this);
                dialogNewItem.show();
                break;

        }


    }




    void getMenuItemData(String username) throws Exception {

        String postUrl = StaticVariables.API_LINK_DEFAULT+"Menu_GetItemsbyBawarchi";
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


    List<MenuModel> list_menuModel = new ArrayList<MenuModel>();


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
              //  Log.i("menurspns", "failed to get response");


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


                if (response.isSuccessful()) {
                    try {

                        String responseString = convertStandardJSONString(response.body().string());

                       // Log.i("menurspns", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);

                        JSONArray jsonarray = new JSONArray(recievedjson.getString("d"));

                        if (jsonarray.length() > 0) {
                            try {

                                list_menuModel.clear();

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject objectjson = (JSONObject) jsonarray.get(i);

                                    Log.i("menurspns", objectjson.toString());

                                    MenuModel model = new MenuModel();

                                    model.setItem_ID((String) ""+objectjson.get("Item_ID"));//integer
                                    model.setItemName((String) objectjson.get("ItemName"));
                                    model.setItemDesc((String) objectjson.get("ItemDesc"));
                                    model.setItemPrice((String) "" + objectjson.get("ItemPrice"));//double
                                    model.setItemPic(ImageUtil.convert( objectjson.getString("ItemPic")));
                                    model.setBawarchi((String) objectjson.get("Bawarchi"));
                                    model.setDeleted((String) objectjson.get("Deleted").toString());

                                    list_menuModel.add(model);


                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rv_items.setLayoutManager(new GridLayoutManager(activity, 2));
                                        txt_nomenu.setVisibility(View.GONE);

                                        rv_items.setAdapter(new BmenuAdapter(activity, list_menuModel,BawarchiMenu.this));

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                         //   Log.i("menurspns", "no item found");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Failed")
                                            .setContentText("no item found")
                                            .setConfirmText("OK")
                                            .show();*/

                                    txt_nomenu.setVisibility(View.VISIBLE);

                                }
                            });
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                   // Log.i("menurspns", "error getting response");

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Response Failed")
                                    .setContentText("No response")
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
