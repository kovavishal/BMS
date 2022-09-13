package com.application.bms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.application.bms.model.BillsSummary;
import com.application.bms.model.CustomerDetails;
import com.application.bms.model.SelectedCustomer;

import java.util.List;

public class BillSummaryAdapter extends BaseAdapter {

    List<BillsSummary> runSheetList;
    List<BillsSummary> runSheetListDisplayed;
    Context context;
    int itPosition = 0;
    String userName,expiryDate;

    public TextView cus_Name, order_value;
    Button next;

    public BillSummaryAdapter(Context context,List<BillsSummary> runSheetList,String userName,String expiryDate) {
        this.runSheetList = runSheetList;
        this.runSheetListDisplayed = runSheetList;
        this.context = context;
        this.userName = userName;
        this.expiryDate = expiryDate;
    }

    @Override
    public int getCount() {
        return runSheetListDisplayed.size();
    }

    @Override
    public Object getItem(int i) {
        return runSheetListDisplayed.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        convertView = mInflater.inflate(R.layout.bill_summay_adapter, null);

        cus_Name = convertView.findViewById(R.id.customerName);
        order_value = convertView.findViewById(R.id.balance);
        next = convertView.findViewById(R.id.button1);
//        pendingOrder = convertView.findViewById(R.id.button2);
        next.setTag(position);
//        pendingOrder.setTag(position);
        BillsSummary runSheet = runSheetListDisplayed.get(position);
        cus_Name.setText(runSheet.getCustName());
        order_value.setText(runSheet.getOrderedValue());
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                BillsSummary OrderClicked = runSheetListDisplayed.get(pos);
                itPosition = pos;
                Intent newOrderIntent = new Intent(context, BillSummaryDetails.class);
                newOrderIntent.putExtra("cust_id", OrderClicked.getCust_id());
                newOrderIntent.putExtra("userName", userName);
                newOrderIntent.putExtra("expiry_date", expiryDate);
                context.startActivity(newOrderIntent);
            }
        });
        return convertView;
    }



}
