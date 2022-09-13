package com.application.bms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.application.bms.model.DatabaseHelper;

import java.util.List;

public class BillSummaryDetails extends Activity {
    String userName,cust_id,expiryDate;
    DatabaseHelper databaseHelper;
    BillSummaryDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary_screen);



        userName =  getIntent().getStringExtra("userName");
        expiryDate =  getIntent().getStringExtra("expiry_date");

        cust_id =  getIntent().getStringExtra("cust_id");
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        List list = databaseHelper.getOrdersDetails(cust_id);
        ListView listView = (ListView) findViewById(R.id.listCusName);
        adapter = new BillSummaryDetailsAdapter(this, list,userName);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BillSummaryDetails.this, ScreenSummaryOrder.class);
        intent.putExtra("userName",userName);
        intent.putExtra("expiry_date", expiryDate);
        startActivity(intent);
    }
}
