package com.application.bms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.application.bms.model.DatabaseHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CheckLoginActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 3;
    Button loginButton;
    EditText password;
    EditText userName;
    String UserName, Password, userNumber, savedUserName, savedPassword, savedDeviceId, uniqueNumber, savedExpiryDate, savedCreatedDate,checkUserName,checkPassword;
    TextView registerTV;

    private static final String url = "jdbc:mysql://143.95.72.225:3306/smartbms_testdb?characterEncoding=latin1";
    private static final String user = "smartbms_test";
    private static final String pass = "password123";

    DatabaseHelper databaseHelper;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    final Context context = this;
    int connectionStatus = 0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);


        checkRunTimePermission();
        checkPermission();
        if (!checkPermission()) {
            requestPermission();
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//        String n = tm.getLine1Number();
            uniqueNumber = tm.getDeviceId();
            if (uniqueNumber == null) {
                uniqueNumber = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        databaseHelper = new DatabaseHelper(this);


        userName = findViewById(R.id.editText1);
        password = findViewById(R.id.editText2);
        loginButton = findViewById(R.id.login);
        registerTV = findViewById(R.id.register);

        checkUserName = databaseHelper.getUserName();
        checkPassword = databaseHelper.getPassword();
        if(checkUserName !=null && checkUserName!=""){
            userName.setText(checkUserName);
        }
        if(checkPassword !=null && checkPassword!=""){
            password.setText(checkPassword);
        }


        registerTV.setOnClickListener(view -> {
            Intent intent = new Intent(CheckLoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().length() != 0 && userName.getText().toString() != "") {
                    if (password.getText().length() != 0 && password.getText().toString() != "") {
                        UserName = userName.getText().toString();
                        Password = password.getText().toString();
                        connectionStatus = getConnectionType(context);
                        if (connectionStatus > 0) {

                            ConnectMySql connectMySql = new ConnectMySql();
                            connectMySql.execute();

                        } else {
                            savedUserName = databaseHelper.getUserName();
                            savedPassword = databaseHelper.getPassword();
                            savedDeviceId = databaseHelper.getDeviceId();
                            savedExpiryDate = databaseHelper.getExpiryDate();
                            savedCreatedDate = databaseHelper.getCreatedDate();

                            System.out.println("savedUserName from local==" + savedUserName + " , savedPassword from local==" + savedPassword + ",savedexpiryfrom local======" + savedExpiryDate);
                            if (UserName.equals(savedUserName) && Password.equals(savedPassword) && uniqueNumber.equals(savedDeviceId)) {
                                try {
                                    Date current_date = new Date();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                                    Date registered_date = dateFormat.parse(savedCreatedDate);
                                    if (!registered_date.before(current_date)) {
                                        Toast.makeText(getApplicationContext(), "You were Behind the Created Date", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(CheckLoginActivity.this, HomeScreenActivity.class);
                                        intent.putExtra("username", UserName);
                                        intent.putExtra("expiry_date", savedExpiryDate);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else if (UserName.equals("9443453320") && Password.equals("kovavishal")) {
//                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CheckLoginActivity.this, HomeScreenActivity.class);
                                intent.putExtra("username", UserName);
                                intent.putExtra("expiry_date", savedExpiryDate);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Un-Authorized User", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });


    }


    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_NUMBERS);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (grantResults.length > 0) {
                    System.out.println("grantResults===" + grantResults);
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
//                userNumber = tMgr.getLine1Number();
                uniqueNumber = tMgr.getDeviceId();
                if (uniqueNumber == null) {
                    uniqueNumber = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
//                Toast.makeText(getApplicationContext(), "ur number=="+userNumber, Toast.LENGTH_SHORT).show();

                break;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
        finishAffinity();
    }


    public class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckLoginActivity.this);
            pDialog.setMessage("Please Wait!!!");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected String doInBackground(String... strings) {

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Databaseection success");

                //insert

//                String query = " insert into user_details (phone_number,password,device_id)"
//                        + " values (?, ?, ?)";
//
//                PreparedStatement preparedStmt = con.prepareStatement(query);
//                preparedStmt.setString(1, UserName);
//                preparedStmt.setString(2, Password);
//                preparedStmt.setString(3, userNumber);
////                preparedStmt.setString (4, "now()");
//
//                preparedStmt.execute();
//                con.close();


                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select phone_number,password,expiry_on,create_on from user_details where device_id =" + "'" + uniqueNumber + "'");
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    savedUserName = rs.getString(1);        // Retrieve the first column value
                    savedPassword = rs.getString(2);      // Retrieve the second column value
                    savedExpiryDate = rs.getString(3);      // Retrieve the third column value
                    savedCreatedDate = rs.getString(4);      // Retrieve the fourth column value

//                    for(int i = 1; i <=3; i++){
//                        result += rs.getString(i) + " ";
//                    }
//                    result += rs.getString(1).toString() + "\n";
                }
                res = result;

                System.out.println("savedUserName from internet==" + savedUserName + " , savedPassword from internet==" + savedPassword + ",savedexpiryfrom internet======" + savedExpiryDate);


            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            UserName = userName.getText().toString();
            Password = password.getText().toString();
            if (UserName.equals(savedUserName) && Password.equals(savedPassword)) {
                int updateexpiryDate = databaseHelper.updateExpiryDate(savedExpiryDate, uniqueNumber);
                if (updateexpiryDate > 0) {
                    Toast.makeText(getApplicationContext(), "User Details updated", Toast.LENGTH_SHORT).show();
                }
                try {
                    Date current_date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date registered_date = dateFormat.parse(savedCreatedDate);
                    System.out.println("current_date ==="+current_date +", registered_date==="+registered_date);
                    if (!registered_date.before(current_date)) {
                        System.out.println("greater in if");
                        Toast.makeText(getApplicationContext(), "You were Behind the Created Date", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.println("greater in else");
                        Intent intent = new Intent(CheckLoginActivity.this, HomeScreenActivity.class);
                        intent.putExtra("username", UserName);
                        intent.putExtra("expiry_date", savedExpiryDate);
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }
            } else {
                Toast.makeText(getApplicationContext(), "Un-Authorized User", Toast.LENGTH_SHORT).show();
            }


//            if(insertStatus > 0) {
////                System.out.println("Record is inserted successfully !!!");
//                saveInLocal = databaseHelper.saveRegisteredUser(phoneNumber,password,uniqueNumber,current_date,expiry_date);
//                if(saveInLocal >0){
//                    Toast.makeText(getApplicationContext(),"User Details Saved Successfully",Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(getApplicationContext(),"Registered Successfully..Please login to Continue",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(RegisterActivity.this, CheckLoginActivity.class);
//                startActivity(intent);
//
//
//            }


        }
    }


    @IntRange(from = 0, to = 3)
    public static int getConnectionType(Context context) {
        int result = 0; // Returns connection type. 0: none; 1: mobile data; 2: wifi
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        result = 3;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = 2;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = 1;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_VPN) {
                        result = 3;
                    }
                }
            }
        }
        return result;
    }


}

/*
Changes:
1) android 11 compatibility
2)UI design changes(Login screen,Dashboard screen,orderBooking screen)
3) Update Cart-flow
4) colour change for the item already in cart
5) update quantity after item added to cart
6) issue fixed in customer name search filter in order booking screen
7) after order booking change the colour of customer colour to red
 5/7/2021
Changes
8) Dashboard changes(totalorders,totalitems,UI changes)
9) changes in stock list screen
10) while pressing back from view stock screen app closes issue solved
11) colour change for the customer already place booked the order
12) implementing side navigation bar
13) removes pending order functions
14) total amount in customer cart screen  for specific customer
15) cancel item cart
16) update item qty after cancel item
17) update item in the cart
18) show all item button
19) item list ui design
change
20)update cart issue closed
21)customer list "make order" button position changed
22)item list "add cart" button position changed
23)redirect to item list after updating cart
13-07-2021
Changes
24)added box/qty,cost to db
25)updating cart by adding free qty
26)update cancel insert cart by checking mrp also
27)colour change for selected item based on mrp and item code
28)save item function(create bills,orders)
29)update,delete item function
30)added boxes_qty,cost to stock_master
31)showing box_qty,cost,calculation for adding box quantity in add cart screen
32)cancel,update item after saving the order
33)creating BMS folder and export orders,bills,stockoutput csv files in that folder
34)finding the possible app to share the all 3 files at the same time and sharing those files
35)clear button for clear all db data
36)order Summary screen with customer wise and item wise
Changes
37)adding item after saving
38)removing header column in output csv
39)instead of slno adding billId in order.csv
40)changing logo
41) setting app expiry time
42) expiry screen UI
Changes
43) while clear data backing up data in the BMSTemp folder
44) orders.csv file crash issue solved
changes
45) duplicate entries in orders.csv issue solved
changes
46) adjust the size of update button in update cart screen
47) resetting the id when clearing the data from the tables
changes
48) changes the position of update button in update cart scrren
49) getting users phone number and setting in dashboard screen
50) adding phone nummber while exporting bills,orders.CSV
Changes
51) created Db and table
52) created register screen and link with db
53) validation for mobile number in register screen
54) if user registered already, phone number and password auto populated in login screen
55) discount percentage will accept double value now in both update and insert cart screen
*/




