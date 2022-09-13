package com.application.bms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.SelectedProductDetails;

import java.util.List;


public class NewProductActivityProductRecyclerViewAdapter extends BaseAdapter {

    Context context;
    List<SelectedProductDetails> stockDetails;
    List<SelectedProductDetails> runSheetListDisplayed;
    DatabaseHelper databaseHelper;

    private NewProductActivityProductRecyclerViewAdapter adapter;

    public TextView item_NameTV, rateTV, qtyTV, discountTV, totalTV;
    Button cancel, update;
    int itPosition = 0;
    String custCode;
    String userName, expiryDate;


    public NewProductActivityProductRecyclerViewAdapter(Context context, List<SelectedProductDetails> stockDetails, String custCode,String userName,String expiryDate) {
        this.context = context;
        this.stockDetails = stockDetails;
        this.runSheetListDisplayed = stockDetails;
        this.custCode = custCode;
        this.userName = userName;
        this.expiryDate = expiryDate;
        this.adapter = this;
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


        convertView = mInflater.inflate(R.layout.list_selected_products, null);

        item_NameTV = convertView.findViewById(R.id.stockName);
        rateTV = convertView.findViewById(R.id.rate);
        qtyTV = convertView.findViewById(R.id.qty);
        discountTV = convertView.findViewById(R.id.discount);
        totalTV = convertView.findViewById(R.id.total);
        cancel = convertView.findViewById(R.id.button2);
        update = convertView.findViewById(R.id.button1);

        cancel.setTag(position);
        update.setTag(position);


        SelectedProductDetails runSheet = runSheetListDisplayed.get(position);
        item_NameTV.setText(runSheet.getProductName());
        rateTV.setText("Rate : " + runSheet.getItemRate());
        qtyTV.setText("Qty : " + runSheet.getCustomerOrderedQty());
        discountTV.setText("Dis % : " + runSheet.getItemDisc());
        totalTV.setText("Total: " + runSheet.getmTotal());


        cancel.setOnClickListener(v -> {
            int pos = (Integer) v.getTag();
            SelectedProductDetails orderClicked = runSheetListDisplayed.get(pos);
            itPosition = pos;
            String ProductCode = orderClicked.getProductCode();
            int qty = Integer.parseInt(orderClicked.getCustomerOrderedQty());
            String mrp = orderClicked.getItemMRP();
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle("Leave application?");
//        dialog.setMessage("Are you sure you want to leave the application?");
            dialog.setContentView(R.layout.custom_alert_dialouge);
            dialog.setCancelable(false);
            Button yesButton = (Button) dialog.findViewById(R.id.yes);
            Button noButton = (Button) dialog.findViewById(R.id.no);
            TextView title = (TextView) dialog.findViewById(R.id.title);
            TextView message = (TextView) dialog.findViewById(R.id.message);
            title.setText("Cancel Order");
            message.setText("Are you sure you want to cancel this Item?");

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper = new DatabaseHelper(context);
                    if (databaseHelper.deleteAddedItem(custCode, ProductCode,mrp)) {
                        databaseHelper.increaseQty(qty, ProductCode,mrp);


                        int savedStatus = databaseHelper.getcustomerCodeFromBills(custCode);
                        System.out.println("customer saved status====="+savedStatus);

                        if(savedStatus == 0){
                            System.out.println("=======dont Delete==========");
                        }else{
                            System.out.println("========Delete=========");
                            String orderValue = databaseHelper.selectUpdatedOrderValue(custCode);
                        int updatebills = databaseHelper.updateOrderAmountinBills(custCode,orderValue);
                        System.out.println("updateOrderAmountinBills======"+updatebills);
                            if (databaseHelper.deleteAddedItemFromOrder(custCode, ProductCode,mrp)) {
                                Toast.makeText(context, "Delete successful from Saved List.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(context, "Delete Failed from Saved List.", Toast.LENGTH_LONG).show();
                            }
                        }
                        Toast.makeText(context, "Delete successfully.", Toast.LENGTH_LONG).show();
                        runSheetListDisplayed.remove(itPosition);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(context, "Delete failed.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });




        update.setOnClickListener(v->{
            int pos = (Integer) v.getTag();
            SelectedProductDetails orderClicked = runSheetListDisplayed.get(pos);
            itPosition = pos;
            Intent intent =  new Intent(context, UpdateProductActivity.class);
            intent.putExtra("productName",orderClicked.getProductName() );
            intent.putExtra("productCode",orderClicked.getProductCode());
            intent.putExtra("brand_code",orderClicked.getBrandCode());
            intent.putExtra("brand_name",orderClicked.getBrandName());
//            intent.putExtra("stock",orderClicked.getItemStock());
            intent.putExtra("item_nos",orderClicked.getItemNos() );
            intent.putExtra("tax_per",orderClicked.getItemTaxPer() );
            intent.putExtra("rate",orderClicked.getItemRate() );
            intent.putExtra("mrp",orderClicked.getItemMRP() );
            intent.putExtra("discount",orderClicked.getItemDisc() );
            intent.putExtra("tax_code",orderClicked.getItemTaxCode());
            intent.putExtra("customer_code",orderClicked.getmCustomerCode());
            intent.putExtra("customer_name",orderClicked.getmCustomerName());
            intent.putExtra("total_free_qty",orderClicked.getmFreeQuantity());
            intent.putExtra("customerOrderedQty",orderClicked.getCustomerOrderedQty());
            intent.putExtra("stock",orderClicked.getTotalItemQty());
            intent.putExtra("userName", userName);
            intent.putExtra("expiry_date", expiryDate);

//            int stock  = Integer.parseInt(orderClicked.getTotalItemQty()) - Integer.parseInt(orderClicked.getCustomerOrderedQty());

//            intent.putExtra("currentStocks",String.valueOf(stock));
            context.startActivity(intent);
        });

        return convertView;
    }


}


//public class NewProductActivityProductRecyclerViewAdapter extends RecyclerView.Adapter <NewProductActivityProductRecyclerViewAdapter.NewProductActivityProductRecyclerViewHolder>{
//
//
//    List<SelectedProductDetails> stockDetails;
//    Context context;
//    DatabaseHelper helper;
//    Intent intent;
//    String mCustomerCode, mCustomerName,mGroupName;
//    public NewProductActivityProductRecyclerViewAdapter(Context context, List<SelectedProductDetails> stockDetails){
////        mContext=context;
//        this.stockDetails=stockDetails;
//        this.context=context;
//        helper = new DatabaseHelper(context);
//
//    }
//
//    @Override
//    public NewProductActivityProductRecyclerViewAdapter.NewProductActivityProductRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater=LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.list_selected_products,parent,false);
//        return new NewProductActivityProductRecyclerViewAdapter.NewProductActivityProductRecyclerViewHolder(view);
//    }
//
////    @Override
////    public void onBindViewHolder(@NonNull NewProductActivityProductRecyclerViewHolder holder, int position) {
////
////    }
//
//    @Override
//    public void onBindViewHolder(final NewProductActivityProductRecyclerViewAdapter.NewProductActivityProductRecyclerViewHolder holder, int position) {
////        final int value=position;
//        holder.itemNo.setText(String.valueOf(position+1));
//        holder.itemName.setText(getItemId(2)+stockDetails.get(position).getItemName()+"/");
//        holder.itemDescription.setText(" description");
//        holder.itemBrand.setText("brand:"+stockDetails.get(position).getItemGroupName());
//        holder.itemQTY.setText("qty"+stockDetails.get(position).getItemStock()+"/");
//        holder.itemMRP.setText("mrp"+stockDetails.get(position).getItemMRP()+"/");
//        holder.itemFree.setText("free"+stockDetails.get(position).getmFreeQuantity());
//        holder.itemRate.setText("rate"+stockDetails.get(position).getItemRate()+"/");
//        holder.itemTotal.setText("total"+stockDetails.get(position).getmTotal());
//
////        holder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                intent =  new Intent(context, InsertProductActivity.class);
////                intent.putExtra("name",stockDetails.get(value).getItemName() );
////                intent.putExtra("stock",stockDetails.get(value).getItemMRP());
////                intent.putExtra("item_nos",stockDetails.get(value).getItemNos() );
////                intent.putExtra("tax_per",stockDetails.get(value).getItemTaxPer() );
////                intent.putExtra("rate",stockDetails.get(value).getItemRate() );
////                intent.putExtra("mrp",stockDetails.get(value).getItemStock() );
////                intent.putExtra("discount",stockDetails.get(value).getItemDisc() );
////                intent.putExtra("item_code",stockDetails.get(value).getItemCode());
////                intent.putExtra("group_code",stockDetails.get(value).getItemGroupCode());
////                intent.putExtra("tax_code",stockDetails.get(value).getItemTaxCode());
////                intent.putExtra("customer_code",mCustomerCode);
////                intent.putExtra("customer_name",mCustomerName);
////                intent.putExtra("group_name",mGroupName);
////                context.startActivity(intent);
////
////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return stockDetails.size();
//    }
//
//    public void filterList(ArrayList<SelectedProductDetails> filteredList) {
//        stockDetails= filteredList;
//        notifyDataSetChanged();
//    }
//
//    public  class NewProductActivityProductRecyclerViewHolder extends RecyclerView.ViewHolder {
//        public TextView itemNo;
//        public TextView itemName;
//        public TextView itemDescription;
//        public TextView itemBrand;
//        public TextView itemQTY;
//        public TextView itemMRP;
//        public TextView itemFree;
//        public TextView itemTotal;
//        public TextView itemRate;
//        public NewProductActivityProductRecyclerViewHolder( View itemView) {
//            super(itemView);
//            itemNo=itemView.findViewById(R.id.no);
//            itemName=itemView.findViewById(R.id.item);
//            itemBrand=itemView.findViewById(R.id.brand);
//            itemDescription=itemView.findViewById(R.id.description);
//            itemQTY=itemView.findViewById(R.id.qty);
//            itemMRP=itemView.findViewById(R.id.mMrp);
//            itemFree=itemView.findViewById(R.id.free);
//            itemRate=itemView.findViewById(R.id.rate);
//            itemTotal=itemView.findViewById(R.id.mTotal);
//            itemView.setClickable(true);
//        }
//    }
//
//
//}
