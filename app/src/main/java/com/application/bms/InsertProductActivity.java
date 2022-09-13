package com.application.bms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.application.bms.model.DatabaseHelper;
import com.application.bms.model.Orders;
import com.application.bms.model.SelectedProductDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertProductActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    TextView mProductName;
    TextView getTaxData,getStockQty,getItems,
            getMrpDetails,getcost,getBoxQty,totalOrderedQtyTV,totalOrderedAmountTV;
    String userName,itemName,getStock,getitemNos,getMrp,getTax,getDiscount,itemCode,groupCode,
            getRate,customerName,customerCode,taxCode,groupName,box_qty,cost,expiryDate;
    TextInputEditText getDiscountPersent,getPrice,getTotalQuantityData,getBoxQuantity,getFreeQuantityData;
    Button updateCart;
    DatabaseHelper databaseHelper;
    DrawerLayout drawer;
    List<Orders> ordersDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_product);
        databaseHelper = new DatabaseHelper(this);

        userName=getIntent().getStringExtra("userName");
        expiryDate =  getIntent().getStringExtra("expiry_date");
        itemCode=getIntent().getStringExtra("item_code");
        groupCode=getIntent().getStringExtra("group_code");
        taxCode=getIntent().getStringExtra("tax_code");
        groupName=getIntent().getStringExtra("group_name");
        itemName= getIntent().getStringExtra("name");
        getStock= getIntent().getStringExtra("stock");
        getitemNos= getIntent().getStringExtra("item_nos");
        getMrp= getIntent().getStringExtra("mrp");
        getTax= getIntent().getStringExtra("tax_per");
        getDiscount= getIntent().getStringExtra("discount");
        getRate= getIntent().getStringExtra("rate");
        customerName=getIntent().getStringExtra("customer_name");
        customerCode=getIntent().getStringExtra("customer_code");
        box_qty = getIntent().getStringExtra("box_qty");
        cost = getIntent().getStringExtra("cost");


        //side navigation code
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        final Button openNav = findViewById(R.id.openNav);
        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.userName);
        navName.setText(userName);
        TextView navReName = headerView.findViewById(R.id.navReName);
        navReName.setText("Executive");
        Button closeNav = headerView.findViewById(R.id.closeNav);
        Menu nav_Menu = navigationView.getMenu();
//        MenuItem nav_dashboard = nav_Menu.findItem(R.id.logout);
//        nav_dashboard.setVisible(true);
        openNav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("opening nav bar");
                        openDrawer();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(openNav.getWindowToken(), 0);
                    }
                });

        closeNav.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeDrawer(null);
                    }

                });
//side navigation code


        mProductName=(TextView)findViewById(R.id.getProductName) ;
        getStockQty=(TextView)findViewById(R.id.getStockQty);
        getItems=(TextView)findViewById(R.id.getItemNos);
        getMrpDetails=(TextView)findViewById(R.id.getMrpDetails);
        getTaxData=(TextView)findViewById(R.id.getTax);
        getTotalQuantityData=(TextInputEditText)findViewById(R.id.getTotalQuantity);
        getDiscountPersent=(TextInputEditText)findViewById(R.id.getDiscountPersent);
        getFreeQuantityData=(TextInputEditText)findViewById(R.id.getFreeQuantity);
        getPrice=(TextInputEditText)findViewById(R.id.getRate);
        getBoxQty = findViewById(R.id.getBoxQty);
        getcost = findViewById(R.id.getcost);
        getBoxQuantity =(TextInputEditText) findViewById(R.id.getBoxQuantity);
        totalOrderedQtyTV = findViewById(R.id.totalOrderedQty);
        totalOrderedAmountTV = findViewById(R.id.totalOrderedAmount);

//
//        getTotalQuantityData.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                calculation();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//











        mProductName.setText(itemName);
        getStockQty.setText(getStock);
        getItems.setText(getitemNos);
        getMrpDetails.setText(getMrp);
        getTaxData.setText(getTax);
        getDiscountPersent.setText(getDiscount);
        getFreeQuantityData.setText("0");
        getTotalQuantityData.setText("0");
        getBoxQuantity.setText("0");
        getPrice.setText(getRate);
        getBoxQty.setText(box_qty);
        getcost.setText(cost);

        updateCart=(Button)findViewById(R.id.update_cart);





        updateCart.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                double mPrice=Double.parseDouble(getPrice.getEditableText().toString());
                String mPicesQuantity=getTotalQuantityData.getEditableText().toString();
                String mFreeQuantity=getFreeQuantityData.getEditableText().toString();
                String mBoxQuantity=getBoxQuantity.getEditableText().toString();

                if(mFreeQuantity.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Free Quantity can't be Empty",Toast.LENGTH_LONG).show();
                }
                if(mPicesQuantity.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Bill Quantity can't be Empty",Toast.LENGTH_LONG).show();
                }
                if(mPicesQuantity.equals("0") && mFreeQuantity.equals("0") && mBoxQuantity.equals("0")){
                    Toast.makeText(getApplicationContext(),"Please Enter valid Quantity",Toast.LENGTH_LONG).show();
                }else{

                    double totalOrderedQty = Double.parseDouble(mPicesQuantity) + (Integer.parseInt(box_qty) * Double.parseDouble(mBoxQuantity) + Double.parseDouble(mFreeQuantity));
                    System.out.println("totalOrderedQty===="+totalOrderedQty);
                    double discountpercentace = new Double(getDiscountPersent.getText().toString());
                    String totalOrderedAmount = calculateTotalAmount(totalOrderedQty,mPrice,discountpercentace);
                    System.out.println("totalOrderedAmount===="+totalOrderedAmount);

//                    if(totalOrderedQty > Integer.parseInt(getStock)){
                    if(totalOrderedQty > Double.parseDouble(getStock)){
                        Toast.makeText(getApplicationContext()," out of stock",Toast.LENGTH_LONG).show();
                    }else {

                        SelectedProductDetails selectedProductDetails=new SelectedProductDetails();
                        List details = new ArrayList<>();
                        selectedProductDetails.setProductCode(itemCode);
                        selectedProductDetails.setProductName(itemName);
                        selectedProductDetails.setTotalItemQty(getStock);
                        selectedProductDetails.setItemNos(getitemNos);
                        selectedProductDetails.setItemDisc(getDiscountPersent.getEditableText().toString());
                        selectedProductDetails.setItemTaxPer(getTax);
                        selectedProductDetails.setItemTaxCode(taxCode);
                        selectedProductDetails.setItemMRP(getMrp);
                        selectedProductDetails.setmTotal(totalOrderedAmount);
                        selectedProductDetails.setItemRate(String.valueOf(mPrice));
                        selectedProductDetails.setCustomerOrderedQty(totalOrderedQty+"");
                        selectedProductDetails.setmFreeQuantity(mFreeQuantity);
                        selectedProductDetails.setmCustomerCode( customerCode);
                        selectedProductDetails.setmCustomerName(customerName);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            selectedProductDetails.setmDate(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
                        }
                        selectedProductDetails.setBrandCode(groupCode);
                        String getGroupName = databaseHelper.getGroupName(groupCode);
                        selectedProductDetails.setBrandName(getGroupName);
                        details.add(selectedProductDetails);
//select * from stock_table where item_group_code = "LORE"
                        databaseHelper.selectedProductTable(details);
                        databaseHelper.reduceQuantity(totalOrderedQty+Integer.parseInt(mFreeQuantity),itemCode,getMrp);

                        int savedStatus = databaseHelper.getcustomerCodeFromBills(customerCode);
                        System.out.println("customer saved status====="+savedStatus);

                        if(savedStatus == 0){
                            System.out.println("=======dont update==========");
                        }else{
                            System.out.println("========update=========");
                            String orderValue = databaseHelper.selectUpdatedOrderValue(customerCode);
                            int updatebills = databaseHelper.updateOrderAmountinBills(customerCode,orderValue);
                            System.out.println("updateOrderAmountinBills======"+updatebills);
//                            ordersDetails = databaseHelper.getOrderDeatils(customerCode);
                            databaseHelper.insertOrdersaftersave(details);
                        }



                        Toast.makeText(getApplicationContext(),"Cart Updated Successfully",Toast.LENGTH_LONG);
                        callActivity();
                    }
                }

            }
        });

    }

//    public void calculation(){
//
////        double finalPrice = Double.parseDouble(getPrice.getEditableText().toString());
//        String finalPicesQuantity = getTotalQuantityData.getEditableText().toString();
//        String finalFreeQuantity = getFreeQuantityData.getEditableText().toString();
//        String finalBoxQuantity = getBoxQuantity.getEditableText().toString();
//        int totalOrderedQty = Integer.parseInt(finalPicesQuantity) + (Integer.parseInt(box_qty) * Integer.parseInt(finalBoxQuantity)+Integer.parseInt(finalFreeQuantity));
//        int discountpercentace = new Double(getDiscountPersent.getText().toString()).intValue();
////        String totalOrderedAmount = calculateTotalAmount(totalOrderedQty,finalPrice,discountpercentace);
//        totalOrderedQtyTV.setText(totalOrderedQty+"");
////        totalOrderedAmountTV.setText(totalOrderedAmount);
//    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        closeDrawer(null);
        int id = menuItem.getItemId();
        if (id == R.id.logout) {
            Intent intent = new Intent(InsertProductActivity.this, CheckLoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.stockList) {
            Intent intent = new Intent(InsertProductActivity.this, ScreenStock.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }
        if (id == R.id.orderBooking) {
            Intent intent = new Intent(InsertProductActivity.this, BookingCustomerListActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }



        return true;
    }

    public void closeDrawer(DrawerLayout.DrawerListener listener) {
        drawer.setDrawerListener(listener);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        drawer.setDrawerListener(null);
        drawer.openDrawer(GravityCompat.START);
    }
    public String calculateTotalAmount(double quantity,double rate,double disc){
//        System.out.println("quantity in calucating total amount ==="+quantity);
//        System.out.println("rate in calucating total amount ==="+rate);
//        System.out.println("disc in calucating total amount ==="+disc);
        double total = 0;
        double discout=(disc/100.0f);
//        System.out.println("discount in calucating total amount ==="+discout);
//        System.out.println("rate and discout in calucating total amount ==="+rate * (1-discout));
        total=quantity * (rate * (1-discout));
        return String.format("%.2f",total);
    }
    public void callActivity(){

//        finish();

        Intent newOrderIntent=new Intent(this, SelectedProductActivity.class);
        newOrderIntent.putExtra("customerName", customerName);
        newOrderIntent.putExtra("customerCode", customerCode);
        newOrderIntent.putExtra("name", "ALLGROUPS");
        newOrderIntent.putExtra("userName", userName);
        newOrderIntent.putExtra("expiry_date", expiryDate);
        startActivity(newOrderIntent);
    }

    @Override
    public void onBackPressed() {
        Intent newOrderIntent=new Intent(this, SelectedProductActivity.class);
        newOrderIntent.putExtra("customerName", customerName);
        newOrderIntent.putExtra("customerCode", customerCode);
        newOrderIntent.putExtra("name", "ALLGROUPS");
        newOrderIntent.putExtra("userName", userName);
        newOrderIntent.putExtra("expiry_date", expiryDate);
        startActivity(newOrderIntent);
    }

}
