package com.example.foodapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.BawarchizModel;
import com.example.foodapp.Models.CartModel;
import com.example.foodapp.Models.MenuModel;
import com.example.foodapp.R;
import com.example.foodapp.Utils.ImageUtil;
import com.example.foodapp.adapters.BawarchiShowAdapter;
import com.example.foodapp.adapters.FoodieMenuSelectionAdapter;
import com.example.foodapp.dialogs.DialogCartView;

import org.json.JSONArray;
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
 * {@link CookShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CookShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookShowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CookShowFragment() {
        // Required empty public constructor
    }

    FoodieDashboard activity;
    public String bname,Fullname;
    public String CookId;
    Bitmap cookPic;
    @SuppressLint("ValidFragment")
    public CookShowFragment(FoodieDashboard activity, String bname, String Fullname, String CookId, Bitmap cookPic) {
        this.bname=bname;
        this.Fullname=Fullname;
        this.activity=activity;
        this.CookId=CookId;
        this.cookPic= cookPic;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CookShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CookShowFragment newInstance(String param1, String param2) {
        CookShowFragment fragment = new CookShowFragment();
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
        View view= inflater.inflate(layout.fragment_cook_show, container, false);

        initView(view);

        return view;

    }


    public void addItemtoCart(String ItemId,String itemname,Double quantity){
        activity.totalItemsCount++;

        CartModel model=new CartModel();
        model.setItemName(itemname);
        model.setPrice(quantity);
        model.setItemId(ItemId);

        activity.cart.add(model);
        btn_cart.setText("Cart ("+activity.cart.size()+")");
    }


    public void refreshCartBtn(){
        btn_cart.setText("Cart ("+activity.cart.size()+")");
    }


    RecyclerView rv_items;
    TextView btn_cart,txt_bawarchi_name,txt_noMenu;

    ImageView f_img_cook;

    private void initView(View view) {

        activity.cart.clear();

        rv_items=(RecyclerView) view.findViewById(R.id.rv_f_menu_items);
        rv_items.setLayoutManager(new GridLayoutManager(activity,3));

        txt_bawarchi_name=(TextView) view.findViewById(R.id.txt_bawarchi_name);
        txt_bawarchi_name.setText(Fullname);

        txt_noMenu=(TextView) view.findViewById(R.id.txt_noMenu);


        f_img_cook=(ImageView) view.findViewById(R.id.f_img_cook);

        if(cookPic!=null){
            f_img_cook.setImageBitmap(cookPic);
        }

        activity.dialog.setMessage("Loading");
        activity.dialog.show();
        try {
            getMenuItemData(bname);
        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_cart=(TextView) view.findViewById(R.id.crt);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activity.cart.size()>0){
                    DialogCartView dlg=new DialogCartView(activity,CookShowFragment.this);
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                }
                else{
                    Toast.makeText(activity,"No Items in Cart",Toast.LENGTH_SHORT).show();
                }




            }
        });


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
                Log.i("all bawahiz","failed to get response");


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

                        List<MenuModel> list_Model=new ArrayList<MenuModel>();

                        String responseString = convertStandardJSONString(response.body().string());

                        Log.i("all bawahiz", responseString);


                        JSONObject recievedjson = new JSONObject(responseString);

                        JSONArray jsonarray = new JSONArray(recievedjson.getString("d"));

                        if (jsonarray.length() > 0) {
                            try {

                                txt_noMenu.setVisibility(View.GONE);

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject objectjson = (JSONObject) jsonarray.get(i);

                                  //  Log.i("menurspns", objectjson.toString());

                                    MenuModel model = new MenuModel();

                                    model.setItem_ID((String) " " + objectjson.get("Item_ID"));//integer
                                    model.setItemName((String) objectjson.get("ItemName"));
                                    model.setItemDesc((String) objectjson.get("ItemDesc"));
                                    model.setItemPrice((String) "" + objectjson.get("ItemPrice"));//double
                                    model.setItemPic(ImageUtil.convert( objectjson.getString("ItemPic")));
                                    model.setBawarchi((String) objectjson.get("Bawarchi"));
                                    model.setDeleted(objectjson.getString("Deleted"));

                                    if(model.getDeleted().equals("0")){
                                        list_Model.add(model);
                                    }




                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        rv_items.setAdapter(new FoodieMenuSelectionAdapter(activity,CookShowFragment.this,list_Model));
                                        activity.dialog.dismiss();

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Log.i("all bawahiz","no bawarchi");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    txt_noMenu.setVisibility(View.VISIBLE);



                                    activity.dialog.dismiss();
                                    new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Menu not found")
                                            .setContentText("No menu is specified by Bawarchi")
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
                    Log.i("all bawahiz","error getting response");

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.dialog.dismiss();
                            new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Failed to load the menu")
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
