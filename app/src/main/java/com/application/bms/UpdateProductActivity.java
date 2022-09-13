package com.application.bms;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.SelectedProductDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateProductActivity extends Activity {


    //    TextView mProductName;
    //    TextView getTaxData, customerOrderedQtyET, getDiscountPersent, freeQtyET, rateET, getStockQty, getItems, getMrpDetails;
//    String itemName, getStock, getitemNos, getMrp, getTax, getDiscount, getTotalQuantity, getFreeQuantity, getRate, customerName, customerCode;
    String customerName, customerCode;
    Button updateCart;
    DatabaseHelper databaseHelper;
    String userName,expiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_product);
        databaseHelper = new DatabaseHelper(this);


        String productCode = getIntent().getStringExtra("productCode");
        String productName = getIntent().getStringExtra("productName");
        String brand_code = getIntent().getStringExtra("brand_code");
        String brand_name = getIntent().getStringExtra("brand_name");
        String getitemNos = getIntent().getStringExtra("item_nos");
        String getTax = getIntent().getStringExtra("tax_per");
        String getRate = getIntent().getStringExtra("rate");
        String getMrp = getIntent().getStringExtra("mrp");
        String getDiscount = getIntent().getStringExtra("discount");
        String taxCode = getIntent().getStringExtra("tax_code");
        customerName = getIntent().getStringExtra("customer_name");
        customerCode = getIntent().getStringExtra("customer_code");
        String freeQty = getIntent().getStringExtra("total_free_qty");
        //currentStock + customerOrderedQty = totalItemQty
        String customerOrderedQty = getIntent().getStringExtra("customerOrderedQty");
        String totalItemQty = getIntent().getStringExtra("stock");
//        String currentStock = getIntent().getStringExtra("currentStocks");
        userName =  getIntent().getStringExtra("userName");
        expiryDate =  getIntent().getStringExtra("expiry_date");


        TextView productNameTV = (TextView) findViewById(R.id.productName);
        TextView currentStockQtyTV = (TextView) findViewById(R.id.currentStockQty);
        TextView getItems = (TextView) findViewById(R.id.getItemNos);
        TextView getMrpDetails = (TextView) findViewById(R.id.getMrpDetails);
        TextView getTaxData = (TextView) findViewById(R.id.getTax);

        EditText getDiscountPersent = (EditText) findViewById(R.id.getDiscountPercentage);
        EditText customerOrderedQtyET = (EditText) findViewById(R.id.customerOrderedQuantity);
        EditText freeQtyET = (EditText) findViewById(R.id.getFreeQuantity);
        EditText rateET = (EditText) findViewById(R.id.getRate);

        String currentStock = databaseHelper.getCurrentStock(productCode);
        int availableQty = Integer.parseInt(customerOrderedQty) + Integer.parseInt(currentStock);

        productNameTV.setText(productName);
        currentStockQtyTV.setText(availableQty + "");
        getItems.setText(getitemNos);
        getMrpDetails.setText(getMrp);
        getTaxData.setText(getTax);
        getDiscountPersent.setText(getDiscount);
        customerOrderedQtyET.setText(customerOrderedQty);
        freeQtyET.setText(freeQty);
        rateET.setText(getRate);
        updateCart = (Button) findViewById(R.id.update_cart);

        updateCart.setOnClickListener(new View.OnClickListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                double Rate = Double.parseDouble(rateET.getEditableText().toString());
                String customerUpdatedQty = customerOrderedQtyET.getEditableText().toString();

                String mFreeQuantity = freeQtyET.getEditableText().toString();

                if (mFreeQuantity.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Free Quantity can't be Empty", Toast.LENGTH_LONG).show();
                }
                if (customerUpdatedQty.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bill Quantity can't be Empty", Toast.LENGTH_LONG).show();
                }
                if (customerUpdatedQty.equals("0") && mFreeQuantity.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please Check Bill Quantity and Free Quantity both are Zero", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("availableQty===" + availableQty);
                    System.out.println("customerUpdatedQty===" + Integer.parseInt(customerUpdatedQty));
                    if (availableQty < Integer.parseInt(customerUpdatedQty)) {
                        System.out.println("iam at out of stock");
                        Toast.makeText(getApplicationContext(), "Out of Stock", Toast.LENGTH_LONG).show();
                    } else {

                        final int Tot_Quantity = Integer.parseInt(customerUpdatedQty) + Integer.parseInt(mFreeQuantity);

                        if (Integer.parseInt(customerUpdatedQty) > Integer.parseInt(customerOrderedQty)) {
                            System.out.println("iam at reduce Stock");
// reduceStock
                            databaseHelper.updateIncreasedQuantity(availableQty, Tot_Quantity, productCode, getMrp);
                        }
                        if (Integer.parseInt(customerUpdatedQty) < Integer.parseInt(customerOrderedQty)) {
                            System.out.println("iam at increase Stock");
// reduceStock
                            databaseHelper.increaseQty(Tot_Quantity, productCode, getMrp);
                        }

                        System.out.println("customerUpdatedQty==" + Integer.parseInt(customerUpdatedQty));
                        System.out.println("Rate==" + Rate);
                        System.out.println("getDiscountPersent==" + new Double(getDiscountPersent.getText().toString()).intValue());

                        String mTotal = calculateTotalAmount(Integer.parseInt(customerUpdatedQty), Rate, new Double(getDiscountPersent.getText().toString()));

                        System.out.println("total====" + mTotal);
                        SelectedProductDetails selectedProductDetails = new SelectedProductDetails();
                        List details = new ArrayList<>();
                        selectedProductDetails.setProductCode(productCode);
                        selectedProductDetails.setProductName(productName);
                        selectedProductDetails.setTotalItemQty(totalItemQty);
                        selectedProductDetails.setItemNos(getitemNos);
                        selectedProductDetails.setItemDisc(getDiscountPersent.getEditableText().toString());
                        selectedProductDetails.setItemTaxPer(getTax);
                        selectedProductDetails.setItemTaxCode(taxCode);
                        selectedProductDetails.setItemMRP(getMrp);
                        selectedProductDetails.setmTotal(mTotal);
                        selectedProductDetails.setItemRate(String.valueOf(Rate));
//                        selectedProductDetails.setmTotalQuantity(updatedQty+"");
                        selectedProductDetails.setCustomerOrderedQty(customerUpdatedQty);
                        selectedProductDetails.setmFreeQuantity(mFreeQuantity);
                        selectedProductDetails.setmCustomerCode(customerCode);
                        selectedProductDetails.setmCustomerName(customerName);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            selectedProductDetails.setmDate(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
                        }
                        selectedProductDetails.setBrandCode(brand_code);
                        selectedProductDetails.setBrandName(brand_name);
                        details.add(selectedProductDetails);
                        databaseHelper.updateProductTable(details);
                        int savedStatus = databaseHelper.getcustomerCodeFromBills(customerCode);
                        System.out.println("customer saved status====="+savedStatus);
                        if(savedStatus == 0){
                            System.out.println("=======dont update==========");
                        }else{
                            System.out.println("========update=========");
                            String orderValue = databaseHelper.selectUpdatedOrderValue(customerCode);
                            int updatebills = databaseHelper.updateOrderAmountinBills(customerCode,orderValue);
                            System.out.println("updateOrderAmountinBills======"+updatebills);
                            databaseHelper.updateOrdersinOrder(details);

                        }


//                        databaseHelper.reduceQuantity(Integer.parseInt(mTotalQuantity),itemCode);
//                        Toast.makeText(getApplicationContext(),"Cart Updated Successfully",Toast.LENGTH_LONG);
                        callActivity();


                    }

                }

            }
        });

    }


//                    if (Integer.parseInt(customerUpdatedQty) > Integer.parseInt(customerOrderedQty)) {
//                        System.out.println("customerOrderedQty=="+Integer.parseInt(customerOrderedQty)+", customerUpdatedQty=="+Integer.parseInt(customerUpdatedQty));

//    int check = Integer.parseInt(customerUpdatedQty) - Integer.parseInt(customerOrderedQty);

//                            System.out.println("check==="+check +",currentStock=="+Integer.parseInt(currentStock));
//                        if(check <= Integer.parseInt(currentStock)){
//                            System.out.println("iam here");
//
////                            reduceStock
//                        databaseHelper.reduceQuantity(Integer.parseInt(customerUpdatedQty),productCode);
//
//                            System.out.println("customerUpdatedQty==" + Integer.parseInt(customerUpdatedQty));
//                            System.out.println("Rate==" + Rate);
//                            System.out.println("getDiscountPersent==" + new Double(getDiscountPersent.getText().toString()).intValue());
//
//                            String mTotal = calculateTotalAmount(Integer.parseInt(customerUpdatedQty), Rate, new Double(getDiscountPersent.getText().toString()).intValue());
//
//                            System.out.println("total====" + mTotal);
//                            SelectedProductDetails selectedProductDetails = new SelectedProductDetails();
//                            List details = new ArrayList<>();
//                            selectedProductDetails.setProductCode(productCode);
//                            selectedProductDetails.setProductName(productName);
//                            selectedProductDetails.setTotalItemQty(totalItemQty);
//                            selectedProductDetails.setItemNos(getitemNos);
//                            selectedProductDetails.setItemDisc(getDiscountPersent.getEditableText().toString());
//                            selectedProductDetails.setItemTaxPer(getTax);
//                            selectedProductDetails.setItemTaxCode(taxCode);
//                            selectedProductDetails.setItemMRP(getMrp);
//                            selectedProductDetails.setmTotal(mTotal);
//                            selectedProductDetails.setItemRate(String.valueOf(Rate));
////                        selectedProductDetails.setmTotalQuantity(updatedQty+"");
//                            selectedProductDetails.setCustomerOrderedQty(customerUpdatedQty);
//                            selectedProductDetails.setmFreeQuantity(mFreeQuantity);
//                            selectedProductDetails.setmCustomerCode(customerCode);
//                            selectedProductDetails.setmCustomerName(customerName);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                selectedProductDetails.setmDate(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
//                            }
//                            selectedProductDetails.setBrandCode(brand_code);
//                            selectedProductDetails.setBrandName(brand_name);
//                            details.add(selectedProductDetails);
//                            databaseHelper.updateProductTable(details);
////                        databaseHelper.reduceQuantity(Integer.parseInt(mTotalQuantity),itemCode);
////                        Toast.makeText(getApplicationContext(),"Cart Updated Successfully",Toast.LENGTH_LONG);
//                            callActivity();
//
//
//
//
//
//                        }else{
//                            System.out.println("iam here else");
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(), "Out of Stock", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                    } else if (Integer.parseInt(customerUpdatedQty) < Integer.parseInt(customerOrderedQty)) {
//
//                        System.out.println("customerOrderedQty=="+Integer.parseInt(customerOrderedQty)+", customerUpdatedQty=="+Integer.parseInt(customerUpdatedQty));
////                            increaseStock
//                        databaseHelper.increaseQty(Integer.parseInt(customerUpdatedQty),productCode);
////                            updatedQty = Integer.parseInt(getStock)+Integer.parseInt(mTotalQuantity);
//
//
//
//
//                        System.out.println("customerUpdatedQty==" + Integer.parseInt(customerUpdatedQty));
//                        System.out.println("Rate==" + Rate);
//                        System.out.println("getDiscountPersent==" + new Double(getDiscountPersent.getText().toString()).intValue());
//
//                        String mTotal = calculateTotalAmount(Integer.parseInt(customerUpdatedQty), Rate, new Double(getDiscountPersent.getText().toString()).intValue());
//
//                        System.out.println("total====" + mTotal);
//                        SelectedProductDetails selectedProductDetails = new SelectedProductDetails();
//                        List details = new ArrayList<>();
//                        selectedProductDetails.setProductCode(productCode);
//                        selectedProductDetails.setProductName(productName);
//                        selectedProductDetails.setTotalItemQty(totalItemQty);
//                        selectedProductDetails.setItemNos(getitemNos);
//                        selectedProductDetails.setItemDisc(getDiscountPersent.getEditableText().toString());
//                        selectedProductDetails.setItemTaxPer(getTax);
//                        selectedProductDetails.setItemTaxCode(taxCode);
//                        selectedProductDetails.setItemMRP(getMrp);
//                        selectedProductDetails.setmTotal(mTotal);
//                        selectedProductDetails.setItemRate(String.valueOf(Rate));
////                        selectedProductDetails.setmTotalQuantity(updatedQty+"");
//                        selectedProductDetails.setCustomerOrderedQty(customerUpdatedQty);
//                        selectedProductDetails.setmFreeQuantity(mFreeQuantity);
//                        selectedProductDetails.setmCustomerCode(customerCode);
//                        selectedProductDetails.setmCustomerName(customerName);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            selectedProductDetails.setmDate(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
//                        }
//                        selectedProductDetails.setBrandCode(brand_code);
//                        selectedProductDetails.setBrandName(brand_name);
//                        details.add(selectedProductDetails);
//                        databaseHelper.updateProductTable(details);
////                        databaseHelper.reduceQuantity(Integer.parseInt(mTotalQuantity),itemCode);
////                        Toast.makeText(getApplicationContext(),"Cart Updated Successfully",Toast.LENGTH_LONG);
//                        callActivity();
//
//
//
//
//                    }
//                        int updatedQty=0;


//            }
//        });
//
//    }

    public String calculateTotalAmount(int quantity, double rate, double disc) {
        double total = 0;
        double discout = (disc / 100.0f);
        total = quantity * (rate * (1 - discout));
        return String.format("%.2f", total);
    }

    public void callActivity() {

//        finish();

        Intent newOrderIntent = new Intent(this, NewProductActivity.class);
        newOrderIntent.putExtra("name", customerName);
        newOrderIntent.putExtra("customerCode", customerCode);
        newOrderIntent.putExtra("userName", userName);
        newOrderIntent.putExtra("expiry_date", expiryDate);
        startActivity(newOrderIntent);
    }

    @Override
    public void onBackPressed() {
        Intent newOrderIntent = new Intent(this, NewProductActivity.class);
        newOrderIntent.putExtra("name", customerName);
        newOrderIntent.putExtra("customerCode", customerCode);
        newOrderIntent.putExtra("userName", userName);
        newOrderIntent.putExtra("expiry_date", expiryDate);
        startActivity(newOrderIntent);

    }




}
