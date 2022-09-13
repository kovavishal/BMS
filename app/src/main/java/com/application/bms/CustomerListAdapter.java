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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.application.bms.model.CustomerDetails;
import com.application.bms.model.SelectedCustomer;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<CustomerDetails> runSheetList;
    List<CustomerDetails> runSheetListDisplayed;
    int itPosition = 0;
    String userName;

    public TextView cus_Name, cus_Area, phoneNuber, cus_Balance;
//    public Button newOrder, pendingOrder;

    public CustomerListAdapter(Context context, List<CustomerDetails> runSheetList, String userName) {
        this.runSheetList = runSheetList;
        this.runSheetListDisplayed = runSheetList;
        this.userName = userName;
        this.context = context;
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


        convertView = mInflater.inflate(R.layout.customer_list_layout, null);

        cus_Name = convertView.findViewById(R.id.customerName);
        cus_Area = convertView.findViewById(R.id.address);
        cus_Balance = convertView.findViewById(R.id.balance);
        phoneNuber = convertView.findViewById(R.id.phonenumber);
//        newOrder = convertView.findViewById(R.id.button1);
//        pendingOrder = convertView.findViewById(R.id.button2);

//        newOrder.setTag(position);
//        pendingOrder.setTag(position);

        CustomerDetails runSheet = runSheetListDisplayed.get(position);



        cus_Name.setText(runSheet.getCustomerName());
        cus_Area.setText(runSheet.getAreaCode() + " , " + runSheet.getAddressLine1() + " , " + runSheet.getAddressLine2());
        cus_Balance.setText(runSheet.getBalance());
        phoneNuber.setText(runSheet.getPhoneNumber());


//        newOrder.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                int pos = (Integer) v.getTag();
//                CustomerDetails OrderClicked = runSheetListDisplayed.get(pos);
//                itPosition = pos;
//                Intent newOrderIntent = new Intent(context, NewProductActivity.class);
//                newOrderIntent.putExtra("name", OrderClicked.getCustomerName());
//                newOrderIntent.putExtra("customerCode", OrderClicked.getCustomerCode());
//                newOrderIntent.putExtra("userName", userName);
//                context.startActivity(newOrderIntent);
//
//            }
//        });


//        pendingOrder.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                int pos = (Integer) v.getTag();
//                CustomerDetails OrderClicked = runSheetListDisplayed.get(pos);
//                itPosition = pos;
//                Intent newOrderIntent = new Intent(context, PendingActivity.class);
//                newOrderIntent.putExtra("name", OrderClicked.getCustomerName());
//                newOrderIntent.putExtra("customerCode", OrderClicked.getCustomerCode());
//                context.startActivity(newOrderIntent);
//
//            }
//        });


        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            String data = "";

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                runSheetListDisplayed = (ArrayList<CustomerDetails>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CustomerDetails> FilteredArrList = new ArrayList<CustomerDetails>();

                if (runSheetList == null) {
                    runSheetList = new ArrayList<CustomerDetails>(runSheetListDisplayed); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = runSheetList.size();
                    results.values = runSheetList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < runSheetList.size(); i++) {
                        if (runSheetList.get(i).getCustomerName() != null) {
                            data = runSheetList.get(i).getCustomerName();
                        }
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new CustomerDetails(runSheetList.get(i).getCustomerCode(),
                                    runSheetList.get(i).getCustomerName(), runSheetList.get(i).getAreaCode(),
                                    runSheetList.get(i).getAddressLine1(), runSheetList.get(i).getAddressLine2(),
                                    runSheetList.get(i).getPhoneNumber(), runSheetList.get(i).getBalance()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

}
