package com.application.bms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.StockDetails;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ScreenStock extends Activity implements NavigationView.OnNavigationItemSelectedListener{
    View view;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    List<StockDetails> stockDetails = new ArrayList<>();;
    private StockListRecyclerViewAdapter adapter;
    Integer no_row;
    ListView stockListView;
    String userName,expiryDate;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_screen);
        databaseHelper = new DatabaseHelper(this);

        userName =  getIntent().getStringExtra("userName");
        expiryDate =  getIntent().getStringExtra("expiry_date");

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





        List<StockDetails> stockDetails = databaseHelper.getStockData();
        stockListView = (ListView) findViewById(R.id.stockListView);

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



//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter= new BookingCustomerListAdapter(this,stockDetails);

        adapter = new StockListRecyclerViewAdapter(this, stockDetails);
        stockListView.setAdapter(adapter);


        //   Snackbar.make(view, (CharSequence) "Stock List is Empty", BaseTransientBottomBar.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ScreenStock.this, HomeScreenActivity.class);
        intent.putExtra("username",userName);
        intent.putExtra("expiry_date", expiryDate);
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        closeDrawer(null);
        int id = menuItem.getItemId();
        if (id == R.id.logout) {
            Intent intent = new Intent(ScreenStock.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(ScreenStock.this, ScreenStock.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(ScreenStock.this, BookingCustomerListActivity.class);
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




}
