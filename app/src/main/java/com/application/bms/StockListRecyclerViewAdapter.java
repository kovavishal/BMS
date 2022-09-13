package com.application.bms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.application.bms.model.CustomerDetails;
import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.StockDetails;

import java.util.ArrayList;
import java.util.List;

public class StockListRecyclerViewAdapter extends BaseAdapter implements Filterable {

    List<StockDetails> stockDetails;
    List<StockDetails> runSheetListDisplayed;
    Context context;
    DatabaseHelper helper;
    TextView mrpTV,rateTV,uomTV,stockNameTV;


    public StockListRecyclerViewAdapter(Context context, List<StockDetails> stockDetails) {
        this.stockDetails = stockDetails;
        this.runSheetListDisplayed = stockDetails;
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
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.stock_screen_adapter, null);

        mrpTV = convertView.findViewById(R.id.mrp);
        rateTV = convertView.findViewById(R.id.rate);
        uomTV = convertView.findViewById(R.id.uom);
        stockNameTV = convertView.findViewById(R.id.stockName);

        StockDetails runSheet = runSheetListDisplayed.get(position);

        mrpTV.setText("MRP :"+runSheet.getItemMRP());
        rateTV.setText("Rate :"+runSheet.getItemRate());
        uomTV.setText("Qty :"+runSheet.getItemStock());
        stockNameTV.setText(runSheet.getItemName());

        return convertView;
    }


//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//            String data = "";
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//
//                runSheetListDisplayed = (ArrayList<CustomerDetails>) results.values; // has the filtered values
//                notifyDataSetChanged();  // notifies the data with new filtered values
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                ArrayList<CustomerDetails> FilteredArrList = new ArrayList<CustomerDetails>();
//
//                if (runSheetList == null) {
//                    runSheetList = new ArrayList<CustomerDetails>(runSheetListDisplayed); // saves the original data in mOriginalValues
//                }
//
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = runSheetList.size();
//                    results.values = runSheetList;
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < runSheetList.size(); i++) {
//                        if (runSheetList.get(i).getCustomerName() != null) {
//                            data = runSheetList.get(i).getCustomerName();
//                        }
//                        if (data.toLowerCase().contains(constraint.toString())) {
//                            FilteredArrList.add(new CustomerDetails(runSheetList.get(i).getCustomerCode(),
//                                    runSheetList.get(i).getCustomerName(), runSheetList.get(i).getAreaCode(),
//                                    runSheetList.get(i).getAddressLine1(), runSheetList.get(i).getAddressLine2(),
//                                    runSheetList.get(i).getPhoneNumber(), runSheetList.get(i).getBalance()));
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//                }
//                return results;
//            }
//        };
//        return filter;
//    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            String data = "";

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                runSheetListDisplayed = (ArrayList<StockDetails>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<StockDetails> FilteredArrList = new ArrayList<StockDetails>();

                if (stockDetails == null) {
                    stockDetails = new ArrayList<StockDetails>(runSheetListDisplayed); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = stockDetails.size();
                    results.values = stockDetails;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < stockDetails.size(); i++) {
                        if (stockDetails.get(i).getItemName() != null) {
                            data = stockDetails.get(i).getItemName();
                        }
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new StockDetails(stockDetails.get(i).getItemCode(),
                                    stockDetails.get(i).getItemName(), stockDetails.get(i).getItemNos(),
                                    stockDetails.get(i).getItemTaxPer(), stockDetails.get(i).getItemTaxCode(),
                                    stockDetails.get(i).getItemMRP(),stockDetails.get(i).getItemRate(),
                                    stockDetails.get(i).getItemStock(), stockDetails.get(i).getItemGroupCode(),
                                    stockDetails.get(i).getItemSubGroup(), stockDetails.get(i).getItemDisc(),
                                    stockDetails.get(i).getBoxQty(), stockDetails.get(i).getCost()
                            ));
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



//public class StockListRecyclerViewAdapter extends RecyclerView.Adapter <StockListRecyclerViewAdapter.StocklistViewHolder>{
//
//    List<StockDetails> stockDetails;
//    Context context;
//    DatabaseHelper helper;
//    Intent intent;
//    public StockListRecyclerViewAdapter(Context context, List<StockDetails> stockDetails){
////        mContext=context;
//        this.stockDetails=stockDetails;
//        this.context=context;
//        helper = new DatabaseHelper(context);
//    }
//
//    @Override
//    public StockListRecyclerViewAdapter.StocklistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater=LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.stock_list_layout,parent,false);
//        return new StockListRecyclerViewAdapter.StocklistViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final StockListRecyclerViewAdapter.StocklistViewHolder holder, int position) {
//        final int value=position;
//        holder.productName.setText(stockDetails.get(position).getItemName());
//        holder.productMRP.setText(stockDetails.get(position).getItemMRP());
//        holder.productRate.setText(stockDetails.get(position).getItemRate());
//        holder.productStock.setText(stockDetails.get(position).getItemStock());
////        holder.itemView.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View v) {
//////                intent =  new Intent(context, ProductActivity.class);
//////                intent.putExtra("name",customerDetails.get(value).getCustomerName() );
//////                intent.putExtra("address",customerDetails.get(value).getAddressLine1()+", "+customerDetails.get(value).getAddressLine2() );
//////                intent.putExtra("phoneNumber",customerDetails.get(value).getPhoneNumber() );
//////                intent.putExtra("balance",customerDetails.get(value).getBalance() );
//////                context.startActivity(intent);
//////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return stockDetails.size();
//    }
//    public  class StocklistViewHolder extends RecyclerView.ViewHolder {
//        public TextView productName;
//        public TextView productMRP;
//        public TextView productRate;
//        public TextView productStock;
//        public StocklistViewHolder( View itemView) {
//            super(itemView);
//            productName=itemView.findViewById(R.id.item_name);
//            productMRP=itemView.findViewById(R.id.item_mrp);
//            productRate=itemView.findViewById(R.id.item_rate);
//            productStock=itemView.findViewById(R.id.item_stock);
//            itemView.setClickable(true);
//        }
//    }


//}
