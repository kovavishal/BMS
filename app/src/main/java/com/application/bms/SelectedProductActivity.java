package com.application.bms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.SelectedStockDetails;
import com.application.bms.model.StockDetails;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class SelectedProductActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper databaseHelper;
    private SelectedProductRecyclerViewAdapter adapter;
    ListView itemListView;
    String groupName, customerCode, customerName;
    List<StockDetails> stockDetails;
    List<SelectedStockDetails> selectedStockDetails;
    private ProgressDialog pDialog;
    String userName,expiryDate;
    DrawerLayout drawer;
    TextView productNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);

        databaseHelper = new DatabaseHelper(this);


        userName = getIntent().getStringExtra("userName");
        expiryDate =  getIntent().getStringExtra("expiry_date");
        customerCode = getIntent().getStringExtra("customerCode");
        customerName = getIntent().getStringExtra("customerName");
        groupName = getIntent().getStringExtra("name");


        //side navigation code
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        final Button openNav = findViewById(R.id.openNav);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.userName);
        navName.setText(userName);
        TextView navReName = headerView.findViewById(R.id.navReName);
        navReName.setText("Executive");
        Button closeNav = headerView.findViewById(R.id.closeNav);
        Menu nav_Menu = navigationView.getMenu();
//        MenuItem nav_dashboard = nav_Menu.findItem(R.id.logout);
//        nav_dashboard.setVisible(true);
        openNav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("opening nav bar");
                        openDrawer();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(openNav.getWindowToken(), 0);
                    }
                });

        closeNav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeDrawer(null);
                    }

                });
//side navigation code


        itemListView = findViewById(R.id.productList);
        productNameTV = findViewById(R.id.textView11);
        EditText editText = findViewById(R.id.customerSearchView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





//        pDialog = new ProgressDialog(SelectedProductActivity.this);
//        pDialog.setMessage("Please Wait!!!");
//        pDialog.setCancelable(false);
//        pDialog.show();



        try {
            if (groupName.equalsIgnoreCase("ALLGROUPS")) {
//                Toast.makeText(getApplicationContext(), groupName, Toast.LENGTH_LONG).show();
                System.out.println("====================iam in allgroups==============");
                productNameTV.setText("All Groups");
                stockDetails = databaseHelper.getStockData();
                selectedStockDetails = databaseHelper.getSelectedStockGroupData(customerCode);
            } else {
                productNameTV.setText(groupName);
                String getCode = databaseHelper.getGroupCode(groupName);
                stockDetails = databaseHelper.getStockGroupData(getCode);
                selectedStockDetails = databaseHelper.getSelectedStockGroupData(customerCode);
//                new BackgroundTask(SelectedProductActivity.this) {
//                    @Override
//                    public void doInBackground() {
//                        //put you background code
//                        //same like doingBackground
//                        //Background Thread
//                        String getCode = databaseHelper.getGroupCode(groupName);
//                        stockDetails = databaseHelper.getStockGroupData(getCode);
//                        selectedStockDetails = databaseHelper.getSelectedStockGroupData(customerCode);
//
//                    }
//
//                    @Override
//                    public void onPostExecute() {
//                        if (pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        //hear is result part same
//                        //same like post execute
//                        //UI Thread(update your UI widget)
//
//
//                        adapter = new SelectedProductRecyclerViewAdapter(getApplicationContext(), stockDetails, getIntent().getStringExtra("customerCode"), getIntent().getStringExtra("customerName"), groupName, selectedStockDetails);
//                        itemListView.setAdapter(adapter);
//                    }
//                }.execute();


            }


            if(stockDetails!=null && stockDetails.size() > 0) {
                System.out.println("stockDetails");
                adapter = new SelectedProductRecyclerViewAdapter(this, stockDetails, customerCode, customerName, groupName, selectedStockDetails,userName,expiryDate);
                itemListView.setAdapter(adapter);
            }



        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        closeDrawer(null);
        int id = menuItem.getItemId();
        if (id == R.id.logout) {
            Intent intent = new Intent(SelectedProductActivity.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(SelectedProductActivity.this, ScreenStock.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(SelectedProductActivity.this, BookingCustomerListActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }



        return true;
    }

    public void closeDrawer(DrawerLayout.DrawerListener listener) {
        drawer.setDrawerListener(listener);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        drawer.setDrawerListener(null);
        drawer.openDrawer(GravityCompat.START);
    }



//    private void filter(String text) {
//        ArrayList<StockDetails> filteredList = new ArrayList<>();
//
//        for (StockDetails item : stockDetails) {
//            if (item.getItemName() != null) {
//                if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
//                    filteredList.add(item);
//                }
//            }
//        }
//        adapter.filterList(filteredList);
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectedProductActivity.this, GroupListActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("customerName", customerName);
        intent.putExtra("customerCode", customerCode);
        intent.putExtra("expiry_date", expiryDate);
        startActivity(intent);
    }


}

