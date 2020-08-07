package com.example.foodapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodapp.BawarchiDashboard;
import com.example.foodapp.Constants.StaticVariables;
import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.Models.ScheduleItemModel;
import com.example.foodapp.R;
import com.example.foodapp.adapters.AcceptedOrdersAdapter;
import com.example.foodapp.adapters.ComplatedOrdersAdapter;
import com.example.foodapp.adapters.FoodieScheduleAdapter;
import com.example.foodapp.adapters.PendingOrdersAdapter;
import com.example.foodapp.adapters.CookingOrdersAdapters;
import com.example.foodapp.adapters.ShippedOrdersAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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
 * {@link BawarchiOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BawarchiOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BawarchiOrders extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BawarchiOrders() {
        // Required empty public constructor
    }

    BawarchiDashboard acticity;
    @SuppressLint("ValidFragment")
    public BawarchiOrders(BawarchiDashboard acticity) {
        this.acticity=acticity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BawarchiOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static BawarchiOrders newInstance(String param1, String param2) {
        BawarchiOrders fragment = new BawarchiOrders();
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
        View view= inflater.inflate(R.layout.fragment_bawarchi_orders, container, false);
        initView(view);
        return view;
    }

    RecyclerView rv_pending_orders, rv_cooking_orders,rv_completed_orders,rv_accepted_orders,rv_shipped_orders;
    Button btn_pending, btn_cooking,btn_completed,btn_shipped,btn_accepted;

    TextView txt_norec;

    private void initView(View view) {

        txt_norec=(TextView) view.findViewById(R.id.txt_norec);

        rv_pending_orders=(RecyclerView) view.findViewById(R.id.rv_prending_orders);
        LinearLayoutManager lm=new LinearLayoutManager(acticity);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_pending_orders.setLayoutManager(lm);


        rv_cooking_orders =(RecyclerView) view.findViewById(R.id.rv_cooking_orders);
        LinearLayoutManager lm1=new LinearLayoutManager(acticity);
        lm1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_cooking_orders.setLayoutManager(lm1);




        rv_shipped_orders=(RecyclerView) view.findViewById(R.id.rv_shipped_orders);
        LinearLayoutManager lm4=new LinearLayoutManager(acticity);
        lm4.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shipped_orders.setLayoutManager(lm4);


        rv_accepted_orders =(RecyclerView) view.findViewById(R.id.rv_accepted_orders);
        LinearLayoutManager lm5=new LinearLayoutManager(acticity);
        lm5.setOrientation(LinearLayoutManager.VERTICAL);
        rv_accepted_orders.setLayoutManager(lm5);



        rv_completed_orders=(RecyclerView) view.findViewById(R.id.rv_completed_orders);
        LinearLayoutManager lm2=new LinearLayoutManager(acticity);
        lm2.setOrientation(LinearLayoutManager.VERTICAL);
        rv_completed_orders.setLayoutManager(lm2);


        btn_pending=(Button) view.findViewById(R.id.btn_pending);
        btn_cooking =(Button) view.findViewById(R.id.btn_progress);
        btn_completed=(Button) view.findViewById(R.id.btn_completed);
        btn_shipped=(Button) view.findViewById(R.id.btn_shipped);
        btn_accepted=(Button) view.findViewById(R.id.btn_accepted);

        btn_pending.setOnClickListener(this);
        btn_cooking.setOnClickListener(this);
        btn_completed.setOnClickListener(this);
        btn_accepted.setOnClickListener(this);
        btn_shipped.setOnClickListener(this);


        getBawarchiSchedule();

    }


    public void getBawarchiSchedule(){
        try {
            getSchedule(acticity.UserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    void getSchedule(String username) throws Exception {

        acticity.dialog.setTitle("Please Wait");
        acticity.dialog.show();

        String postUrl = StaticVariables.API_LINK_DEFAULT+"Order_GetOrdersbyBawarchi";

        JSONObject json = new JSONObject();
        json.put("UserEmail", username);

        String postBody = json.toString();
        try {
            postRequest(postUrl, postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/*
1	             Approved
2        	     In Progress
3	             Cancelled
4	             In Shipping
5	             Complete
*/

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


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()) {
                    try {

                        String responseString = convertStandardJSONString( response.body().string());




                        JSONObject recievedjson = new JSONObject(responseString);


                            JSONArray jsonarray = new JSONArray(recievedjson.getString("d"));


                            setScheduleData(jsonarray);


                        acticity.dialog.dismiss();


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {
                    Log.i("loginrspns", "error getting response");


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

    List<ScheduleItemModel> list_schedule_model=new ArrayList<ScheduleItemModel>();

    List<ScheduleItemModel> list_pending=new ArrayList<ScheduleItemModel>();
    List<ScheduleItemModel> list_saccepted=new ArrayList<ScheduleItemModel>();
    List<ScheduleItemModel> list_cooking=new ArrayList<ScheduleItemModel>();
    List<ScheduleItemModel> list_shipped=new ArrayList<ScheduleItemModel>();
    List<ScheduleItemModel> list_completed=new ArrayList<ScheduleItemModel>();
    List<ScheduleItemModel> list_canceled=new ArrayList<ScheduleItemModel>();




    List<LinkedHashMap<String,ScheduleItemModel>> finalList=new ArrayList<LinkedHashMap<String,ScheduleItemModel>>();

    public void setScheduleData(JSONArray jsonArray){

        list_schedule_model.clear();

        list_pending.clear();
        list_saccepted.clear();
        list_canceled.clear();
        list_shipped.clear();
        list_completed.clear();
        list_cooking.clear();

        try {


            //get all sheduled models in a list

            for (int i = 0; i < jsonArray.length(); i++) {




                JSONObject jsonObject=jsonArray.getJSONObject(i);

                Log.i("rspns", jsonObject.toString());

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

                    if(model.getStatus_From_Bawarchi().equals("1")){
                        list_saccepted.add(model);
                    }
                    else if(model.getStatus_From_Bawarchi().equals("2")){
                        list_cooking.add(model);
                    }

                    else if(model.getStatus_From_Bawarchi().equals("4")){
                        list_shipped.add(model);
                    }
                    else if(model.getStatus_From_Bawarchi().equals("5")){


                            model.setRating((float) Double.parseDouble(jsonObject.getString("Rating")));

                            if(jsonObject.getString("RatingRemarks").equals("")){
                                model.setRemarks("No Remarks by Foodie");

                            }
                            else{
                                model.setRemarks(jsonObject.getString("RatingRemarks"));

                            }


                        list_completed.add(model);

                    }
                    else{
                        list_pending.add(model);
                    }


                    //list_schedule_model.add(model);
                }
                else{
                    list_canceled.add(model);
                }

            }

            acticity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rv_completed_orders.setAdapter(new ComplatedOrdersAdapter(acticity,BawarchiOrders.this,list_completed));
                    rv_accepted_orders.setAdapter(new AcceptedOrdersAdapter(acticity,BawarchiOrders.this,list_saccepted));
                    rv_shipped_orders.setAdapter(new ShippedOrdersAdapter(acticity,BawarchiOrders.this,list_shipped));
                    rv_cooking_orders.setAdapter(new CookingOrdersAdapters(acticity,BawarchiOrders.this,list_cooking));
                    rv_pending_orders.setAdapter(new PendingOrdersAdapter(acticity,BawarchiOrders.this,list_pending));
                    if(list_pending.size()==0){
                        txt_norec.setVisibility(View.VISIBLE);
                        txt_norec.setText("No Pending Orders");
                    }
                    else {
                        txt_norec.setVisibility(View.GONE);
                    }
                }
            });





            //get dates

           /* Date date= Calendar.getInstance().getTime();
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
            }*/


            //set meal types of each date


            /*finalList.clear();

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
*/


            acticity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //rv_f_schedule.setAdapter(new FoodieScheduleAdapter(FoodieDashboard.this,finalList,dateArray));

                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }


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

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_pending:
                rv_pending_orders.setVisibility(View.VISIBLE);
                rv_cooking_orders.setVisibility(View.GONE);
                rv_completed_orders.setVisibility(View.GONE);

                rv_accepted_orders.setVisibility(View.GONE);
                rv_shipped_orders.setVisibility(View.GONE);

                btn_accepted.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_shipped.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));

                btn_pending.setBackground(new ColorDrawable(getResources().getColor(R.color.semi_transparent)));
                btn_cooking.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_completed.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));


                if(list_pending.size()==0){
                    txt_norec.setVisibility(View.VISIBLE);
                    txt_norec.setText("No Pending Orders");
                }
                else {
                    txt_norec.setVisibility(View.GONE);
                }


                break;

            case R.id.btn_progress:
                rv_pending_orders.setVisibility(View.GONE);
                rv_cooking_orders.setVisibility(View.VISIBLE);
                rv_completed_orders.setVisibility(View.GONE);

                rv_accepted_orders.setVisibility(View.GONE);
                rv_shipped_orders.setVisibility(View.GONE);

                btn_accepted.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_shipped.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));


                btn_pending.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_cooking.setBackground(new ColorDrawable(getResources().getColor(R.color.semi_transparent)));
                btn_completed.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));


                if(list_cooking.size()==0){
                    txt_norec.setVisibility(View.VISIBLE);
                    txt_norec.setText("No Cooking Orders");
                }
                else {
                    txt_norec.setVisibility(View.GONE);
                }


                break;

            case R.id.btn_completed:
                rv_pending_orders.setVisibility(View.GONE);
                rv_cooking_orders.setVisibility(View.GONE);
                rv_completed_orders.setVisibility(View.VISIBLE);

                rv_accepted_orders.setVisibility(View.GONE);
                rv_shipped_orders.setVisibility(View.GONE);

                btn_accepted.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_shipped.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));

                btn_pending.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_cooking.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_completed.setBackground(new ColorDrawable(getResources().getColor(R.color.semi_transparent)));

                if(list_completed.size()==0){
                    txt_norec.setVisibility(View.VISIBLE);
                    txt_norec.setText("No Completed Orders");
                }
                else {
                    txt_norec.setVisibility(View.GONE);
                }


                break;


            case R.id.btn_accepted:
                rv_pending_orders.setVisibility(View.GONE);
                rv_cooking_orders.setVisibility(View.GONE);
                rv_completed_orders.setVisibility(View.GONE);

                rv_accepted_orders.setVisibility(View.VISIBLE);
                rv_shipped_orders.setVisibility(View.GONE);

                btn_accepted.setBackground(new ColorDrawable(getResources().getColor(R.color.semi_transparent)));
                btn_shipped.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));

                btn_pending.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_cooking.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_completed.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));


                if(list_saccepted.size()==0){
                    txt_norec.setVisibility(View.VISIBLE);
                    txt_norec.setText("No Accepted Orders");
                }
                else {
                    txt_norec.setVisibility(View.GONE);
                }

                break;


            case R.id.btn_shipped:
                rv_pending_orders.setVisibility(View.GONE);
                rv_cooking_orders.setVisibility(View.GONE);
                rv_completed_orders.setVisibility(View.GONE);

                rv_accepted_orders.setVisibility(View.GONE);
                rv_shipped_orders.setVisibility(View.VISIBLE);

                btn_accepted.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_shipped.setBackground(new ColorDrawable(getResources().getColor(R.color.semi_transparent)));

                btn_pending.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_cooking.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));
                btn_completed.setBackground(new ColorDrawable(getResources().getColor(R.color.transparent)));


                if(list_shipped.size()==0){
                    txt_norec.setVisibility(View.VISIBLE);
                    txt_norec.setText("No Shipped Orders");
                }
                else {
                    txt_norec.setVisibility(View.GONE);
                }

                break;


        }


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
