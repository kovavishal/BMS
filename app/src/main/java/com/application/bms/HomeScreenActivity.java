package com.application.bms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.bms.model.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class HomeScreenActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {


    CardView importButton, exportButton, orderBookingButton, orderSummaryButton, stockButton, custListButton;
    TextView totalOrdersTV, totalAmountTV, totalItemsTV, phoneNumberTV;
    String totalOrders = "0", totalAmount = "0", totalItems = "0", userName, userNumber;
    DatabaseHelper databaseHelper;
    DrawerLayout drawer;
    Dialog dialog;
    final Context context = this;

//    public static final long DESTROY_APP_TH = 432000000;

    private String EVENT_DATE_TIME = null;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Handler handler = new Handler();
    private Runnable runnable;
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second;


    //    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sampledashboard);
        userName = getIntent().getStringExtra("username");
        EVENT_DATE_TIME = getIntent().getStringExtra("expiry_date");




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

//        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
//                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
//            userNumber = tMgr.getLine1Number();
//            phoneNumberTV.setText("PhoneNumber : "+userNumber);
////            Toast.makeText(context, "ur number=="+mPhoneNumber, Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            requestPermission();
//        }


        initUI();
        countDownStart();


//        String currentTime = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
//        Log.d("timeStamp", currentTime);
//        Calendar date = new GregorianCalendar(2021, Calendar.JULY, 19);
//        date.add(Calendar.DAY_OF_WEEK, 0);
//        String expireTime = new SimpleDateFormat("yyyyMMdd").format(date.getTime());
//
//        int intcurrentTime = Integer.parseInt(currentTime);
//        int intexpireTime = Integer.parseInt(expireTime);
//
//
//        if(intcurrentTime == intexpireTime || intcurrentTime  > intcurrentTime  ) {
//
//            //logic to set off the features of app
////            Toast.makeText(getApplicationContext(),"expired",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(HomeScreenActivity.this, ExpireScreen.class);
//            intent.putExtra("userName",userName);
//            startActivity(intent);
//
//        }


        phoneNumberTV = findViewById(R.id.phonenumber);
        totalOrdersTV = findViewById(R.id.total_orders);
        totalItemsTV = findViewById(R.id.total_items);
        totalAmountTV = findViewById(R.id.total_amount);
        importButton = findViewById(R.id.import_btn);
        exportButton = findViewById(R.id.export_btn);
        stockButton = findViewById(R.id.viewStockList);
        orderSummaryButton = findViewById(R.id.order_summary_btn);
        orderBookingButton = findViewById(R.id.order_booking_btn);
        custListButton = findViewById(R.id.viewCustList);








        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        String n = tm.getLine1Number();
        userNumber = tm.getDeviceId();
        if(userNumber == null){
            userNumber = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        phoneNumberTV.setText("DeviceId : "+userNumber);




        databaseHelper = new DatabaseHelper(this);
        totalItems = databaseHelper.getTotalOrderItemsCount();
        totalAmount = databaseHelper.getTotalAmount();
        totalOrders = databaseHelper.gettotalOrdersCount();

        totalItemsTV.setText(totalItems);
        if (totalAmount != null) {
            totalAmountTV.setText(totalAmount);
        } else {
            totalAmountTV.setText("0");
        }
        totalOrdersTV.setText(totalOrders);

        importButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreenActivity.this, ScreenImport.class);
            intent.putExtra("userName", userName);
            intent.putExtra("expiry_date", EVENT_DATE_TIME);
            startActivity(intent);
//                finish();
        });
        exportButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreenActivity.this, ScreenExport.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userNumber", userNumber);
            intent.putExtra("expiry_date", EVENT_DATE_TIME);
            startActivity(intent);
//                finish();
        });
        stockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ScreenStock.class);
                intent.putExtra("userName", userName);
                intent.putExtra("expiry_date", EVENT_DATE_TIME);
                startActivity(intent);
//                finish();
            }
        });
        orderSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ScreenSummaryOrder.class);
                intent.putExtra("userName", userName);
                intent.putExtra("expiry_date", EVENT_DATE_TIME);
                startActivity(intent);
                finish();
            }
        });
        orderBookingButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreenActivity.this, BookingCustomerListActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("expiry_date", EVENT_DATE_TIME);
            startActivity(intent);
//                finish();
        });
        custListButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreenActivity.this, CustomerListActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("expiry_date", EVENT_DATE_TIME);
            startActivity(intent);
//                finish();
        });

    }


//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 100:
//                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
//                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
//                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=      PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                userNumber = tMgr.getLine1Number();
//                phoneNumberTV.setText("PhoneNumber : "+userNumber);
////                Toast.makeText(context, "ur number=="+mPhoneNumber, Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle("Leave application?");
//        dialog.setMessage("Are you sure you want to leave the application?");
        dialog.setContentView(R.layout.custom_alert_dialouge);
        dialog.setCancelable(false);
        Button yesButton = (Button) dialog.findViewById(R.id.yes);
        Button noButton = (Button) dialog.findViewById(R.id.no);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        title.setText("Leave application?");
        message.setText("Are you sure you want to leave the application?");


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, CheckLoginActivity.class);
                startActivity(intent);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        closeDrawer(null);
        int id = menuItem.getItemId();
        if (id == R.id.logout) {
            Intent intent = new Intent(HomeScreenActivity.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(HomeScreenActivity.this, ScreenStock.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(HomeScreenActivity.this, BookingCustomerListActivity.class);
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


    private void initUI() {
        linear_layout_1 = findViewById(R.id.linear_layout_1);
        linear_layout_2 = findViewById(R.id.linear_layout_2);
        tv_days = findViewById(R.id.tv_days);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);
    }


    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;


                        if (Days < 30) {
                            tv_days.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_error));
                            tv_hour.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_error));
                            tv_minute.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_error));
                            tv_second.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_error));
                        }
                        //
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
                        linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);

                        Intent intent = new Intent(HomeScreenActivity.this, ExpireScreen.class);
                        startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }


}
