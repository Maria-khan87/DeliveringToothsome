package com.example.foodapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.ProfileModel;
import com.example.foodapp.R;
import com.example.foodapp.Utils.ImageUtil;
import com.example.foodapp.dialogs.DialogFoodieUpdateProfile;
import com.example.foodapp.dialogs.DialogUpdateProfile;

import org.json.JSONException;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodieProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodieProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodieProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    FoodieDashboard activity;

    @SuppressLint("ValidFragment")
    public FoodieProfile(FoodieDashboard activity) {
        this.activity = activity;
    }

    public FoodieProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodieProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodieProfile newInstance(String param1, String param2) {
        FoodieProfile fragment = new FoodieProfile();
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

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_foodie_profile, container, false);
        initview(view);
        return view;
    }

    Button btn_updateprofile;
    ImageView img_bawarchi;
    TextView txt_city, txt_b_name, txt_address, txt_cno, txt_email, txt_password;
    OkHttpClient okHttpClient;
    public ProfileModel profileModel;

    private void initview(View view) {

        okHttpClient = new OkHttpClient();
        profileModel=new ProfileModel();

        img_bawarchi = (ImageView) view.findViewById(R.id.img_foodie);
        txt_city = (TextView) view.findViewById(R.id.txt_city);
        txt_b_name = (TextView) view.findViewById(R.id.txt_b_name);
        txt_address = (TextView) view.findViewById(R.id.txt_address);
        txt_cno = (TextView) view.findViewById(R.id.txt_cno);
        txt_email = (TextView) view.findViewById(R.id.txt_email);
        txt_password = (TextView) view.findViewById(R.id.txt_password);

//get data and display


       getUserProfileData();

    }


    public void getUserProfileData(){

        activity.dialog.setTitle("Loading");
        activity.dialog.show();

        try {
            getUserData(activity.UserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void getUserData(String username) throws Exception{

        String postUrl= StaticVariables.API_LINK_DEFAULT+"User_GetUserbyUserEmail";
        //  String postBody="{\"UserEmail\":\""+username+"\",\"Password\":\""+password+"\"}";

        JSONObject json=new JSONObject();
        json.put("UserEmail",username);

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
                Log.i("loginrspns","failed to get response");


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


                if(response.isSuccessful()){
                    try {

                        String responseString=response.body().string();

                        Log.i("loginrspns",responseString);



                        JSONObject recievedjson=new JSONObject(responseString);

                        JSONObject datajson=new JSONObject(recievedjson.getString("d"));

                        if (datajson.get("RetVal").equals("1")){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                       /* new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("User Found")
                                                .setContentText("Sucessfuly found "+datajson.get("UserEmail")+" with role name "+datajson.get("RoleName"))
                                                .show();*/


                                        profileModel.setUserEmail(datajson.get("UserEmail").toString());
                                        profileModel.setCity_Id(datajson.get("City_ID").toString());
                                        profileModel.setFullAddress(datajson.get("FullAddress").toString());
                                        profileModel.setPersonalContactNumber(datajson.get("PersonalContactNumber").toString());
                                        profileModel.setFullName(datajson.get("FullName").toString());
                                        profileModel.setPassword(activity.Password);
                                        profileModel.setProfilePic(datajson.get("ProfilePic").toString());

                                        activity.profileModel.setUserEmail(datajson.get("UserEmail").toString());
                                        activity.profileModel.setCity_Id(datajson.get("CityName").toString());
                                        activity.profileModel.setFullAddress(datajson.get("FullAddress").toString());
                                        activity.profileModel.setPersonalContactNumber(datajson.get("PersonalContactNumber").toString());
                                        activity.profileModel.setFullName(datajson.get("FullName").toString());
                                        activity.profileModel.setPassword(activity.Password);
                                        activity.profileModel.setProfilePic(datajson.get("ProfilePic").toString());


                                        String[] cities=activity.getResources().getStringArray(R.array.cities);


                                        txt_b_name.setText(profileModel.getFullName());
                                        txt_city.setText(cities[Integer.parseInt(profileModel.getCity_Id())]);
                                        txt_address.setText(profileModel.getFullAddress());
                                        txt_cno.setText(profileModel.getPersonalContactNumber());
                                        txt_email.setText(profileModel.getUserEmail());
                                        txt_password.setText(profileModel.getPassword());
                                        img_bawarchi.setImageBitmap(ImageUtil.convert(profileModel.getProfilePic()));



                                        btn_updateprofile = (Button) view.findViewById(R.id.btn_f_update);
                                        btn_updateprofile.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                DialogFoodieUpdateProfile dlg = new DialogFoodieUpdateProfile(activity,profileModel,FoodieProfile.this);
                                                dlg.show();
                                            }
                                        });

                                    } catch (JSONException e) {
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
                                            .setTitleText("Log In Failed")
                                            .setContentText("Wrong username or password")
                                            .setConfirmText("OK")
                                            .show();
                                }
                            });
                        }
                        activity.dialog.dismiss();

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
