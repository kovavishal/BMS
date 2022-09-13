package com.application.bms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.bms.model.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "WelcomeGroupListActivity";
    ItemArrayAdapter itemArrayAdapter;
    Intent importFileIntent;
    ListView listView;
    List groupList;
    DatabaseHelper databaseHelper;
    String path;
    String[] mobileArray;
    private DatabaseHelper dbHelper;
    DrawerLayout drawer;
    String userName, customerName, customerCode,expiryDate;

    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_manufacturer);

        userName = getIntent().getStringExtra("userName");
        customerName = getIntent().getStringExtra("customerName");
        customerCode = getIntent().getStringExtra("customerCode");
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
//        MenuItem nav_dashboard = nav_Menu.findItem(R.id.nav_dashboard);
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


        listView = (ListView) findViewById(R.id.listView);
        Button allGroups = findViewById(R.id.textViews4);


        allGroups.setOnClickListener(v -> {

            Intent intent = new Intent(GroupListActivity.this, SelectedProductActivity.class);
            intent.putExtra("name", "ALLGROUPS");
            intent.putExtra("customerCode", customerCode);
            intent.putExtra("customerName", customerName);
            intent.putExtra("userName", userName);
            intent.putExtra("expiry_date", expiryDate);
            startActivity(intent);
        });


        ArrayList<String> mylist = new ArrayList<String>();
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        final List newList = databaseHelper.selectGroupDetails();


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.select_manufacturer_item_layout, R.id.group_item, newList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String productName = (String) newList.get(position);
//                String productCode = (String) newList.get(position);
//                Toast.makeText(this,"",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GroupListActivity.this, SelectedProductActivity.class);
                intent.putExtra("name", productName);
                intent.putExtra("customerCode", customerCode);
                intent.putExtra("customerName", customerName);
                intent.putExtra("userName", userName);
                intent.putExtra("expiry_date", expiryDate);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GroupListActivity.this, NewProductActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("customerCode", customerCode);
        intent.putExtra("name", customerName);
        intent.putExtra("expiry_date", expiryDate);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        closeDrawer(null);
        int id = menuItem.getItemId();
        if (id == R.id.logout) {
            Intent intent = new Intent(GroupListActivity.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(GroupListActivity.this, ScreenStock.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(GroupListActivity.this, BookingCustomerListActivity.class);
            intent.putExtra("userName", userName);
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
