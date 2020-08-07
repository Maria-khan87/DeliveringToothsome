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
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.HistoryModel;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.FoodieHistoryAdapter;
import com.example.foodapp.adapters.FoodieMenuSelectionAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
 * {@link FoodieHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodieHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodieHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public Spinner spinner_months;

    public FoodieHistory() {
        // Required empty public constructor
    }

    FoodieDashboard activity;
    @SuppressLint("ValidFragment")
    public FoodieHistory(FoodieDashboard activity) {
this.activity=activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodieHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodieHistory newInstance(String param1, String param2) {
        FoodieHistory fragment = new FoodieHistory();
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
        View view= inflater.inflate(R.layout.fragment_foodie_history, container, false);

        initView(view);

        return view;
    }


    RecyclerView rv_history;

    private void initView(View view) {
        rv_history=(RecyclerView) view.findViewById(R.id.rv_foddie_history);
        rv_history.setLayoutManager(new GridLayoutManager(activity,1));
        spinner_months=(Spinner) view.findViewById(R.id.spinner_months);

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);


        spinner_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    getMenuItemData(activity.UserName,i+1+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_months.setSelection(month,true);



    }


    public void refreshHistory(int month){
        try {
            getMenuItemData(activity.UserName,month+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    void getMenuItemData(String username,String Month) throws Exception {

        activity.dialog.setTitle("Loading");
        activity.dialog.show();
        String postUrl = StaticVariables.API_LINK_DEFAULT+"Orders_GetMonthlyOrdersbyFoodie";
        //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json = new JSONObject();
        json.put("UserEmail", username);
        json.put("Month", Month);

        String postBody = json.toString();

        Log.d("historyjson",postUrl+"  "+postBody);

        try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    List<HistoryModel> list_Model=new ArrayList<HistoryModel>();

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


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.dialog.dismiss();
                        activity.dialog.dismiss();
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

                if (response.isSuccessful()) {
                    try {

                        list_Model.clear();

                        String responseString = convertStandardJSONString(response.body().string());

                        Log.i("menurspns", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);

                        JSONArray jsonarray = new JSONArray(recievedjson.getString("d"));

                        if (jsonarray.length() > 0) {
                            try {

                               // txt_noMenu.setVisibility(View.GONE);

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject objectjson = (JSONObject) jsonarray.get(i);

                                    Log.i("menurspns", objectjson.toString());

                                    HistoryModel model = new HistoryModel();
                                    model.setOrderNumber(objectjson.getString("Order_ID"));
                                    model.setOrderDate(objectjson.getString("OrderDate").replace("T"," "));
                                    model.setShippingAddress(objectjson.getString("ShippingAddress"));
                                    model.setScheduleDate(objectjson.getString("ScheduleDate").replace("T"," "));
                                    model.setScheduleSlot(objectjson.getString("ScheduleSlot"));
                                    model.setFoodie_Id(objectjson.getString("Foodie_Id"));
                                    model.setFoodie_UserEmail(objectjson.getString("Foodie_UserEmail"));
                                    model.setBawarchi_Id(objectjson.getString("Bawarchi_Id"));
                                    model.setBawarchi_UserEmail(objectjson.getString("Bawarchi_UserEmail"));
                                    model.setStatus_From_Bawarchi(objectjson.getString("Status_From_Bawarchi"));
                                    model.setStaus_From_Foodie(objectjson.getString("Status_From_Foodie"));
                                    model.setOrderDesc(objectjson.getString("OrderDesc"));
                                    model.setOrderedItemsDetail(objectjson.getString("OrderedItemsDetail"));
                                    model.setGrantTotal(objectjson.getString("GrandTotal"));
                                    model.setRating((float) Double.parseDouble(objectjson.getString("Rating")));
                                    if(objectjson.getString("RatingRemarks").equals("")){
                                        model.setRemarks("No Remarks");

                                    }
                                    else{
                                        model.setRemarks(objectjson.getString("RatingRemarks"));

                                    }
                                   // model.setItem_ID((String) " " + objectjson.get("Item_ID"));//integer


                                    list_Model.add(model);


                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        rv_history.setAdapter(new FoodieHistoryAdapter(activity,FoodieHistory.this,list_Model));
                                        activity.dialog.dismiss();

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Log.i("loginrspns","no history");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //txt_noMenu.setVisibility(View.VISIBLE);
                                    rv_history.setAdapter(null);
                                    activity.dialog.dismiss();
                                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("No History")
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

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.dialog.dismiss();
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("No Response")
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
