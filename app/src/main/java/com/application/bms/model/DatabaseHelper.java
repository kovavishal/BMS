package com.application.bms.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.application.bms.BillSummaryDetailsTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static DatabaseHelper databaseHelper;
    SQLiteDatabase sqliteDb;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private java.lang.Object Object;

    public DatabaseHelper(Context context) {
        super(context, "CSV.db", null, 1);
    }


    public static DatabaseHelper createInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public void openDB() {
        if (this.atomicInteger.incrementAndGet() == 1) {
            this.sqliteDb = getWritableDatabase();

        }
    }

    public void closeDB() {
        if (this.atomicInteger.decrementAndGet() == 0) {
            this.sqliteDb.close();
        }
    }

    //insert Group Data to database
    public void insertGroupDetails(String[] value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_code", value[0].trim());
        contentValues.put("group_name", value[1].trim());
        this.sqliteDb.insert("group_table", null, contentValues);
    }

    // retrieving Group Data from database
    public List selectGroupDetails() {
        ArrayList<String> arrayList = new ArrayList<String>();
        openDB();
        Cursor rawQuery = this.sqliteDb.rawQuery("select * from group_table", null);
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                String value = (String) rawQuery.getString(rawQuery.getColumnIndex("group_name"));
                GroupDetails groupDetails = new GroupDetails();
                groupDetails.setGroupCode(rawQuery.getString(rawQuery.getColumnIndex("group_code")));
                groupDetails.setGroupName(rawQuery.getString(rawQuery.getColumnIndex("group_name")));
                arrayList.add(value);
            }
            rawQuery.close();
        }
        return arrayList;
    }





    // Delete the Table values
    private void deleteTable(String str) {
        this.sqliteDb.delete(str, null, null);
    }

    //Getting Group data from UI
    public void insertGroupData(List list) {
        deleteTable("group_table");
        openDB();
        List list1 = new ArrayList(list);
        for (int i = 0; i < list1.size(); i++) {
            String[] value = (String[]) list1.get(i);
            insertGroupDetails(value);
        }
        closeDB();

    }

    // Geting Customer Data from UI
    public Boolean customerTable(List list) {
        deleteTable("customer_table");
        boolean result = false;
        int listSize = list.size();
        int Counter = 0;
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            insertCustomerDetails((CustomerDetails) it.next());
            Counter++;
        }
        if (listSize == Counter) {
            result = true;
        }
        return result;
    }

    // Inserting Customer data to database
    public void insertCustomerDetails(CustomerDetails customerDetails) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_code", customerDetails.getCustomerCode());
        contentValues.put("customer_name", customerDetails.getCustomerName());
        contentValues.put("customer_area", customerDetails.getAreaCode());
        contentValues.put("customer_address1", customerDetails.getAddressLine1());
        contentValues.put("customer_address2", customerDetails.getAddressLine2());
        contentValues.put("customer_balance", customerDetails.getBalance());
        contentValues.put("customer_phone", customerDetails.getPhoneNumber());
        this.sqliteDb.insert("customer_table", null, contentValues);

    }

    public void selectedProductTable(List list) {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            selectDetails((SelectedProductDetails) it.next());
        }

    }

    public void selectDetails(SelectedProductDetails selectedProductDetails) {
        openDB();
        ContentValues contentValues = new ContentValues();
        Log.d(TAG, "selectDetails: customer name ::::::::::::::" + selectedProductDetails.getmCustomerName());
        contentValues.put("product_code", selectedProductDetails.getProductCode());
        contentValues.put("product_name", selectedProductDetails.getProductName());
        contentValues.put("brand_code", selectedProductDetails.getBrandCode());
        contentValues.put("brand_name", selectedProductDetails.getBrandName());
        contentValues.put("total_qty", selectedProductDetails.getCustomerOrderedQty());
        contentValues.put("free_qty", selectedProductDetails.getmFreeQuantity());
        contentValues.put("qty", selectedProductDetails.getTotalItemQty());
        contentValues.put("item_nos", selectedProductDetails.getItemNos());
        contentValues.put("disc_perc", selectedProductDetails.getItemDisc());
        contentValues.put("tax", selectedProductDetails.getItemTaxPer());
        contentValues.put("tax_code", selectedProductDetails.getItemTaxCode());
        contentValues.put("rate", selectedProductDetails.getItemRate());
        contentValues.put("mrp", selectedProductDetails.getItemMRP());
        contentValues.put("total", selectedProductDetails.getmTotal());
        contentValues.put("customer_code", selectedProductDetails.getmCustomerCode());
        contentValues.put("customer_name", selectedProductDetails.getmCustomerName());
        contentValues.put("date_time", selectedProductDetails.getmDate());
        this.sqliteDb.insert("selected_product_table", null, contentValues);
        closeDB();
    }
//    select item_stock from stock_table where item_code = "BU05"

    //
    public void insertSelectedProductTable(SelectedProductDetails selectedProductDetails) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_code", selectedProductDetails.getProductCode());
        contentValues.put("product_name", selectedProductDetails.getProductName());
        contentValues.put("brand_code", selectedProductDetails.getBrandCode());
        contentValues.put("brand_name", selectedProductDetails.getBrandName());
        contentValues.put("total_qty", selectedProductDetails.getCustomerOrderedQty());
        contentValues.put("free_qty", selectedProductDetails.getmFreeQuantity());
        contentValues.put("qty", selectedProductDetails.getTotalItemQty());
        contentValues.put("item_nos", selectedProductDetails.getItemNos());
        contentValues.put("disc_perc", selectedProductDetails.getItemDisc());
        contentValues.put("tax", selectedProductDetails.getItemTaxPer());
        contentValues.put("tax_code", selectedProductDetails.getProductCode());
        contentValues.put("rate", selectedProductDetails.getItemRate());
        contentValues.put("mrp", selectedProductDetails.getItemMRP());
        contentValues.put("total", selectedProductDetails.getmTotal());
        contentValues.put("customer_code", selectedProductDetails.getmCustomerCode());
        contentValues.put("customer_name", selectedProductDetails.getmCustomerCode());
        contentValues.put("date_time", selectedProductDetails.getmDate());
        this.sqliteDb.insert("selected_product_table", null, contentValues);
        closeDB();
    }

    public List<SelectedProductDetails> selectSelectedProductDetails(String customerName) {
        openDB();
        ArrayList<SelectedProductDetails> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select product_code, product_name, brand_code, " +
                "total_qty, qty ,item_nos ,disc_perc ,tax,  tax_code, rate, mrp, total, customer_code, customer_name, date_time " +
                "from selected_product_table where product_name=?", new String[]{customerName});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new SelectedProductDetails(
                        rawQuery.getString(rawQuery.getColumnIndex("product_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("product_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("brand_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("brand_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("total_qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("free_qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_nos")),
                        rawQuery.getString(rawQuery.getColumnIndex("disc_perc")),
                        rawQuery.getString(rawQuery.getColumnIndex("tax")),
                        rawQuery.getString(rawQuery.getColumnIndex("tax_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("rate")),
                        rawQuery.getString(rawQuery.getColumnIndex("mrp")),
                        rawQuery.getString(rawQuery.getColumnIndex("total")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("date_time"))
                ));
            }
        }
        return details;
    }

    public List<SelectedProductDetails> selectSelectedProductDetailsWithoutName(String custCode) {
        openDB();
        ArrayList<SelectedProductDetails> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select product_code, product_name,total_qty,free_qty," +
                " qty ,item_nos ,disc_perc ,tax,  tax_code, rate, mrp, total, customer_code, customer_name, date_time," +
                "brand_code,brand_name" +
                " from selected_product_table where customer_code = ?  ", new String[]{custCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            System.out.println("rawQuery===" + rawQuery.getCount());
            while (rawQuery.moveToNext()) {
                System.out.println("rawQuery.getString(0)===" + rawQuery.getString(0));
                details.add(new SelectedProductDetails(
//                        rawQuery.getString(rawQuery.getColumnIndex("product_code")),
//                        rawQuery.getString(rawQuery.getColumnIndex("product_name")),
//                        rawQuery.getString(rawQuery.getColumnIndex("brand_code")),
//                        rawQuery.getString(rawQuery.getColumnIndex("brand_name")),
//                        rawQuery.getString(rawQuery.getColumnIndex("total_qty")),
//                        rawQuery.getString(rawQuery.getColumnIndex("free_qty")),
//                        rawQuery.getString(rawQuery.getColumnIndex("qty")),
//                        rawQuery.getString(rawQuery.getColumnIndex("item_nos")),
//                        rawQuery.getString(rawQuery.getColumnIndex("disc_perc")),
//                        rawQuery.getString(rawQuery.getColumnIndex("tax")),
//                        rawQuery.getString(rawQuery.getColumnIndex("tax_code")),
//                        rawQuery.getString(rawQuery.getColumnIndex("rate")),
//                        rawQuery.getString(rawQuery.getColumnIndex("mrp")),
//                        rawQuery.getString(rawQuery.getColumnIndex("total")),
//                        rawQuery.getString(rawQuery.getColumnIndex("customer_code")),
//                        rawQuery.getString(rawQuery.getColumnIndex("customer_name")),
//                        rawQuery.getString(rawQuery.getColumnIndex("date_time"))
                        rawQuery.getString(0),
                        rawQuery.getString(1),
                        rawQuery.getString(2),
                        rawQuery.getString(3),
                        rawQuery.getString(4),
                        rawQuery.getString(5),
                        rawQuery.getString(6),
                        rawQuery.getString(7),
                        rawQuery.getString(8),
                        rawQuery.getString(9),
                        rawQuery.getString(10),
                        rawQuery.getString(11),
                        rawQuery.getString(12),
                        rawQuery.getString(13),
                        rawQuery.getString(14),
                        rawQuery.getString(15),
                        rawQuery.getString(16)

                ));
            }
        }

//        Log.d(TAG, "selectSelectedProductDetailsWithoutName() returned: " + details.get(1).getmCustomerName());
//         Toast.makeText(,details.get(1).getmCustomerName(),Toast.LENGTH_LONG).show();

        for (int i = 0; i < details.size(); i++) {
//            Log.d(TAG, "customerName        :" + details.get(i).getmCustomerName());
//            Log.d(TAG, "customerCode        :" + details.get(i).getmCustomerCode());
//            Log.d(TAG, "ItemName            :" + details.get(i).getProductName());
//            Log.d(TAG, "itemCode           :" + details.get(i).getProductCode());
//            Log.d(TAG, "getItemGroupName        :" + details.get(i).getBrandName());
//            Log.d(TAG, "getItemGroupCode       :" + details.get(i).getBrandCode());
//            Log.d(TAG, "getmTotal            :" + details.get(i).getmTotal());
//            Log.d(TAG, "getItemMRP           :" + details.get(i).getItemMRP());
//            Log.d(TAG, "getItemRate          :" + details.get(i).getItemRate());
//            Log.d(TAG, "getItemStock          :" + details.get(i).getTotalItemQty());
//            Log.d(TAG, "getmTotalQuantity      :" + details.get(i).getCustomerOrderedQty());
//            Log.d(TAG, "getmFreeQuantity        :" + details.get(i).getmFreeQuantity());
//            Log.d(TAG, "getmDate           :" + details.get(i).getmDate());
//            Log.d(TAG, "tax           :" + details.get(i).getItemTaxPer());
//            Log.d(TAG, "tax code      :" + details.get(i).getItemTaxCode());
//            Log.d(TAG, "Discount           :" + details.get(i).getItemDisc());
//            Log.d(TAG, "qty           :" + details.get(i).getTotalItemQty());
        }

        return details;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table stock_table(item_code text, item_name text, item_nos text, item_tax_per text, item_tax_code text, item_mrp text,item_rate text, item_stock text, item_group_code text, item_sub_group text, item_disc text,box_qty,cost)");
        db.execSQL("create table group_table(group_code text, group_name text)");
        db.execSQL("create table customer_table(customer_code text, customer_name text,customer_area text, customer_address1 text, customer_address2 text,customer_balance text, customer_phone text)");
        db.execSQL("create table selected_product_table(id integer primary key autoincrement, product_code text, product_name text, brand_code text, brand_name text, total_qty text, free_qty text, qty text, item_nos text, disc_perc text,tax text, tax_code text, rate text, mrp text, total text,  customer_code text, customer_name text, date_time text)");
        db.execSQL("create table bills(id integer primary key autoincrement,cust_code text,cust_name text, order_value text,date_time text)");
        db.execSQL("create table orders(id integer primary key autoincrement,cust_code text,cust_name text," +
                " manufacture_code text,manufacture_name text,product_code text,product_name text,date_time text,tax_code text," +
                "tex_percentage text,qty text,descp text, free_qty text,mrp text, rate text,value text ,discount text)");
        db.execSQL("create table user_details(id integer primary key autoincrement,phone_number text,password text,device_id text,created_on text,expiry_on text)");
//        db.execSQL("create table login_table( user_name text, password text, client_name text);");
//        db.execSQL("create table user_table ( customer_name text, imei_no text PRIMARY KEY, expiry_date text, before_month text);");
    }

    // Deleting Tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS stock_table");
        db.execSQL("DROP TABLE IF EXISTS group_table");
        db.execSQL("DROP TABLE IF EXISTS customer_table");
        db.execSQL("DROP TABLE IF EXISTS selected_product_table");
        db.execSQL("DROP TABLE IF EXISTS bills");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS user_details");

//        db.execSQL("DROP TABLE IF EXISTS login_table");
//        db.execSQL("DROP TABLE IF EXISTS user_table");
        onCreate(db);
    }


    // Retrieving Customer Data from Database
    public List<CustomerDetails> getData() {
        openDB();
        ArrayList<CustomerDetails> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select customer_code, customer_name,customer_area,customer_address1,customer_address2,customer_balance,customer_phone from customer_table", null);
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new CustomerDetails(
                        rawQuery.getString(rawQuery.getColumnIndex("customer_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_area")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_address1")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_address2")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_phone")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_balance"))
                ));

            }
        }
        return details;
    }


    public List<SelectedCustomer> selectedCustomerDetails() {
        openDB();
        ArrayList<SelectedCustomer> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select distinct(customer_code) from selected_product_table", null);
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new SelectedCustomer(
                        rawQuery.getString(rawQuery.getColumnIndex("customer_code"))
                ));

            }
        }
        return details;
    }


    public Boolean stockTable(List list) {
        deleteTable("stock_table");
        boolean result = false;
        int listSize = list.size();
        int Counter = 0;
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            insertSDetails((StockDetails) it.next());
            Counter++;

        }
        if (listSize == Counter) {
            result = true;
        }
        return result;
    }

    public void insertSDetails(StockDetails stockDetails) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("item_code", stockDetails.getItemCode());
        contentValues.put("item_name", stockDetails.getItemName());
        contentValues.put("item_nos", stockDetails.getItemNos());
        contentValues.put("item_tax_per", stockDetails.getItemTaxPer());
        contentValues.put("item_tax_code", stockDetails.getItemTaxCode());
        contentValues.put("item_mrp", stockDetails.getItemMRP());
        contentValues.put("item_rate", stockDetails.getItemRate());
        contentValues.put("item_stock", stockDetails.getItemStock());
        contentValues.put("item_group_code", stockDetails.getItemGroupCode());
        contentValues.put("item_sub_group", stockDetails.getItemSubGroup());
        contentValues.put("item_disc", stockDetails.getItemDisc());
        contentValues.put("box_qty", stockDetails.getBoxQty());
        contentValues.put("cost", stockDetails.getCost());
        this.sqliteDb.insert("stock_table", null, contentValues);
    }

    public String getGroupCode(String groupName) {
        openDB();
        String group_code = new String();
//        group_code=groupCode;
        Cursor rawQuery = this.sqliteDb.rawQuery("SELECT group_code  FROM group_table WHERE group_name=?", new String[]{groupName});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                group_code = rawQuery.getString(rawQuery.getColumnIndex("group_code"));
            }
        }
        closeDB();
        return group_code;

    }


    public String getGroupName(String groupCode) {
        openDB();
        String group_code = new String();
//        group_code=groupCode;
        Cursor rawQuery = this.sqliteDb.rawQuery("SELECT group_code FROM group_table WHERE group_name=?", new String[]{groupCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                group_code = rawQuery.getString(rawQuery.getColumnIndex("group_code"));
            }
        }
        closeDB();
        return group_code;

    }

    public List<StockDetails> getStockData() {
        openDB();
        Cursor rawQuery;
        ArrayList<StockDetails> details = new ArrayList<>();
        rawQuery = this.sqliteDb.rawQuery("select item_code,item_name,item_nos, item_tax_per, item_tax_code , item_mrp,item_rate,item_stock,item_group_code, item_sub_group, item_disc,box_qty,cost from stock_table", null);
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new StockDetails(
                        rawQuery.getString(rawQuery.getColumnIndex("item_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_nos")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_tax_per")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_tax_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_mrp")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_rate")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_stock")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_group_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_sub_group")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_disc")),
                        rawQuery.getString(rawQuery.getColumnIndex("box_qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("cost"))
                ));

            }
        }
        return details;
    }

    public List<StockDetails> getStockGroupData(String groupCode) {
        openDB();
        Cursor rawQuery;
        ArrayList<StockDetails> details = new ArrayList<>();
        rawQuery = this.sqliteDb.rawQuery("select item_code,item_name,item_nos, item_tax_per, item_tax_code , item_mrp,item_rate,item_stock,item_group_code, item_sub_group, item_disc,box_qty,cost from stock_table WHERE item_group_code=?", new String[]{groupCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new StockDetails(
                        rawQuery.getString(rawQuery.getColumnIndex("item_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_nos")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_tax_per")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_tax_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_mrp")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_rate")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_stock")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_group_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_sub_group")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_disc")),
                        rawQuery.getString(rawQuery.getColumnIndex("box_qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("cost"))
                ));

            }
        }
        return details;
    }

    public List<SelectedStockDetails> getSelectedStockGroupData(String customerCode) {
        System.out.println("customerCode in db===" + customerCode);
        openDB();
        Cursor rawQuery;
        ArrayList<SelectedStockDetails> details = new ArrayList<>();
        rawQuery = this.sqliteDb.rawQuery("select product_code, mrp from selected_product_table WHERE customer_code=?", new String[]{customerCode});
//        System.out.println("rawQuery======"+rawQuery);
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new SelectedStockDetails(
                        rawQuery.getString(rawQuery.getColumnIndex("product_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("mrp"))
                ));

            }
        }
        return details;
    }


    public String getTotalOrderItemsCount() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select count(*) from selected_product_table;", null);
//       for(int i=0;i<rawQuery.getCount();i++) {
//           String name = rawQuery.getString(rawQuery.getColumnIndex("customer_name"));
//           list.add(name);
//           rawQuery.moveToNext();
//       }
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }


    public String getTotalBillsCount() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select count(*) from bills;", null);
//       for(int i=0;i<rawQuery.getCount();i++) {
//           String name = rawQuery.getString(rawQuery.getColumnIndex("customer_name"));
//           list.add(name);
//           rawQuery.moveToNext();
//       }
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }

    public String gettotalOrdersCount() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("SELECT COUNT(DISTINCT customer_code) FROM selected_product_table;", null);
//       for(int i=0;i<rawQuery.getCount();i++) {
//           String name = rawQuery.getString(rawQuery.getColumnIndex("customer_name"));
//           list.add(name);
//           rawQuery.moveToNext();
//       }
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }

    public String getTotalAmount() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select sum(total) from selected_product_table;", null);
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }

    public String getCustomerTotalAmount(String custCode) {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select sum(total) from selected_product_table where customer_code = ?", new String[]{custCode});
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }

    public String getCurrentStock(String productCode) {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select item_stock from stock_table where item_code = ?", new String[]{productCode});
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }

//    public void reduceQuantity(int quantity, String itemCode, String mrp) {
    public void reduceQuantity(double quantity, String itemCode, String mrp) {
        System.out.println("quantity in reduce == " + quantity + ", itemCode===" + itemCode);
        int item_stock = 0;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select item_stock from stock_table where item_code = ? and item_mrp = ? ;", new String[]{itemCode, mrp});
        if (rawQuery.moveToFirst()) {
            item_stock = rawQuery.getInt(0);
        }
        System.out.println("quantity in increase == " + quantity + ", item_stock===" + item_stock);
//        updateQuantity(item_stock, quantity, itemCode, mrp);
        updateQuantity(item_stock, quantity, itemCode, mrp);
//        return update;
    }

//    public void updateQuantity(int item_stock, int quantity, String itemCode, String mrp) {
    public void updateQuantity(int item_stock, double quantity, String itemCode, String mrp) {
//        int itemStock = 0;
        double itemStock = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        if (quantity > item_stock) {
            itemStock = quantity - item_stock;
        } else {
            itemStock = item_stock - quantity;
        }
        System.out.println("updated stock value=====" + itemStock);
        String updateQuery = " UPDATE stock_table SET item_stock = '" + itemStock + "'  WHERE item_code = '" + itemCode + "' and item_mrp = '" + mrp + "' ";
        Cursor c = db.rawQuery(updateQuery, null);
        c.moveToFirst();
        c.close();
//        int update = db.update("stock_table", data, "item_code" + " = "+itemCode , null);
//        return update;
    }

    public void updateIncreasedQuantity(int item_stock, int quantity, String itemCode, String mrp) {
        System.out.println("quantity in updateIncrease reduce == " + quantity + ", item_stock===" + item_stock);
        int itemStock = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        if (quantity > item_stock) {
            itemStock = quantity - item_stock;
        } else {
            itemStock = item_stock - quantity;
        }
        System.out.println("updated stock value=====" + itemStock);
//        String updateQuery =" UPDATE stock_table SET item_stock = '" + itemStock +"' "+" WHERE item_code = '" + itemCode + "' and item_mrp = '"+ mrp +"' ";
        String updateQuery = " UPDATE stock_table SET item_stock = '" + itemStock + "'  WHERE item_code = '" + itemCode + "' and item_mrp = '" + mrp + "' ";
        Cursor c = db.rawQuery(updateQuery, null);
        c.moveToFirst();
        c.close();
//        int update = db.update("stock_table", data, "item_code" + " = "+itemCode , null);
//        return update;
    }


    public void increaseQty(int quantity, String itemCode, String mrp) {
        System.out.println("quantity in increase == " + quantity + ", itemCode===" + itemCode);
        int item_stock = 0;
        openDB();
        sqliteDb = getReadableDatabase();
//        List list=new ArrayList();
        Cursor rawQuery = this.sqliteDb.rawQuery("select item_stock from stock_table where item_code = ? and item_mrp = ?;", new String[]{itemCode, mrp});
        if (rawQuery.moveToFirst()) {
            item_stock = rawQuery.getInt(0);
        }
        System.out.println("quantity in increase == " + quantity + ", item_stock===" + item_stock);
        increaceUpdateQuantity(item_stock, quantity, itemCode, mrp);
//        return update;
    }

    public void increaceUpdateQuantity(int item_stock, int quantity, String itemCode, String mrp) {
        SQLiteDatabase db = this.getWritableDatabase();
        int itemStock = item_stock + quantity;
        System.out.println("updated stock value=====" + itemStock);
//        String updateQuery = " UPDATE stock_table SET item_stock = '" + itemStock + "' " + " WHERE item_code = '" + itemCode + "' and item_mrp = '"+ mrp +"' ";
        String updateQuery = " UPDATE stock_table SET item_stock = '" + itemStock + "'  WHERE item_code = '" + itemCode + "' and item_mrp = '" + mrp + "' ";
        Cursor c = db.rawQuery(updateQuery, null);
        c.moveToFirst();
        c.close();
    }


    public boolean deleteAddedItem(String custCode, String productCode, String mrp) {
        System.out.println("custCode in delete db==" + custCode + "productCode in delete db===" + productCode);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("selected_product_table", "customer_code=? and product_code=? and mrp=?", new String[]{custCode, productCode, mrp}) > 0;
//        db.close();

    }

//    update stock_table set item_stock = item_stock- 1 where item_code = 'CA04'
//    select * from stock_table where item_code = 'CA04'


    public void updateProductTable(List list) {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            updateDetails((SelectedProductDetails) it.next());
        }

    }


    public void updateDetails(SelectedProductDetails selectedProductDetails) {
        openDB();
        ContentValues contentValues = new ContentValues();
        Log.d(TAG, "selectDetails: customer name ::::::::::::::" + selectedProductDetails.getmCustomerName());
        contentValues.put("product_code", selectedProductDetails.getProductCode());
        contentValues.put("product_name", selectedProductDetails.getProductName());
        contentValues.put("brand_code", selectedProductDetails.getBrandCode());
        contentValues.put("brand_name", selectedProductDetails.getBrandName());
        contentValues.put("total_qty", selectedProductDetails.getCustomerOrderedQty());
        contentValues.put("free_qty", selectedProductDetails.getmFreeQuantity());
        contentValues.put("qty", selectedProductDetails.getTotalItemQty());
        contentValues.put("item_nos", selectedProductDetails.getItemNos());
        contentValues.put("disc_perc", selectedProductDetails.getItemDisc());
        contentValues.put("tax", selectedProductDetails.getItemTaxPer());
        contentValues.put("tax_code", selectedProductDetails.getProductCode());
        contentValues.put("rate", selectedProductDetails.getItemRate());
        contentValues.put("mrp", selectedProductDetails.getItemMRP());
        contentValues.put("total", selectedProductDetails.getmTotal());
        contentValues.put("customer_code", selectedProductDetails.getmCustomerCode());
        contentValues.put("customer_name", selectedProductDetails.getmCustomerName());
        contentValues.put("date_time", selectedProductDetails.getmDate());
        this.sqliteDb.update("selected_product_table", contentValues, "product_code =? and customer_code = ? and mrp = ?", new String[]{selectedProductDetails.getProductCode(), selectedProductDetails.getmCustomerCode(), selectedProductDetails.getItemMRP() });
        closeDB();
    }


    public List<Bills> selectBillsDetails(String customerCode) {
        openDB();
        ArrayList<Bills> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select customer_name,customer_code,sum(total) as order_value from selected_product_table where customer_code = ? group by customer_code", new String[]{customerCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new Bills(
                        rawQuery.getString(rawQuery.getColumnIndex("customer_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("order_value"))
                ));

            }
        }
        return details;
    }

    public List<Orders> getOrderDeatils(String customerCode) {
        openDB();
        ArrayList<Orders> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select customer_name,customer_code,brand_code," +
                "brand_name,product_code,product_name,date_time," +
                "tax_code,tax,total_qty,item_nos,free_qty,mrp,rate,total,disc_perc " +
                "from selected_product_table where customer_code = ? ", new String[]{customerCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new Orders(
                        rawQuery.getString(rawQuery.getColumnIndex("customer_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("customer_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("brand_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("brand_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("product_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("product_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("date_time")),
                        rawQuery.getString(rawQuery.getColumnIndex("tax_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("tax")),
                        rawQuery.getString(rawQuery.getColumnIndex("total_qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("item_nos")),
                        rawQuery.getString(rawQuery.getColumnIndex("free_qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("mrp")),
                        rawQuery.getString(rawQuery.getColumnIndex("rate")),
                        rawQuery.getString(rawQuery.getColumnIndex("total")),
                        rawQuery.getString(rawQuery.getColumnIndex("disc_perc"))
                ));

            }
        }
        return details;
    }


    public void insertBills(List list, String currentTime) {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            insertBillDetails((Bills) it.next(), currentTime);
        }

    }

    public void insertBillDetails(Bills selectedProductDetails, String currentTime) {
        openDB();
        ContentValues contentValues = new ContentValues();
        Log.d(TAG, "billDetails: customer name ::::::::::::::" + selectedProductDetails.getCust_name());
        contentValues.put("cust_code", selectedProductDetails.getCust_code());
        contentValues.put("cust_name", selectedProductDetails.getCust_name());
        contentValues.put("order_value", selectedProductDetails.getOrder_value());
        contentValues.put("date_time", currentTime);
        this.sqliteDb.insert("bills", null, contentValues);
        closeDB();
    }

    public void insertOrders(List list) {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            insertOrdersDetails((Orders) it.next());
        }

    }

//    db.execSQL("create table orders(id integer primary key autoincrement,cust_code text,cust_name text," +
//            " manufacture_code text,manufacture_name text,product_code text,product_name text,date_time text,tax_code text," +
//            "tex_percentage text,qty text,descp text, free_qty text,mrp text, rate text,value text ,discount text)");

    public void insertOrdersDetails(Orders selectedProductDetails) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cust_code", selectedProductDetails.getCust_code());
        contentValues.put("cust_name", selectedProductDetails.getCust_name());
        contentValues.put("manufacture_code", selectedProductDetails.getManufacture_code());
        contentValues.put("manufacture_name", selectedProductDetails.getManufacture_name());
        contentValues.put("product_code", selectedProductDetails.getProduct_code());
        contentValues.put("product_name", selectedProductDetails.getProduct_name());
        contentValues.put("date_time", selectedProductDetails.getDate_time());
        contentValues.put("tax_code", selectedProductDetails.getTax_code());
        contentValues.put("tex_percentage", selectedProductDetails.getTax_percentage());
        contentValues.put("qty", selectedProductDetails.getQuantity());
        contentValues.put("descp", selectedProductDetails.getDes());
        contentValues.put("free_qty", selectedProductDetails.getFreeqty());
        contentValues.put("mrp", selectedProductDetails.getMrp());
        contentValues.put("rate", selectedProductDetails.getRate());
        contentValues.put("value", selectedProductDetails.getValue());
        contentValues.put("discount", selectedProductDetails.getDiscount());
        this.sqliteDb.insert("orders", null, contentValues);
        closeDB();
    }




    public void insertOrdersaftersave(List list) {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            insertOrdersDetailsAfterSave((SelectedProductDetails) it.next());
        }

    }

//    db.execSQL("create table orders(id integer primary key autoincrement,cust_code text,cust_name text," +
//            " manufacture_code text,manufacture_name text,product_code text,product_name text,date_time text,tax_code text," +
//            "tex_percentage text,qty text,descp text, free_qty text,mrp text, rate text,value text ,discount text)");

    public void insertOrdersDetailsAfterSave(SelectedProductDetails selectedProductDetails) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cust_code", selectedProductDetails.getmCustomerCode());
        contentValues.put("cust_name", selectedProductDetails.getmCustomerName());
        contentValues.put("manufacture_code", selectedProductDetails.getBrandCode());
        contentValues.put("manufacture_name", selectedProductDetails.getBrandName());
        contentValues.put("product_code", selectedProductDetails.getProductCode());
        contentValues.put("product_name", selectedProductDetails.getProductName());
        contentValues.put("date_time", selectedProductDetails.getmDate());
        contentValues.put("tax_code", selectedProductDetails.getItemTaxCode());
        contentValues.put("tex_percentage", selectedProductDetails.getItemTaxPer());
        contentValues.put("qty", selectedProductDetails.getCustomerOrderedQty());
        contentValues.put("descp", selectedProductDetails.getItemNos());
        contentValues.put("free_qty", selectedProductDetails.getmFreeQuantity());
        contentValues.put("mrp", selectedProductDetails.getItemMRP());
        contentValues.put("rate", selectedProductDetails.getItemRate());
        contentValues.put("value", selectedProductDetails.getmTotal());
        contentValues.put("discount", selectedProductDetails.getItemDisc());
        this.sqliteDb.insert("orders", null, contentValues);
        closeDB();
    }

//    db.execSQL("create table orders(id integer primary key autoincrement,cust_code text,cust_name text," +
//            " manufacture_code text,manufacture_name text,product_code text,product_name text,date_time text,tax_code text," +
//            "tex_percentage text,qty text,descp text, free_qty text,mrp text, rate text,value text ,discount text)");

    public void updateOrdersinOrder(List list) {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            updateOrderDetailsinOrder((SelectedProductDetails) it.next());
        }

    }

    public void updateOrderDetailsinOrder(SelectedProductDetails selectedProductDetails) {
        openDB();
        ContentValues contentValues = new ContentValues();
        Log.d(TAG, "selectDetails: customer name ::::::::::::::" + selectedProductDetails.getmCustomerName());
        contentValues.put("cust_code", selectedProductDetails.getmCustomerCode());
        contentValues.put("cust_name", selectedProductDetails.getmCustomerName());
        contentValues.put("manufacture_code", selectedProductDetails.getBrandCode());
        contentValues.put("manufacture_name", selectedProductDetails.getBrandName());
        contentValues.put("product_code", selectedProductDetails.getProductCode());
        contentValues.put("product_name", selectedProductDetails.getProductName());
        contentValues.put("date_time", selectedProductDetails.getmDate());
        contentValues.put("tax_code", selectedProductDetails.getItemTaxCode());
        contentValues.put("tex_percentage", selectedProductDetails.getItemTaxPer());
        contentValues.put("qty", selectedProductDetails.getCustomerOrderedQty());
        contentValues.put("descp", selectedProductDetails.getItemNos());
        contentValues.put("free_qty", selectedProductDetails.getmFreeQuantity());
        contentValues.put("mrp", selectedProductDetails.getItemMRP());
        contentValues.put("rate", selectedProductDetails.getItemRate());
        contentValues.put("value", selectedProductDetails.getmTotal());
        contentValues.put("discount", selectedProductDetails.getItemDisc());
        this.sqliteDb.update("orders", contentValues, "product_code =? and cust_code = ? and mrp = ?", new String[]{selectedProductDetails.getProductCode(), selectedProductDetails.getmCustomerCode(), selectedProductDetails.getItemMRP() });
        closeDB();
    }




    public int getcustomerCodeFromBills(String custCode) {
        int id = 0;


        openDB();
        Cursor rawQuery = this.sqliteDb.rawQuery("SELECT count(cust_code)as count FROM bills  WHERE cust_code =  ? ", new String[]{custCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
//                details.add(new Bills(
//                        rawQuery.getString(rawQuery.getColumnIndex("customer_code")),
//                        rawQuery.getString(rawQuery.getColumnIndex("customer_name")),
//                        rawQuery.getString(rawQuery.getColumnIndex("order_value"))
//                ));
                id = Integer.parseInt(rawQuery.getString(rawQuery.getColumnIndex("count")));

            }
        }
        return id;
    }

    public int updateOrderAmountinBills(String cust_code,String order_value){
        int update = 0;
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_value", order_value);
        update = this.sqliteDb.update("bills", contentValues, "cust_code =? ", new String[]{cust_code});
        closeDB();
        return  update;
    }


    public String selectUpdatedOrderValue(String custCode) {
        openDB();
        String orderValue = new String();
//        group_code=groupCode;
        Cursor rawQuery = this.sqliteDb.rawQuery("select sum(total) as order_value from selected_product_table where customer_code = ? ", new String[]{custCode});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                orderValue = rawQuery.getString(rawQuery.getColumnIndex("order_value"));
            }
        }
        closeDB();
        return orderValue;

    }

    public boolean deleteAddedItemFromOrder(String custCode, String productCode, String mrp) {
        System.out.println("custCode in delete db==" + custCode + "productCode in delete db===" + productCode);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("orders", "cust_code=? and product_code=? and mrp=?", new String[]{custCode, productCode, mrp}) > 0;
//        db.close();

    }


    public boolean deleteItemsFromCart(String custCode) {
        System.out.println("custCode in delete cart db==" + custCode );
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("selected_product_table", "customer_code=?", new String[]{custCode}) > 0;
//        db.close();

    }

    public boolean deleteItemsFromBills(String custCode) {
        System.out.println("custCode in delete bills db==" + custCode );
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("bills", "cust_code=? ", new String[]{custCode}) > 0;
//        db.close();

    }

    public boolean deleteItemsFromOrders(String custCode) {
        System.out.println("custCode in delete orders  db==" + custCode );
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("orders", "cust_code=? ", new String[]{custCode}) > 0;
//        db.close();

    }

    public void deleteAllItemsFromCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from selected_product_table");
        db.execSQL("update sqlite_sequence set seq=1 where name='selected_product_table'");
        db.close();

    }
    public void deleteAllItemsFromBills() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from bills");
        db.execSQL("update sqlite_sequence set seq=1 where name='bills'");
        db.close();
//        return db.delete("bills", "1",null) > 0;
    }
    public void deleteAllItemsFromOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from orders");
        db.execSQL("update sqlite_sequence set seq=1 where name='orders'");
        db.close();
//        return db.delete("orders", "1",null) > 0;
    }
    public void deleteAllItemsFromCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from customer_table");
        db.execSQL("update sqlite_sequence set seq=1 where name='customer_table'");
        db.close();
//        return db.delete("customer_table", "1",null) > 0;
    }
    public void deleteAllItemsFromGroup() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from group_table");
        db.execSQL("update sqlite_sequence set seq=1 where name='group_table'");
        db.close();
//        return db.delete("group_table", "1",null) > 0;
    }
    public void deleteAllItemsFromStocks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from stock_table");
        db.execSQL("update sqlite_sequence set seq=1 where name='stock_table'");
        db.close();
//        return db.delete("stock_table", "1",null) > 0;
    }


//    public List selectBillDetails() {
//        ArrayList<String> arrayList = new ArrayList<String>();
//        openDB();
//        Cursor rawQuery = this.sqliteDb.rawQuery("select * from bills", null);
//        if (rawQuery != null && rawQuery.getCount() > 0) {
//            while (rawQuery.moveToNext()) {
//                BillsSummary groupDetails = new BillsSummary();
//                groupDetails.setCustName(rawQuery.getString(rawQuery.getColumnIndex("cust_name")));
//                groupDetails.setOrderedValue(rawQuery.getString(rawQuery.getColumnIndex("order_value")));
//                arrayList.add(groupDetails);
//            }
//            rawQuery.close();
//        }
//        return arrayList;
//    }




    public List<BillsSummary> selectBillDetails() {
        openDB();
        ArrayList<BillsSummary> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select cust_code,cust_name,order_value " +
                "from bills ", null);
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new BillsSummary(
                        rawQuery.getString(rawQuery.getColumnIndex("cust_code")),
                        rawQuery.getString(rawQuery.getColumnIndex("cust_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("order_value"))
                ));

            }
        }
        return details;
    }



    public List<BillSummaryDetailsTO> getOrdersDetails(String cust_id) {
        openDB();
        ArrayList<BillSummaryDetailsTO> details = new ArrayList<>();
        Cursor rawQuery = this.sqliteDb.rawQuery("select product_name,qty,discount,rate,value " +
                "from orders where cust_code = ?", new String[]{cust_id});
        if (rawQuery != null && rawQuery.getCount() > 0) {
            while (rawQuery.moveToNext()) {
                details.add(new BillSummaryDetailsTO(
                        rawQuery.getString(rawQuery.getColumnIndex("product_name")),
                        rawQuery.getString(rawQuery.getColumnIndex("discount")),
                        rawQuery.getString(rawQuery.getColumnIndex("rate")),
                        rawQuery.getString(rawQuery.getColumnIndex("qty")),
                        rawQuery.getString(rawQuery.getColumnIndex("value"))
                ));

            }
        }
        return details;
    }


    public int saveRegisteredUser(String phone_number,String password,String unique_number,String created_date,String expiry_date){
        int insert = 0;
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone_number", phone_number);
        contentValues.put("password", password);
        contentValues.put("device_id", unique_number);
        contentValues.put("created_on", created_date);
        contentValues.put("expiry_on", expiry_date);
        insert =  (int) this.sqliteDb.insert("user_details", null, contentValues);
//        update = (int) this.sqliteDb.insert("user_details", contentValues, null);
        closeDB();
        return  insert;
    }

    public String getUserName() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
        Cursor rawQuery = this.sqliteDb.rawQuery("select phone_number from user_details", null);
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }
    public String getPassword() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
        Cursor rawQuery = this.sqliteDb.rawQuery("select password from user_details", null);
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }
    public String getDeviceId() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
        Cursor rawQuery = this.sqliteDb.rawQuery("select device_id from user_details", null);
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }
    public String getExpiryDate() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
        Cursor rawQuery = this.sqliteDb.rawQuery("select expiry_on from user_details", null);
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }
    public String getCreatedDate() {
        String value = null;
        openDB();
        sqliteDb = getReadableDatabase();
        Cursor rawQuery = this.sqliteDb.rawQuery("select created_on from user_details", null);
        if (rawQuery.moveToFirst())
            value = rawQuery.getString(0);
        return value;
    }

    public int updateExpiryDate(String expiry_date,String deiceId){
        int update = 0;
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("expiry_on", expiry_date);
        update = this.sqliteDb.update("user_details", contentValues, "device_id =? ", new String[]{deiceId});
        closeDB();
        return  update;
    }


















}


//    public List  selectCustomerDetails(){
//        List arrayList=new ArrayList();
//        openDB();
//        Cursor rawQuery = this.sqliteDb.rawQuery("select * from customer_table", null);
//
//        if(rawQuery != null && rawQuery.getCount()>0){
//            while (rawQuery.moveToNext()){
//                CustomerDetails customerDetails=new CustomerDetails();
//                customerDetails.setCustomerName(rawQuery.getString(rawQuery.getColumnIndex("customer_name")));
//                customerDetails.setCustomerCode(rawQuery.getString(rawQuery.getColumnIndex("customer_code")));
//                customerDetails.setAreaCode(rawQuery.getString(rawQuery.getColumnIndex("customer_area")));
//                customerDetails.setAddressLine1(rawQuery.getString(rawQuery.getColumnIndex("customer_address1")));
//                customerDetails.setAddressLine2(rawQuery.getString(rawQuery.getColumnIndex("customer_address2")));
//                customerDetails.setBalance(rawQuery.getString(rawQuery.getColumnIndex("customer_balance")));
//                customerDetails.setPhoneNumber(rawQuery.getString(rawQuery.getColumnIndex("customer_phone")));
//                arrayList.add(customerDetails);
//            }
//            rawQuery.close();
//        }
//        return arrayList;
//    }

//    public  void insertSelectedProductTable(String productCode, String productName, String totalQuantity, String freeQuantity, String quantity, String itemNos, String discPercent, String tax,
//                                            String taxCode, String rate, String mrp, String total, String customerName, String customerCode, String date, String groupCode, String groupName){
//        openDB();
//        ContentValues contentValues=new ContentValues();
//        contentValues.put("product_code",productCode);
//        contentValues.put("product_name",productName);
//        contentValues.put("brand_code",groupCode);
//        contentValues.put("brand_name",groupName);
//        contentValues.put("total_qty",totalQuantity);
//        contentValues.put("free_qty",freeQuantity);
//        contentValues.put("qty",quantity);
//        contentValues.put("item_nos",itemNos);
//        contentValues.put("disc_perc",discPercent);
//        contentValues.put("tax",tax);
//        contentValues.put("tax_code",taxCode);
//        contentValues.put("rate",rate);
//        contentValues.put("mrp",mrp);
//        contentValues.put("total",total);
//        contentValues.put("customer_code",customerCode);
//        contentValues.put("customer_name",customerName);
//        contentValues.put("date_time",date);
//        this.sqliteDb.insert("selected_product_table", null, contentValues);
//        closeDB();
//    }

//    public Integer getCustomerCount(){
//        openDB();
//        sqliteDb=getReadableDatabase();
////        List list=new ArrayList();
//        Cursor rawQuery = this.sqliteDb.rawQuery("select customer_name from customer_table", null);
////       for(int i=0;i<rawQuery.getCount();i++) {
////           String name = rawQuery.getString(rawQuery.getColumnIndex("customer_name"));
////           list.add(name);
////           rawQuery.moveToNext();
////       }
//        Integer value=rawQuery.getCount();
//        return value;
//    }
//    public List getCustomerNames(){
//
//        sqliteDb=getReadableDatabase();
//        ArrayList<String> arrayList=new ArrayList<String>();
//        openDB();
//        Cursor rawQuery = this.sqliteDb.rawQuery("select customer_name from customer_table", null);
//        if(rawQuery != null && rawQuery.getCount()>0) {
//            while (rawQuery.moveToNext()) {
//                String name = rawQuery.getString(rawQuery.getColumnIndex("customer_name"));
//                arrayList.add(name);
//            }
//            rawQuery.close();
//        }
//        return arrayList;
////        List list=new ArrayList();
////        Cursor rawQuery = this.sqliteDb.rawQuery("select customer_name from customer_table", null);
////       for(int i=0;i<rawQuery.getCount();i++) {
////           String name = rawQuery.getString(rawQuery.getColumnIndex("customer_name"));
////           list.add(name);
////           rawQuery.moveToNext();
////       }
////        Integer value=rawQuery.getCount();
//    }
//    public void insertStockData(List list) {
//        deleteTable("stock_table");
//        openDB();
//        List list1=new ArrayList(list);
//        for(int i=0;i<list1.size();i++){
//            String[] value= (String[]) list1.get(i);
//            insertStockDetails(value);
//        }
//        closeDB();
//
//    }
//    public void insertStockDetails(String[] value){
//        ContentValues contentValues=new ContentValues();
//
//        contentValues.put("item_code", value[0]);
//        contentValues.put("item_name",value[1] );
//        contentValues.put("item_nos", value[2]);
//        contentValues.put("item_tax_per",value[3] );
//        contentValues.put("item_tax_code", value[4]);
//        contentValues.put("item_mrp",value[5] );
//        contentValues.put("item_rate", value[6]);
//        contentValues.put("item_closing",value[7] );
//        contentValues.put("item_group_code", value[8]);
//        contentValues.put("item_sub_group",value[9] );
////        contentValues.put("item_disc",value[10] );
//        this.sqliteDb.insert("stock_table", null, contentValues);
//    }
//    public Cursor fetchCurser() {
//        openDB();
//
//        Cursor rawQuery = this.sqliteDb.rawQuery("select * from group_table", null);
//        if(rawQuery != null && rawQuery.getCount()>0){
//            rawQuery.moveToFirst();
//        }
//        closeDB();
//        return rawQuery;
//    }
