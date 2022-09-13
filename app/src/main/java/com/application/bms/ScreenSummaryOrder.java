package com.application.bms;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.application.bms.model.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ScreenSummaryOrder extends Activity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    BillSummaryAdapter adapter;
    String userName,expiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary_screen);


        userName =  getIntent().getStringExtra("userName");
        expiryDate =  getIntent().getStringExtra("expiry_date");


        ArrayList<String> mylist = new ArrayList<String>();
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        List list = databaseHelper.selectBillDetails();
        ListView listView = (ListView) findViewById(R.id.listCusName);
        //int value=databaseHelper.getCustomerCount();
//        TextView text=(TextView)findViewById(R.id.valueT);
//        text.setText("" + );
        adapter = new BillSummaryAdapter(this, list,userName,expiryDate);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScreenSummaryOrder.this, HomeScreenActivity.class);
        intent.putExtra("username",userName);
        intent.putExtra("expiry_date", expiryDate);
        startActivity(intent);
    }

}
