package com.application.bms;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.application.bms.model.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ScreenExport extends Activity implements View.OnClickListener {

    Button exportAllBtn, clrAllBtn, shareBtn;
    View view;
    TextView modelNameView;
    String modelName;
    DatabaseHelper databaseHelper;
    String userName,userNumber,expiryDate;
    int exportStatus = 0;
    String getCount;
    private ProgressDialog pDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_screen);

        userName = getIntent().getStringExtra("userName");
        userNumber = getIntent().getStringExtra("userNumber");
        expiryDate =  getIntent().getStringExtra("expiry_date");


//        System.out.println("unique number in export===="+userNumber);
        databaseHelper = new DatabaseHelper(this);
        exportAllBtn = (Button) findViewById(R.id.export_all_btn);
        clrAllBtn = (Button) findViewById(R.id.clear_btn);
        shareBtn = (Button) findViewById(R.id.send_all_btn);
        exportAllBtn.setOnClickListener(this);
        clrAllBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        modelNameView = (TextView) findViewById(R.id.modelNmae);

        getCount = databaseHelper.getTotalOrderItemsCount();
        if (Integer.parseInt(getCount) > 0) {
            exportStatus = 1;//there is some data to export
        }


    }

    public String getFileData() {
        databaseHelper.openDB();
        String name = new String();
//        List<SelectedProductDetails> list=databaseHelper.selectSelectedProductDetailsWithoutName();
//        Iterator iterator=((ArrayList) list).iterator();
//        for(int i=0;i<list.size();i++){
//            name=list.get(1).getItemName();
//            name.concat(",");
//        }

        return name;
    }

    private void clearAll() {
//        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());


        pDialog = new ProgressDialog(ScreenExport.this);
        pDialog.setMessage("Please Wait!!!");
        pDialog.setCancelable(false);
        pDialog.show();

        new BackgroundTask(ScreenExport.this) {

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
                    Cursor curCSV1 = db1.rawQuery("SELECT *,"+"'"+userNumber+"'"+" as number FROM bills", null);
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
                            " b.tex_percentage ,b.qty ,b.descp , b.free_qty ,b.mrp , b.rate ,b.value ,b.discount, " +"'"+userNumber+"'"+
                            " as number from orders b join bills a on a.cust_code = b.cust_code ", null);
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

    private void exportDB() {

        pDialog = new ProgressDialog(ScreenExport.this);
        pDialog.setMessage("Please Wait!!!");
        pDialog.setCancelable(false);
        pDialog.show();

        new BackgroundTask(ScreenExport.this) {

            @Override
            public void doInBackground() {
                //put you background code
                //same like doingBackground
                //Background Thread

                DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
                File exportDir = new File(Environment.getExternalStorageDirectory(), "BMS");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }

                File file = new File(exportDir, "stockoutput.csv");
                File file1 = new File(exportDir, "bills.csv");
                File file2 = new File(exportDir, "orders.csv");
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


                    file1.createNewFile();
                    CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));
                    SQLiteDatabase db1 = dbhelper.getReadableDatabase();
                    Cursor curCSV1 = db1.rawQuery("SELECT *,"+"'"+userNumber+"'"+" as number FROM bills", null);
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


                    file2.createNewFile();
                    CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file2));
                    SQLiteDatabase db2 = dbhelper.getReadableDatabase();
//                    Cursor curCSV2 = db2.rawQuery("SELECT * FROM orders", null);
                    Cursor curCSV2 = db2.rawQuery("select a.id as bill_id,b.cust_code ,b.cust_name, " +
                            " b.manufacture_code ,b.manufacture_name ,b.product_code ,b.product_name ,b.date_time ,b.tax_code , " +
                            " b.tex_percentage ,b.qty ,b.descp , b.free_qty ,b.mrp , b.rate ,b.value ,b.discount ," +"'"+userNumber+"'"+
                            " as number from orders b join bills a on a.cust_code = b.cust_code ", null);
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
                } catch (Exception sqlEx) {
                    Log.e("ScreenExport : ", sqlEx.getMessage(), sqlEx);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
//                            toast.show();
                        }
                    });

                }
            }

            @Override
            public void onPostExecute() {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Exported Successfully", Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {

        view = v;

        int id = v.getId();
        switch (id) {
            case R.id.export_all_btn:
                if (exportStatus == 1) {
                    exportDB();
                } else {
                    Toast.makeText(getApplicationContext(), "No Saved Orders for Export", Toast.LENGTH_LONG).show();
                }

//              modelName=getModelNumber();
//                modelNameView.setText(getFileData());
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


            case R.id.send_all_btn:
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
////                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayUri);
//                intent.setType("text/plain");
//                startActivity(intent);
//                File pictures = new File(Environment.getExternalStorageDirectory(),"BMS");
//                String[] listOfPictures = pictures.list();
//                Uri uri=null;
//                ArrayList<Uri> arrayList = new ArrayList<>();
//                if (listOfPictures!=null) {
//                    for (String name : listOfPictures) {
//                        uri = Uri.parse("file://" + pictures.toString() + "/" + name);
//                        arrayList.add(uri);
//                    }
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                    intent.putExtra(Intent.EXTRA_STREAM, arrayList);
//                    //A content: URI holding a stream of data associated with the Intent, used with ACTION_SEND to supply the data being sent.
//                    intent.setType("text/*"); //any kind of images can support.
////                    chooser = Intent.createChooser(intent, "Send Multiple Images");//choosers title
//                    startActivity(Intent.createChooser(intent, "Send Multiple images"));
//                }

                File exportDir = new File(Environment.getExternalStorageDirectory(), "BMS/bills.csv");
                File exportDir1 = new File(Environment.getExternalStorageDirectory(), "BMS/orders.csv");
                File exportDir2 = new File(Environment.getExternalStorageDirectory(), "BMS/stockoutput.csv");
                ArrayList<Uri> arrayList = new ArrayList<>();
                for (int i = 0; i <= 2; i++) {
                    Uri uri = null;
                    if (i == 0) {
                        uri = FileProvider.getUriForFile(ScreenExport.this, BuildConfig.APPLICATION_ID + ".provider", exportDir);
                    }
                    if (i == 1) {
                        uri = FileProvider.getUriForFile(ScreenExport.this, BuildConfig.APPLICATION_ID + ".provider", exportDir1);
                    }
                    if (i == 2) {
                        uri = FileProvider.getUriForFile(ScreenExport.this, BuildConfig.APPLICATION_ID + ".provider", exportDir2);
                    }
                    arrayList.add(uri);
                }
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND_MULTIPLE);
                share.setType("text/csv");
                share.putExtra(Intent.EXTRA_STREAM, arrayList);
//                share.setPackage("com.whatsapp");
                startActivity(share);

                break;
        }
    }


    @Override
    public void onBackPressed() {


        Intent intent = new Intent(ScreenExport.this, HomeScreenActivity.class);
        intent.putExtra("username",userName);
        intent.putExtra("expiry_date", expiryDate);
        startActivity(intent);


    }

}
