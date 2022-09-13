package com.application.bms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.application.bms.model.BillsSummary;

import java.util.List;

public class BillSummaryDetailsAdapter extends BaseAdapter {
    List<BillSummaryDetailsTO> runSheetList;
    List<BillSummaryDetailsTO> runSheetListDisplayed;
    Context context;
    int itPosition = 0;
    String userName;

    public TextView item_NameTV, rateTV, qtyTV, discountTV, totalTV;
    Button next;

    public BillSummaryDetailsAdapter(Context context,List<BillSummaryDetailsTO> runSheetList, String userName) {
        this.runSheetList = runSheetList;
        this.runSheetListDisplayed = runSheetList;
        this.context = context;
        this.userName = userName;
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


        convertView = mInflater.inflate(R.layout.bill_summay_details_adapter, null);

        item_NameTV = convertView.findViewById(R.id.stockName);
        rateTV = convertView.findViewById(R.id.rate);
        qtyTV = convertView.findViewById(R.id.qty);
        discountTV = convertView.findViewById(R.id.discount);
        totalTV = convertView.findViewById(R.id.total);
//        pendingOrder.setTag(position);
        BillSummaryDetailsTO runSheet = runSheetListDisplayed.get(position);
        item_NameTV.setText(runSheet.getItemName());
        rateTV.setText("Rate :"+runSheet.getItemRate());
        qtyTV.setText("qty :"+runSheet.getOrderedQty());
        discountTV.setText("dis% :"+runSheet.getItemDiscount());
        totalTV.setText(runSheet.getTotal());

        return convertView;
    }
}
