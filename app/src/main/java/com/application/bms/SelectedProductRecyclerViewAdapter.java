
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
import com.application.bms.model.SelectedStockDetails;
import com.application.bms.model.StockDetails;

import java.util.ArrayList;
import java.util.List;

public class SelectedProductRecyclerViewAdapter extends BaseAdapter implements Filterable {

    List<StockDetails> stockDetails ;
    List<StockDetails> runSheetListDisplayed;
    List<SelectedStockDetails> selectedStockDetails;
    Context context;
    //    DatabaseHelper helper;
//    Intent intent;
    String mCustomerCode, mCustomerName, mGroupName,userName,expiryDate;
    TextView itemNameTv, qtyTV, rateTV, mrpTV;
    Button addCart;
    int itPosition = 0;



    public SelectedProductRecyclerViewAdapter(Context context, List<StockDetails> stockDetails, String customerCode, String customerName, String groupName, List<SelectedStockDetails> selectedStockDetails,String userName,String expiryDate) {
        System.out.println("stockDetails size==="+stockDetails.size());
        this.stockDetails = stockDetails;
        this.runSheetListDisplayed = stockDetails;
        this.selectedStockDetails = selectedStockDetails;
        this.context = context;
        this.mCustomerCode = customerCode;
        this.mCustomerName = customerName;
        this.mGroupName = groupName;
        this.userName = userName;
        this.expiryDate = expiryDate;

    }


    @Override
    public int getCount() {
        return runSheetListDisplayed.size();
//        return stockSize;
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
        convertView = mInflater.inflate(R.layout.product_discription_layout, null);

        itemNameTv = convertView.findViewById(R.id.product_name);
        qtyTV = convertView.findViewById(R.id.product_qty);
        rateTV = convertView.findViewById(R.id.product_rate);
        mrpTV = convertView.findViewById(R.id.product_mrp);
        addCart = convertView.findViewById(R.id.button1);

        addCart.setTag(position);

        StockDetails runSheet = runSheetListDisplayed.get(position);


        for (int i = 0; i < selectedStockDetails.size(); i++) {
            if (selectedStockDetails.get(i).getItemCode().equalsIgnoreCase(runSheet.getItemCode()) &&
            selectedStockDetails.get(i).getMrp().equalsIgnoreCase(runSheet.getItemMRP())) {
                itemNameTv.setTextColor(Color.parseColor("#eb1c21"));
                qtyTV.setTextColor(Color.parseColor("#eb1c21"));
                rateTV.setTextColor(Color.parseColor("#eb1c21"));
                mrpTV.setTextColor(Color.parseColor("#eb1c21"));
            }
        }

        itemNameTv.setText(runSheet.getItemName());
        qtyTV.setText("Qty : " + runSheet.getItemStock());
        rateTV.setText("Rate : " + runSheet.getItemRate());
        mrpTV.setText("Mrp : " + runSheet.getItemMRP());

        addCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int pos = (Integer) v.getTag();
                StockDetails OrderClicked = runSheetListDisplayed.get(pos);
                itPosition = pos;
                Intent newOrderIntent = new Intent(context, InsertProductActivity.class);

                newOrderIntent.putExtra("name", OrderClicked.getItemName());
                newOrderIntent.putExtra("stock", OrderClicked.getItemStock());
                newOrderIntent.putExtra("item_nos", OrderClicked.getItemNos());
                newOrderIntent.putExtra("tax_per", OrderClicked.getItemTaxPer());
                newOrderIntent.putExtra("rate", OrderClicked.getItemRate());
                newOrderIntent.putExtra("mrp", OrderClicked.getItemMRP());
                newOrderIntent.putExtra("discount", OrderClicked.getItemDisc());
                newOrderIntent.putExtra("item_code", OrderClicked.getItemCode());
                newOrderIntent.putExtra("group_code", OrderClicked.getItemGroupCode());
                newOrderIntent.putExtra("tax_code", OrderClicked.getItemTaxCode());
                newOrderIntent.putExtra("box_qty", OrderClicked.getBoxQty());
                newOrderIntent.putExtra("cost", OrderClicked.getCost());
                newOrderIntent.putExtra("customer_code", mCustomerCode);
                newOrderIntent.putExtra("customer_name", mCustomerName);
                newOrderIntent.putExtra("group_name", mGroupName);
                newOrderIntent.putExtra("userName", userName);
                newOrderIntent.putExtra("expiry_date", expiryDate);
                newOrderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newOrderIntent);

            }
        });

        return convertView;
    }


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




//    @Override
//    public SelectedProductRecyclerViewAdapter.SelectedProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater=LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.product_discription_layout,parent,false);
//        return new SelectedProductRecyclerViewAdapter.SelectedProductViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final SelectedProductRecyclerViewAdapter.SelectedProductViewHolder holder, int position) {
//        final int value=position;
//
//        for (int i=0;i < selectedStockDetails.size();i++){
//            if(stockDetails.get(position).getItemCode().equals(selectedStockDetails.get(i).getItemCode())){
//            holder.productName.setTextColor(Color.parseColor("#eb1c21"));
//            holder.productMRP.setTextColor(Color.parseColor("#eb1c21"));
//            holder.productRate.setTextColor(Color.parseColor("#eb1c21"));
//            holder.productStock.setTextColor(Color.parseColor("#eb1c21"));
//            }
//        }
//
//
//
//
//
//        holder.productName.setText(stockDetails.get(position).getItemName());
//        holder.productMRP.setText("MRP :"+stockDetails.get(position).getItemStock());
//        holder.productRate.setText("Rate :"+stockDetails.get(position).getItemRate());
//        holder.productStock.setText("Qty : "+stockDetails.get(position).getItemMRP());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent =  new Intent(context, InsertProductActivity.class);
//                intent.putExtra("name",stockDetails.get(value).getItemName() );
//                intent.putExtra("stock",stockDetails.get(value).getItemMRP());
//                intent.putExtra("item_nos",stockDetails.get(value).getItemNos() );
//                intent.putExtra("tax_per",stockDetails.get(value).getItemTaxPer() );
//                intent.putExtra("rate",stockDetails.get(value).getItemRate() );
//                intent.putExtra("mrp",stockDetails.get(value).getItemStock() );
//                intent.putExtra("discount",stockDetails.get(value).getItemDisc() );
//                intent.putExtra("item_code",stockDetails.get(value).getItemCode());
//                intent.putExtra("group_code",stockDetails.get(value).getItemGroupCode());
//                intent.putExtra("tax_code",stockDetails.get(value).getItemTaxCode());
//                intent.putExtra("customer_code",mCustomerCode);
//                intent.putExtra("customer_name",mCustomerName);
//                intent.putExtra("group_name",mGroupName);
//                context.startActivity(intent);
//
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return stockDetails.size();
//    }
//
//    public void filterList(ArrayList<StockDetails> filteredList) {
//        stockDetails= filteredList;
//        notifyDataSetChanged();
//    }
//
//    public  class SelectedProductViewHolder extends RecyclerView.ViewHolder {
//        public TextView productName;
//        public TextView productMRP;
//        public TextView productRate;
//        public TextView productStock;
//        public SelectedProductViewHolder( View itemView) {
//            super(itemView);
//            productName=itemView.findViewById(R.id.product_name);
//            productMRP=itemView.findViewById(R.id.product_mrp);
//            productRate=itemView.findViewById(R.id.product_rate);
//            productStock=itemView.findViewById(R.id.product_qty);
//            itemView.setClickable(true);
//
//        }
//    }

//}


