package com.application.bms;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.SelectedProductDetails;

import java.util.List;

public class PendingActivity extends Activity {

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    List<SelectedProductDetails> selectedProductDetails;
    public PendingActivityRecyclerViewAdapter adapter;
    String mName,customerCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prevoius_orders);
        recyclerView= (RecyclerView) findViewById(R.id.previous_order_listview);
        databaseHelper = new DatabaseHelper(this);
        mName=getIntent().getStringExtra("name");
        customerCode = getIntent().getStringExtra("customerCode");
        selectedProductDetails=databaseHelper.selectSelectedProductDetailsWithoutName(customerCode);
//        recyclerView = (RecyclerView) findViewById(R.id.new_product_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=  new PendingActivityRecyclerViewAdapter(this,selectedProductDetails);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);

    }

}
