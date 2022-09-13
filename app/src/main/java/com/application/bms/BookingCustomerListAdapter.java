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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.bms.model.CustomerDetails;
import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.SelectedCustomer;

import java.util.ArrayList;
import java.util.List;


public class BookingCustomerListAdapter extends BaseAdapter implements Filterable {

    List<CustomerDetails> runSheetList;
    List<CustomerDetails> runSheetListDisplayed;
    List<SelectedCustomer> selectedCustomers;
    Context context;
//    DatabaseHelper helper;
//    Intent intent;
    int itPosition = 0;
    String userName,expiryDate;

    public TextView cus_Name, cus_Area, phoneNuber, cus_Balance;
    public Button newOrder, pendingOrder;
    private LinearLayout linear_layout_1;

    public BookingCustomerListAdapter(Context context, List<CustomerDetails> runSheetList, List<SelectedCustomer> selectedCustomers,String userName,String expiryDate) {
        this.runSheetList = runSheetList;
        this.runSheetListDisplayed = runSheetList;
        this.selectedCustomers = selectedCustomers;
        this.userName = userName;
        this.expiryDate = expiryDate;
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


        convertView = mInflater.inflate(R.layout.booking_order_customer_list_layout, null);

        cus_Name = convertView.findViewById(R.id.customerName);
        cus_Area = convertView.findViewById(R.id.address);
        cus_Balance = convertView.findViewById(R.id.balance);
        phoneNuber = convertView.findViewById(R.id.phonenumber);
        newOrder = convertView.findViewById(R.id.button1);
        linear_layout_1 = convertView.findViewById(R.id.background);
//        pendingOrder = convertView.findViewById(R.id.button2);

        newOrder.setTag(position);
        linear_layout_1.setTag(position);
//        pendingOrder.setTag(position);

        CustomerDetails runSheet = runSheetListDisplayed.get(position);

        for (int i = 0; i < selectedCustomers.size(); i++) {
            if (selectedCustomers.get(i).getCustomerCode().equalsIgnoreCase(runSheet.getCustomerCode())) {
                cus_Name.setTextColor(Color.parseColor("#eb1c21"));
                phoneNuber.setTextColor(Color.parseColor("#eb1c21"));
                cus_Area.setTextColor(Color.parseColor("#eb1c21"));
                cus_Balance.setTextColor(Color.parseColor("#eb1c21"));
            }
        }

        cus_Name.setText(runSheet.getCustomerName());
        cus_Area.setText(runSheet.getAreaCode() + " , " + runSheet.getAddressLine1() + " , " + runSheet.getAddressLine2());
        cus_Balance.setText(runSheet.getBalance());
        phoneNuber.setText(runSheet.getPhoneNumber());


        newOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int pos = (Integer) v.getTag();
                CustomerDetails OrderClicked = runSheetListDisplayed.get(pos);
                itPosition = pos;
                Intent newOrderIntent = new Intent(context, NewProductActivity.class);
                newOrderIntent.putExtra("name", OrderClicked.getCustomerName());
                newOrderIntent.putExtra("customerCode", OrderClicked.getCustomerCode());
                newOrderIntent.putExtra("userName", userName);
                newOrderIntent.putExtra("expiry_date", expiryDate);

                context.startActivity(newOrderIntent);

            }
        });

        linear_layout_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int pos = (Integer) v.getTag();
                CustomerDetails OrderClicked = runSheetListDisplayed.get(pos);
                itPosition = pos;
                Intent newOrderIntent = new Intent(context, NewProductActivity.class);
                newOrderIntent.putExtra("name", OrderClicked.getCustomerName());
                newOrderIntent.putExtra("customerCode", OrderClicked.getCustomerCode());
                newOrderIntent.putExtra("userName", userName);
                newOrderIntent.putExtra("expiry_date", expiryDate);
                context.startActivity(newOrderIntent);

            }
        });


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


//public class BookingCustomerListAdapter extends RecyclerView.Adapter<BookingCustomerListAdapter.CustomerViewHolder> {
//
//
//    List<CustomerDetails> customerDetails;
//    List<SelectedCustomer> selectedCustomers;
//    Context context;
//    DatabaseHelper helper;
//    Intent intent;
//
//    public BookingCustomerListAdapter(Context context, List<CustomerDetails> customerDetails, List<SelectedCustomer> selectedCustomers) {
////        mContext=context;
//        this.customerDetails = customerDetails;
//        this.context = context;
//        helper = new DatabaseHelper(context);
//        this.selectedCustomers = selectedCustomers;
//    }
//
//    @Override
//    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View view = layoutInflater.inflate(R.layout.booking_order_customer_list_layout, parent, false);
//
//        return new CustomerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final BookingCustomerListAdapter.CustomerViewHolder holder, int position) {
//        final int value = position;
//
//
//        for (int i = 0; i < selectedCustomers.size(); i++) {
//
////            if (customerDetails.get(position).getCustomerCode().contains(selectedCustomers.get(i).getCustomerCode())) {
//            if (selectedCustomers.get(i).getCustomerCode().equalsIgnoreCase(customerDetails.get(position).getCustomerCode())) {
//                System.out.println("customerDetails.inside========" + customerDetails.get(position).getCustomerCode());
//
//                holder.cus_Name.setText(customerDetails.get(position).getCustomerName());
//                holder.phoneNuber.setText(customerDetails.get(position).getPhoneNumber());
//                holder.cus_Area.setText(customerDetails.get(position).getAreaCode() + " , " + customerDetails.get(position).getAddressLine1() + " , " + customerDetails.get(position).getAddressLine2());
//                holder.cus_Balance.setText(customerDetails.get(position).getBalance());
//
//                holder.cus_Name.setTextColor(Color.parseColor("#eb1c21"));
//                holder.phoneNuber.setTextColor(Color.parseColor("#eb1c21"));
//                holder.cus_Area.setTextColor(Color.parseColor("#eb1c21"));
//                holder.cus_Balance.setTextColor(Color.parseColor("#eb1c21"));
//
//
//            } else {
//                System.out.println("customerDetails.outside========" + customerDetails.get(position).getCustomerCode());
//
//                holder.cus_Name.setTextColor(Color.parseColor("#000000"));
//                holder.phoneNuber.setTextColor(Color.parseColor("#000000"));
//                holder.cus_Area.setTextColor(Color.parseColor("#000000"));
//                holder.cus_Balance.setTextColor(Color.parseColor("#000000"));
//
//                holder.cus_Name.setText(customerDetails.get(position).getCustomerName());
//                holder.phoneNuber.setText(customerDetails.get(position).getPhoneNumber());
//                holder.cus_Area.setText(customerDetails.get(position).getAreaCode() + " , " + customerDetails.get(position).getAddressLine1() + " , " + customerDetails.get(position).getAddressLine2());
//                holder.cus_Balance.setText(customerDetails.get(position).getBalance());
//            }
//
//
//        }
//
//
//
//
//
//        holder.newOrder.setOnClickListener(view -> {
//            Intent newOrderIntent = new Intent(context, NewProductActivity.class);
//            newOrderIntent.putExtra("name", customerDetails.get(value).getCustomerName());
//            newOrderIntent.putExtra("customerCode", customerDetails.get(value).getCustomerCode());
//            context.startActivity(newOrderIntent);
//        });
//
//        holder.pendingOrder.setOnClickListener(view -> {
//            Intent newOrderIntent = new Intent(context, PendingActivity.class);
//            newOrderIntent.putExtra("name", customerDetails.get(value).getCustomerName());
//            newOrderIntent.putExtra("customerCode", customerDetails.get(value).getCustomerCode());
//            context.startActivity(newOrderIntent);
//        });
////        holder.addressLine1.setText(customerDetails.get(position).getAddressLine1());
////        holder.addressLine2.setText(customerDetails.get(position).getAddressLine2());
////        holder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                intent =  new Intent(context, ProductActivity.class);
//////                ProductActivity productActivity=new ProductActivity();
//////                productActivity.getCusData(customerDetails.get(value).getCustomerName(),customerDetails.get(value).getAddressLine1(),customerDetails.get(value).getAddressLine2(),customerDetails.get(value).getPhoneNumber(),customerDetails.get(value).getBalance());
////                intent.putExtra("name",customerDetails.get(value).getCustomerName() );
////                intent.putExtra("address",customerDetails.get(value).getAddressLine1()+", "+customerDetails.get(value).getAddressLine2() );
////                intent.putExtra("phoneNumber",customerDetails.get(value).getPhoneNumber() );
////                intent.putExtra("balance",customerDetails.get(value).getBalance() );
////                intent.putExtra("customerCode",customerDetails.get(value).getCustomerCode() );
////                context.startActivity(intent);
////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return customerDetails.size();
//    }
//
//    public void filterList(ArrayList<CustomerDetails> filteredList) {
//        customerDetails = filteredList;
//        notifyDataSetChanged();
//    }
//
//    public class CustomerViewHolder extends RecyclerView.ViewHolder {
//        public TextView cus_Name;
//        public TextView cus_Area;
//        public TextView addressLine1;
//        public TextView addressLine2;
//        public TextView cus_Balance;
//        public TextView phoneNuber;
//        public Button newOrder, pendingOrder;
//
//        public CustomerViewHolder(View itemView) {
//            super(itemView);
//            cus_Name = itemView.findViewById(R.id.customerName);
//            cus_Area = itemView.findViewById(R.id.address);
////            addressLine1=itemView.findViewById(R.id.addressLine1);
////            addressLine2=itemView.findViewById(R.id.addressLine2);
//            cus_Balance = itemView.findViewById(R.id.balance);
//            phoneNuber = itemView.findViewById(R.id.phonenumber);
//            newOrder = itemView.findViewById(R.id.button1);
//            pendingOrder = itemView.findViewById(R.id.button2);
//            itemView.setClickable(false);
//        }
//    }
//
//
//}
