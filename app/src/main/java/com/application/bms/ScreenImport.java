package com.application.bms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.bms.model.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ScreenImport extends Activity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    Intent importFileIntent;
    Button btnCustomer, btnGroup, btnStock,clrAllBtn;
    View view;
    ItemArrayAdapter itemArrayAdapter;
    ListView listView;
    DatabaseHelper databaseHelper;
    String path;
    GroupListActivity groupListActivity;
    final Context context = this;
    public static final int RequestPermissionCode = 3;
    String userName,expiryDate;
    DrawerLayout drawer;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_screen);
        userName = getIntent().getStringExtra("userName");
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


        btnCustomer = findViewById(R.id.imt_customer_btn);
        btnGroup = findViewById(R.id.imt_group_btn);
        btnStock = findViewById(R.id.imt_stock_btn);
        clrAllBtn = (Button) findViewById(R.id.clear_btn);
        btnCustomer.setOnClickListener(this);
        btnGroup.setOnClickListener(this);
        btnStock.setOnClickListener(this);
        clrAllBtn.setOnClickListener(this);
//        MainActivity mainActivity=new MainActivity();
//        mainActivity.didCall();

        requestPermission();


    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(ScreenImport.this, HomeScreenActivity.class);
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
            Intent intent = new Intent(ScreenImport.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(ScreenImport.this, ScreenStock.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(ScreenImport.this, BookingCustomerListActivity.class);
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

    @Override
    public void onClick(View v) {
        checkRunTimePermission();
        checkPermission();
        view = v;
        int id = v.getId();
        switch (id) {
            case R.id.imt_customer_btn:
                getCustomerFile();
                break;
            case R.id.imt_group_btn:

                getGroupFile();
                break;
            case R.id.imt_stock_btn:
                getStockFile();
                break;
            case R.id.clear_btn:

                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_alert_dialouge);
                dialog.setCancelable(false);
                Button yesButton = (Button) dialog.findViewById(R.id.yes);
                Button noButton = (Button) dialog.findViewById(R.id.no);
                TextView title = (TextView) dialog.findViewById(R.id.title);
                TextView message = (TextView) dialog.findViewById(R.id.message);
                title.setText("Alert");
                message.setText("Are you sure want to Clear?");

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearAll();
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
                break;
        }
    }

    public void getGroupFile() {
        importFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        importFileIntent.setType("text/csv");
        startActivityForResult(importFileIntent, 1001);
    }

    public void getCustomerFile() {
        importFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        importFileIntent.setType("text/csv");
        startActivityForResult(importFileIntent, 1003);
    }

    public void getStockFile() {
        importFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        importFileIntent.setType("text/csv");
        startActivityForResult(importFileIntent, 1005);
    }

    public void wrongFileChoosen(String msg) {
        String message = msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ScreenImport.this);
        builder.setTitle("Wrong File Choosen");
        builder.setMessage(message);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
//                        Toast.makeText(getApplicationContext(), "Yes is clicked", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();

    }

    public boolean fileGroupNameCheck(String fileName) {
        boolean result = false;
        String message;
        if (fileName.equalsIgnoreCase("group.csv")) {
            result = true;
        } else {
            message = "Please Select GROUP.CSV file";
            wrongFileChoosen(message);
        }
        return result;
    }

    public boolean fileCustomerNameCheck(String fileName) {
        boolean result = false;
        String message;
        if (fileName.equalsIgnoreCase("customer.csv")) {
            result = true;
        } else {
            message = "Please Select CUSTOMER.CSV file";
            wrongFileChoosen(message);
        }
        return result;
    }

    public boolean fileStockNameCheck(String fileName) {
        boolean result = false;
        String message;
        if (fileName.equalsIgnoreCase("stock.csv")) {
            result = true;
        } else {
            message = "Please Select STOCK.CSV file";
            wrongFileChoosen(message);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 1001) {
            try {
                path = data.getData().getPath();
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
                if (fileGroupNameCheck(fileName)) {
                    System.out.println("iam at 1");
                    CSVFile csvFile = new CSVFile(new FileInputStream(this.path));
                    DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
                    db.openDB();
                    List list = (csvFile.groupDataRead());
                    db.insertGroupData(list);
                    db.closeDB();
//                    Intent customerIntent = new Intent(this, GroupListActivity.class);
//                    startActivity(customerIntent);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == 1003) {
            path = data.getData().getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
            System.out.println("iam at before filename check");
            if (fileCustomerNameCheck(fileName)) {
                System.out.println("iam at after filename check");
                try {
                    CSVFile csvFile = new CSVFile(new FileInputStream(this.path));
//                        File csvFILE = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "customer.csv");
//                        System.out.println("csvFILE========"+csvFILE);

                    DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
                    db.openDB();
                    System.out.println("iam at 2");
                    List list = (csvFile.customerDataRead());
                    boolean result = db.customerTable(list);
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "failed to  Update", Toast.LENGTH_LONG).show();
                    }

                    db.closeDB();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } else if (resultCode == RESULT_OK && requestCode == 1005) {
            path = data.getData().getPath();
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
            if (fileStockNameCheck(fileName)) {
                try {
                    CSVFile csvFile = new CSVFile(new FileInputStream(this.path));
                    DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
                    db.openDB();
                    List list = (csvFile.stockDataRead());
                    boolean result = db.stockTable(list);
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "failed to  Update", Toast.LENGTH_LONG).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(ScreenImport.this, new
                String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    System.out.println("grantResults===" + grantResults);
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
//                    boolean RecordPermission = grantResults[1] ==
//                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission) {
                        Toast.makeText(ScreenImport.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ScreenImport.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }

















    private void clearAll() {
//        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());


        pDialog = new ProgressDialog(ScreenImport.this);
        pDialog.setMessage("Please Wait!!!");
        pDialog.setCancelable(false);
        pDialog.show();

        new BackgroundTask(ScreenImport.this) {

            @Override
            public void doInBackground() {
                //put you background code
                //same like doingBackground
                //Background Thread

                DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                File exportDir = new File(Environment.getExternalStorageDirectory(), "BMS/BMSTemp");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                File file = new File(exportDir, "stockoutput"+currentDateandTime+".csv");
                File file1 = new File(exportDir, "bills"+currentDateandTime+".csv");
                File file2 = new File(exportDir, "orders"+currentDateandTime+".csv");
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    Cursor curCSV = db.rawQuery("SELECT * FROM stock_table", null);
//                    csvWrite.writeNext(curCSV.getColumnNames());

                    while (curCSV.moveToNext()) {
                        //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2), curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9),
//                        curCSV.getString(10),curCSV.getString(11),curCSV.getString(12),curCSV.getString(13),curCSV.getString(14),curCSV.getString(15),curCSV.getString(16),curCSV.getString(17)};
//                csvWrite.writeNext(arrStr);

                        String arrStr[] = null;
                        String[] mySecondStringArray = new String[curCSV.getColumnNames().length];

                        for (int i = 0; i < curCSV.getColumnNames().length; i++) {
                            mySecondStringArray[i] = curCSV.getString(i);
                        }

                        csvWrite.writeNext(mySecondStringArray);
                    }
                    csvWrite.close();
                    curCSV.close();
                    databaseHelper.deleteAllItemsFromStocks();



                    file1.createNewFile();
                    CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));
                    SQLiteDatabase db1 = dbhelper.getReadableDatabase();
                    Cursor curCSV1 = db1.rawQuery("SELECT * FROM bills", null);
//                    csvWrite1.writeNext(curCSV1.getColumnNames());
                    while (curCSV1.moveToNext()) {
                        //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2), curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9),
//                        curCSV.getString(10),curCSV.getString(11),curCSV.getString(12),curCSV.getString(13),curCSV.getString(14),curCSV.getString(15),curCSV.getString(16),curCSV.getString(17)};
//                csvWrite.writeNext(arrStr);

                        String arrStr[] = null;
                        String[] mySecondStringArray = new String[curCSV1.getColumnNames().length];
                        for (int i = 0; i < curCSV1.getColumnNames().length; i++) {
                            mySecondStringArray[i] = curCSV1.getString(i);
                        }
                        csvWrite1.writeNext(mySecondStringArray);
                    }
                    csvWrite1.close();
                    curCSV1.close();
                    databaseHelper.deleteAllItemsFromBills();


                    file2.createNewFile();
                    CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file2));
                    SQLiteDatabase db2 = dbhelper.getReadableDatabase();
//                    Cursor curCSV2 = db2.rawQuery("SELECT * FROM orders", null);
                    Cursor curCSV2 = db2.rawQuery("select a.id as bill_id,b.cust_code ,b.cust_name, " +
                            " b.manufacture_code ,b.manufacture_name ,b.product_code ,b.product_name ,b.date_time ,b.tax_code , " +
                            " b.tex_percentage ,b.qty ,b.descp , b.free_qty ,b.mrp , b.rate ,b.value ,b.discount " +
                            " from orders b join bills a on a.cust_code = b.cust_code ", null);
//                    csvWrite2.writeNext(curCSV2.getColumnNames());
                    while (curCSV2.moveToNext()) {
                        //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2), curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9),
//                        curCSV.getString(10),curCSV.getString(11),curCSV.getString(12),curCSV.getString(13),curCSV.getString(14),curCSV.getString(15),curCSV.getString(16),curCSV.getString(17)};
//                csvWrite.writeNext(arrStr);

                        String arrStr[] = null;
                        String[] mySecondStringArray = new String[curCSV2.getColumnNames().length];
                        for (int i = 0; i < curCSV2.getColumnNames().length; i++) {
                            mySecondStringArray[i] = curCSV2.getString(i);
                        }
                        csvWrite2.writeNext(mySecondStringArray);
                    }
                    csvWrite2.close();
                    curCSV2.close();

                    databaseHelper.deleteAllItemsFromOrders();


                    databaseHelper.deleteAllItemsFromCart();
                    databaseHelper.deleteAllItemsFromCustomers();
                    databaseHelper.deleteAllItemsFromGroup();


                } catch (Exception sqlEx) {
                    Log.e("ScreenExport : ", sqlEx.getMessage(), sqlEx);
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPostExecute() {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Cleared Successfully", Toast.LENGTH_LONG).show();
            }
        }.execute();


//        if (databaseHelper.deleteAllItemsFromCart()) {
//            if (databaseHelper.deleteAllItemsFromBills()) {
//                if (databaseHelper.deleteAllItemsFromOrders()) {
//                    if (databaseHelper.deleteAllItemsFromCustomers()) {
//                        if (databaseHelper.deleteAllItemsFromGroup()) {
//                            if (databaseHelper.deleteAllItemsFromStocks()) {
//                                Toast.makeText(getApplicationContext(), "Cleared Successfully", Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                    }
//
//                }
//            }
//        }
    }


}
