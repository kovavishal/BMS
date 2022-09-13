package com.application.bms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProductActivity extends Activity implements View.OnClickListener {

    TextView name,address,phoneNumber,balance;
    String mAddreess,mCustomerName,mPhoneNumber,mBalance;
    Button newOrder,pendingOrder;
    View view;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        name=(TextView)findViewById(R.id.customerName);
        address=(TextView)findViewById(R.id.address);
        phoneNumber=(TextView)findViewById(R.id.phoneNumber);
        balance=(TextView)findViewById(R.id.balance);
        newOrder=(Button)findViewById(R.id.newOrder);
        pendingOrder=(Button)findViewById(R.id.pendingOrder);
        mCustomerName= getIntent().getStringExtra("name");
        mAddreess= getIntent().getStringExtra("address");
        mPhoneNumber= getIntent().getStringExtra("phoneNumber");
        mBalance= getIntent().getStringExtra("balance");
        name.setText(mCustomerName);
        address.setText("Address : "+mAddreess);
        phoneNumber.setText("PhoneNumber : "+mPhoneNumber);
        balance.setText("Balance : "+mBalance);
        newOrder.setOnClickListener( this);
        pendingOrder.setOnClickListener( this);
    }


    @Override
    public void onClick(View v) {
        view = v;

        int id = v.getId();
        switch (id) {
            case R.id.newOrder:
                Intent newOrderIntent=new Intent(this, NewProductActivity.class);
                newOrderIntent.putExtra("name", mCustomerName);
                newOrderIntent.putExtra("customerCode", getIntent().getStringExtra("customerCode"));
                startActivity(newOrderIntent);
                break;
            case R.id.pendingOrder:
                Intent pendingOrderIntent=new Intent(this, PendingActivity.class);
                pendingOrderIntent.putExtra("name", mCustomerName);
                pendingOrderIntent.putExtra("customerCode", getIntent().getStringExtra("customerCode"));
                startActivity(pendingOrderIntent);
                break;
        }
    }



}
