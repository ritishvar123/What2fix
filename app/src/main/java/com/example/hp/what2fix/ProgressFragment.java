package com.example.hp.what2fix;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;

public class ProgressFragment extends Fragment {
    ListView listView3;
    ProgressDialog progressDialog;
    ListAdapter myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    static int flag2 = 0;
    String[] sortItems = {"A-Z", "Z-A", "Newest first", "Oldest first", "Order Low to High", "Order High to Low"};
    int selectedItem;
    Name[] customer_name;
    Id[] customer_id;
    Date[] customer_date;
    Address[] customer_address;
    Phno[] customer_phno;
    Status[] customer_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        setHasOptionsMenu(true);
        listView3 = (ListView) v.findViewById(R.id.listView3);
        progressDialog = new ProgressDialog(getContext());
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_progress);
        if (flag2==0) {
            fetchData();
            flag2++;
        } else {
            myAdapter=new MyAdapter(getContext(),R.layout.item,JsonParseProgress.orderId,JsonParseProgress.customerName,JsonParseProgress.date);
            listView3.setAdapter(myAdapter);
        }
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getBaseContext(), DetailsCustomerProgress.class);
                    intent.putExtra("position", "" + position);
                    startActivity(intent);
                }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnline()){
                    Toast.makeText(getContext(), "Check your internet connection !!", Toast.LENGTH_LONG).show();
                } else fetchData();
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.progress_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_sortby){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setTitle("Sort By");
            alertBuilder.setSingleChoiceItems(sortItems, -1, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedItem = i;
                }
            });
            alertBuilder.setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (selectedItem==0){
                                EntityName[] array = new EntityName[JsonParseProgress.customerName.length];
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    array[j] = new EntityName(customer_name[j], customer_id[j], customer_date[j], customer_address[j],
                                            customer_phno[j], customer_status[j]);
                                }
                                Arrays.sort(array);
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    customer_name[j] = array[j].name;
                                    customer_id[j] = array[j].id;
                                    customer_date[j] = array[j].date;
                                    customer_address[j] = array[j].address;
                                    customer_phno[j] = array[j].phno;
                                    customer_status[j] = array[j].status;
                                }
                            } else if (selectedItem==1){
                                EntityName[] array = new EntityName[JsonParseProgress.customerName.length];
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    array[j] = new EntityName(customer_name[j], customer_id[j], customer_date[j], customer_address[j],
                                            customer_phno[j], customer_status[j]);
                                }
                                Arrays.sort(array, Collections.reverseOrder());
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    customer_name[j] = array[j].name;
                                    customer_id[j] = array[j].id;
                                    customer_date[j] = array[j].date;
                                    customer_address[j] = array[j].address;
                                    customer_phno[j] = array[j].phno;
                                    customer_status[j] = array[j].status;
                                }
                            } else if (selectedItem==2){
                                EntityDate[] array = new EntityDate[JsonParseProgress.customerName.length];
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    array[j] = new EntityDate(customer_name[j], customer_id[j], customer_date[j], customer_address[j],
                                            customer_phno[j], customer_status[j]);
                                }
                                Arrays.sort(array, Collections.reverseOrder());
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    customer_name[j] = array[j].name;
                                    customer_id[j] = array[j].id;
                                    customer_date[j] = array[j].date;
                                    customer_address[j] = array[j].address;
                                    customer_phno[j] = array[j].phno;
                                    customer_status[j] = array[j].status;
                                }
                            } else if (selectedItem==3){
                                EntityDate[] array = new EntityDate[JsonParseProgress.customerName.length];
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    array[j] = new EntityDate(customer_name[j], customer_id[j], customer_date[j], customer_address[j],
                                            customer_phno[j], customer_status[j]);
                                }
                                Arrays.sort(array);
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    customer_name[j] = array[j].name;
                                    customer_id[j] = array[j].id;
                                    customer_date[j] = array[j].date;
                                    customer_address[j] = array[j].address;
                                    customer_phno[j] = array[j].phno;
                                    customer_status[j] = array[j].status;
                                }
                            } else if (selectedItem==4){
                                EntityId[] array = new EntityId[JsonParseProgress.customerName.length];
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    array[j] = new EntityId(customer_name[j], customer_id[j], customer_date[j], customer_address[j],
                                            customer_phno[j], customer_status[j]);
                                }
                                Arrays.sort(array);
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    customer_name[j] = array[j].name;
                                    customer_id[j] = array[j].id;
                                    customer_date[j] = array[j].date;
                                    customer_address[j] = array[j].address;
                                    customer_phno[j] = array[j].phno;
                                    customer_status[j] = array[j].status;
                                }
                            } else if (selectedItem==5){
                                EntityId[] array = new EntityId[JsonParseProgress.customerName.length];
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    array[j] = new EntityId(customer_name[j], customer_id[j], customer_date[j], customer_address[j],
                                            customer_phno[j], customer_status[j]);
                                }
                                Arrays.sort(array, Collections.reverseOrder());
                                for (int j = 0; j < JsonParseProgress.customerName.length; j++ ) {
                                    customer_name[j] = array[j].name;
                                    customer_id[j] = array[j].id;
                                    customer_date[j] = array[j].date;
                                    customer_address[j] = array[j].address;
                                    customer_phno[j] = array[j].phno;
                                    customer_status[j] = array[j].status;
                                }
                            }
                            for (int j=0; j<customer_name.length; j++){
                                JsonParseProgress.customerName[j] = customer_name[j].getName();
                                JsonParseProgress.orderId[j] = customer_id[j].getId();
                                JsonParseProgress.date[j] = customer_date[j].getDate();
                                JsonParseProgress.address[j] = customer_address[j].getAddress();
                                JsonParseProgress.phoneNumber[j] = customer_phno[j].getPhno();
                                JsonParseProgress.status[j] = customer_status[j].getStatus();
                            }
                            /*Toast.makeText(getContext(), Arrays.toString(names)+"\n"+Arrays.toString(ids)+"\n"+Arrays.toString(dates),
                                    Toast.LENGTH_LONG).show();*/
                            myAdapter=new MyAdapter(getContext(),R.layout.item,JsonParseProgress.orderId, JsonParseProgress.customerName,
                                    JsonParseProgress.date);
                            listView3.setAdapter(myAdapter);

                        }
                    })
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
            Button positive = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            Button negative = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positive.setTextColor(Color.parseColor("#014c6f"));
            negative.setTextColor(Color.parseColor("#014c6f"));
        } else if (id == R.id.action_logout) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("Do you want to logout ?").setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog close = alert.create();
            close.setTitle("Logout");
            close.show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void fetchData() {
        swipeRefreshLayout.setRefreshing(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url="https://boxinall.in/kshitiz/fetch_progress_customer.php"; // change link
        StringRequest stringRequest= new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonParsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error in response", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void jsonParsing(String response) {
        JsonParseProgress jsonParse = new JsonParseProgress(response);
        jsonParse.jsonTodo();
        myAdapter=new MyAdapter(getContext(),R.layout.item,JsonParseProgress.orderId,JsonParseProgress.customerName,
                JsonParseProgress.date);
        listView3.setAdapter(myAdapter);
        customer_name = new Name[JsonParseProgress.customerName.length];
        customer_id = new Id[JsonParseProgress.customerName.length];
        customer_date = new Date[JsonParseProgress.customerName.length];
        customer_address = new Address[JsonParseProgress.customerName.length];
        customer_phno = new Phno[JsonParseProgress.customerName.length];
        customer_status = new Status[JsonParseProgress.customerName.length];
        for (int j=0; j<JsonParseProgress.customerName.length; j++){
            customer_name[j] = new Name(JsonParseProgress.customerName[j]);
            customer_id[j] = new Id(JsonParseProgress.orderId[j]);
            customer_date[j] = new Date(JsonParseProgress.date[j]);
            customer_address[j] = new Address(JsonParseProgress.address[j]);
            customer_phno[j] = new Phno(JsonParseProgress.phoneNumber[j]);
            customer_status[j] = new Status(JsonParseProgress.status[j]);
        }
        swipeRefreshLayout.setRefreshing(false);
        progressDialog.hide();
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

}

