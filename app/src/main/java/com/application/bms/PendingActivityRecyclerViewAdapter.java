package com.application.bms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.SelectedProductDetails;

import java.util.ArrayList;
import java.util.List;

public class PendingActivityRecyclerViewAdapter extends RecyclerView.Adapter<PendingActivityRecyclerViewAdapter.PendingViewHolder> {

    List<SelectedProductDetails> selectedProductDetails;
    Context context;
    DatabaseHelper databaseHelper;
    Intent intent;
    public PendingActivityRecyclerViewAdapter(Context context, List<SelectedProductDetails> selectedProductDetails){
//        mContext=context;
        this.selectedProductDetails=selectedProductDetails;
        this.context=context;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public PendingActivityRecyclerViewAdapter.PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_previous_order,parent,false);
        return new PendingActivityRecyclerViewAdapter.PendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PendingActivityRecyclerViewAdapter.PendingViewHolder holder, int position) {
        final int value=position;
        holder.user_Name.setText(selectedProductDetails.get(position).getmCustomerName()+">>>\t \ton");
        holder.item_details.setText(selectedProductDetails.get(position).getBrandName()+"\t"+"items,\twith \t " +
                "Total\t"+selectedProductDetails.get(position).getmTotal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent =  new Intent(context, NewProductActivity.class);
                NewProductActivity newProductActivity=new NewProductActivity();
//                newProductActivity.getCusData(selectedProductDetails.get(value).getmCustomerName(),selectedProductDetails.get(value).getAddressLine1(),selectedProductDetails.get(value).getAddressLine2(),selectedProductDetails.get(value).getPhoneNumber(),selectedProductDetails.get(value).getBalance());
                intent.putExtra("name",selectedProductDetails.get(value).getmCustomerName() );
//                intent.putExtra("address",customerDetails.get(value).getAddressLine1()+", "+customerDetails.get(value).getAddressLine2() );
//                intent.putExtra("phoneNumber",customerDetails.get(value).getPhoneNumber() );
//                intent.putExtra("balance",customerDetails.get(value).getBalance() );
//                intent.putExtra("customerCode",customerDetails.get(value).getCustomerCode() );
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return selectedProductDetails.size();
    }

    public void filterList(ArrayList<SelectedProductDetails> filteredList) {
        selectedProductDetails= filteredList;
        notifyDataSetChanged();
    }

    public  class PendingViewHolder extends RecyclerView.ViewHolder   {
        public TextView user_Name;
        public TextView item_details;

        public PendingViewHolder( View itemView) {
            super(itemView);
            user_Name=itemView.findViewById(R.id.userName);
            item_details=itemView.findViewById(R.id.item_details);

            itemView.setClickable(true);
        }


    }




}
