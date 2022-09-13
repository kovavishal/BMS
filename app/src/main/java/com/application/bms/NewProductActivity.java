package com.application.bms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.bms.model.Bills;
import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.Orders;
import com.application.bms.model.SelectedProductDetails;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewProductActivity extends Activity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    String mName, customerCode;
    TextView getDateTime, customerName, mTotalAmount;
    Button mCancel, addItem, saveOrder;
    View view;
    DatabaseHelper databaseHelper;
    ListView recyclerView;
    public NewProductActivityProductRecyclerViewAdapter adapter;
    List<SelectedProductDetails> selectedProductDetails;
    String totalAmount = "";

    DrawerLayout drawer;
    String userName, expiryDate;
    final Context context = this;
    List<Bills> billDetails;
    List<Orders> ordersDetails;
    int savedStatus = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new);

        userName = getIntent().getStringExtra("userName");
        expiryDate = getIntent().getStringExtra("expiry_date");

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


        databaseHelper = new DatabaseHelper(this);

        mName = getIntent().getStringExtra("name");
        customerCode = getIntent().getStringExtra("customerCode");


        getDateTime = (TextView) findViewById(R.id.getDateTime);
        customerName = (TextView) findViewById(R.id.mCustomer);
        mTotalAmount = (TextView) findViewById(R.id.totalAmount);
        mCancel = (Button) findViewById(R.id.cancel);
        addItem = (Button) findViewById(R.id.addItem);
        saveOrder = (Button) findViewById(R.id.saveOrder);

        savedStatus = databaseHelper.getcustomerCodeFromBills(customerCode);
        System.out.println("customer saved status=====" + savedStatus);

//        if(savedStatus > 0){
//            saveOrder.setEnabled(false);
//        }

        mCancel.setOnClickListener(this);
        addItem.setOnClickListener(this);
        saveOrder.setOnClickListener(this);

        totalAmount = databaseHelper.getCustomerTotalAmount(customerCode);
//        System.out.println("totalAmount=="+totalAmount);
        if (totalAmount == null) {
            mTotalAmount.setText("TOTAL AMOUNT : 0");
        } else {
            mTotalAmount.setText("TOTAL AMOUNT : " + totalAmount);
        }

        customerName.setText(mName);
        String date_n = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());
        getDateTime.setText(date_n);

        selectedProductDetails = databaseHelper.selectSelectedProductDetailsWithoutName(customerCode);
        recyclerView = (ListView) findViewById(R.id.new_product_recyclerview);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewProductActivityProductRecyclerViewAdapter(this, selectedProductDetails, customerCode, userName ,expiryDate);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        view = v;

        int id = v.getId();
        switch (id) {
            case R.id.saveOrder:
//                productActivityAletBox("Save preorder card?","CONFRIM","Abort","save");
//                Intent saveOrderIntent=new Intent(this, BookingCustomerListActivity.class);
//                startActivity(saveOrderIntent);
                if (totalAmount != null && Double.parseDouble(totalAmount) > 0) {
                    if (savedStatus == 0) {

                        Dialog dialog = new Dialog(this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_alert_dialouge);
                        dialog.setCancelable(false);
                        Button yesButton = (Button) dialog.findViewById(R.id.yes);
                        Button noButton = (Button) dialog.findViewById(R.id.no);
                        TextView title = (TextView) dialog.findViewById(R.id.title);
                        TextView message = (TextView) dialog.findViewById(R.id.message);
                        title.setText("Save Order");
                        message.setText("Are you sure want to Save?");

                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseHelper = new DatabaseHelper(context);
                                String currentTime = "";
                                billDetails = databaseHelper.selectBillsDetails(customerCode);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    currentTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());
                                }
                                databaseHelper.insertBills(billDetails, currentTime);
                                ordersDetails = databaseHelper.getOrderDeatils(customerCode);
                                databaseHelper.insertOrders(ordersDetails);
                                saveOrder.setEnabled(false);
                                savedStatus = 1;
                                Toast.makeText(context, "Saved successfully.", Toast.LENGTH_LONG).show();
                                Intent pendingOrderIntent = new Intent(NewProductActivity.this, BookingCustomerListActivity.class);
                                pendingOrderIntent.putExtra("userName", userName);
                                pendingOrderIntent.putExtra("customerCode", customerCode);
                                pendingOrderIntent.putExtra("customerName", mName);
                                pendingOrderIntent.putExtra("expiry_date", expiryDate);
                                startActivity(pendingOrderIntent);


//                        if (databaseHelper.deleteAddedItem(custCode, ProductCode,mrp)) {
//                            databaseHelper.increaseQty(qty, ProductCode,mrp);
//
//                            Toast.makeText(context, "Delete successful.", Toast.LENGTH_LONG).show();
//                            runSheetListDisplayed.remove(itPosition);
//                            adapter.notifyDataSetChanged();
//                            dialog.dismiss();
//                        }
//                        if() {
//
//                        }
//                        else{
//                            Toast.makeText(context, "Delete failed.", Toast.LENGTH_LONG).show();
//                            dialog.dismiss();
//                        }
                                dialog.dismiss();
                            }
                        });
                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        Toast.makeText(context, "Already Saved", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "No Items to Save", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.cancel:
//                productActivityAletBox("Delete preorder card?","Delete","Abort","cancel");
//                Intent newOrderIntent=new Intent(this, NewProductActivity.class);
////                newOrderIntent.putExtra("name", mCustomerName);
//                startActivity(newOrderIntent);


                if (totalAmount != null && Double.parseDouble(totalAmount) > 0) {


                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_alert_dialouge);
                    dialog.setCancelable(false);
                    Button yesButton = (Button) dialog.findViewById(R.id.yes);
                    Button noButton = (Button) dialog.findViewById(R.id.no);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    TextView message = (TextView) dialog.findViewById(R.id.message);
                    title.setText("Cancel Order");
                    message.setText("Are you sure you want to Cancel?");

                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseHelper = new DatabaseHelper(context);
                            String currentTime = "";

                            if (databaseHelper.deleteItemsFromCart(customerCode)) {
                                if (databaseHelper.deleteItemsFromBills(customerCode)) {
                                    if (databaseHelper.deleteItemsFromOrders(customerCode)) {
                                        Toast.makeText(context, "Canceled Succesfully", Toast.LENGTH_LONG).show();

                                        Intent pendingOrderIntent = new Intent(NewProductActivity.this, BookingCustomerListActivity.class);
                                        pendingOrderIntent.putExtra("userName", userName);
                                        pendingOrderIntent.putExtra("customerCode", customerCode);
                                        pendingOrderIntent.putExtra("customerName", mName);
                                        pendingOrderIntent.putExtra("expiry_date", expiryDate);
                                        startActivity(pendingOrderIntent);
                                    }
                                }
                            }
                            dialog.dismiss();

                        }
                    });
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(context, "No Items To Cancel", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.addItem:

//                if (savedStatus == 0) {
                Intent pendingOrderIntent = new Intent(this, GroupListActivity.class);
                pendingOrderIntent.putExtra("userName", userName);
                pendingOrderIntent.putExtra("customerCode", customerCode);
                pendingOrderIntent.putExtra("customerName", mName);
                pendingOrderIntent.putExtra("expiry_date", expiryDate);
                startActivity(pendingOrderIntent);
//                } else {
//                    Toast.makeText(context, "Already Saved", Toast.LENGTH_LONG).show();
//                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        exiteDialog();

        if (selectedProductDetails.size() > 0 && savedStatus == 0) {

            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_alert_dialouge);
            dialog.setCancelable(false);
            Button yesButton = (Button) dialog.findViewById(R.id.yes);
            Button noButton = (Button) dialog.findViewById(R.id.no);
            yesButton.setText("Save");
            noButton.setText("later");
            TextView title = (TextView) dialog.findViewById(R.id.title);
            TextView message = (TextView) dialog.findViewById(R.id.message);
            title.setText("Alert");
            message.setText("You Are Leaving Without Saving");


            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper = new DatabaseHelper(context);
                    String currentTime = "";
                    billDetails = databaseHelper.selectBillsDetails(customerCode);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        currentTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());
                    }
                    databaseHelper.insertBills(billDetails, currentTime);
                    ordersDetails = databaseHelper.getOrderDeatils(customerCode);
                    databaseHelper.insertOrders(ordersDetails);

                    Intent intent = new Intent(NewProductActivity.this, BookingCustomerListActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("expiry_date", expiryDate);
                    startActivity(intent);
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(NewProductActivity.this, BookingCustomerListActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("expiry_date", expiryDate);
                    startActivity(intent);
                }
            });
            dialog.show();
        } else {
            Intent intent = new Intent(NewProductActivity.this, BookingCustomerListActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("expiry_date", expiryDate);
            startActivity(intent);
        }

    }

    public void moveActivity() {
        finish();
    }

    public void exiteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setMessage("Tap Close to exite the order will be cancelled or press the back button to return to the order screen");
        builder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        moveActivity();
//                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();

    }

    public void productActivityAletBox(String title, String postiveButton, String negativeButton, String buttonView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setMessage(title);
        builder.setPositiveButton(postiveButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (buttonView.equals("save")) {
                            finish();
                        } else if (buttonView.equals("cancel")) {
                            finish();
                        }
//                        Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton(negativeButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        builder.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        closeDrawer(null);
        int id = menuItem.getItemId();
        if (id == R.id.logout) {
            Intent intent = new Intent(NewProductActivity.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(NewProductActivity.this, ScreenStock.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(NewProductActivity.this, BookingCustomerListActivity.class);
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
