package com.example.foodapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.BawarchizModel;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.Models.ProfileModel;
import com.example.foodapp.R;
import com.example.foodapp.Utils.ImageUtil;
import com.example.foodapp.adapters.BawarchiShowAdapter;
import com.example.foodapp.adapters.BmenuAdapter;
import com.example.foodapp.dialogs.DialogFoodieUpdateProfile;

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
 * {@link ShowBawarchizFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowBawarchizFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowBawarchizFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShowBawarchizFrag() {
        // Required empty public constructor
    }

    FoodieDashboard activity;
    @SuppressLint("ValidFragment")
    public ShowBawarchizFrag(FoodieDashboard activity) {

        this.activity=activity;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowBawarchizFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowBawarchizFrag newInstance(String param1, String param2) {
        ShowBawarchizFrag fragment = new ShowBawarchizFrag();
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
        View view= inflater.inflate(R.layout.fragment_show_bawarchiz, container, false);

        initView(view);

        return view;
    }

    OkHttpClient okHttpClient;


    RecyclerView recyclerView;
    private void initView(View view) {
        recyclerView=(RecyclerView) view.findViewById(R.id.rv_bawarchi_show);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,3));


        okHttpClient = new OkHttpClient();


        activity.dialog.setMessage("Please Wait");
        activity.dialog.show();
        try {
            getBawarchiz(activity.profileModel.getCity_Id());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    void getBawarchiz(String cityname) throws Exception{

        String postUrl= StaticVariables.API_LINK_DEFAULT+"User_GetBawarchisByCity";
        //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json=new JSONObject();
        json.put("City_Name",cityname);

        String postBody=json.toString();
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
                Log.i("menurspns","failed to get response");


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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



                if (response.isSuccessful()) {
                    try {

                        List<BawarchizModel> list_Model=new ArrayList<BawarchizModel>();

                        String responseString = convertStandardJSONString(response.body().string());

                        Log.i("menurspns", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);

                        JSONArray jsonarray = new JSONArray(recievedjson.getString("d"));

                        if (jsonarray.length() > 0) {
                            try {

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject objectjson = (JSONObject) jsonarray.get(i);

                                    //Log.i("menurspns", objectjson.toString());

                                    BawarchizModel model = new BawarchizModel();

                                    model.setName((String) objectjson.get("UserEmail"));
                                    model.setFullName((String) objectjson.get("FullName"));
                                    model.setBawarchiId(""+ objectjson.get("User_ID"));
                                    model.setRating((float) Double.parseDouble(objectjson.getString("Rating")));

                                    if(objectjson.getString("ProfilePic")!=null){
                                        model.setPic(ImageUtil.convert(objectjson.getString("ProfilePic")));
                                    }

                                    list_Model.add(model);


                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(new BawarchiShowAdapter(activity,list_Model));
                                        activity.dialog.dismiss();
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Log.i("menurspns","user not found");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.dialog.dismiss();
                                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Bawarchi not Found")
                                            .setContentText("No bawarchi if found in your city")
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
                    Log.i("menurspns","error getting response");

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.dialog.dismiss();
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Server Error")
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






    public List<String> getMenuList(){

        List<String> list=new ArrayList<String>();

        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");


        return list;
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
