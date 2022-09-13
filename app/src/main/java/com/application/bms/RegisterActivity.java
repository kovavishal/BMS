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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.application.bms.model.DatabaseHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private static final String url = "jdbc:mysql://143.95.72.225:3306/smartbms_testdb?characterEncoding=latin1";
    private static final String user = "smartbms_test";
    private static final String pass = "password123";

    private ProgressDialog pDialog;
    EditText passwordET, phoneNumberET;
    Button registerButton;
    String phoneNumber, password, uniqueNumber, expiry_date, current_date;
    int insertStatus, saveInLocal;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    DatabaseHelper databaseHelper;
    int connectionStatus = 0;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        databaseHelper = new DatabaseHelper(this);

        phoneNumberET = findViewById(R.id.editText1);
        passwordET = findViewById(R.id.editText2);
        registerButton = findViewById(R.id.login);


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
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        current_date = dateFormat.format(new Date());
        System.out.println("current Date===" + current_date);
        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DATE, 365);
        c.add(Calendar.DATE, 30);
        expiry_date = dateFormat.format(c.getTime());
        System.out.println("expiry Date===" + expiry_date);


        registerButton.setOnClickListener(view -> {

            connectionStatus = getConnectionType(context);
            if (phoneNumberET.getText().length() != 0 && phoneNumberET.getText().toString() != "") {
                if (passwordET.getText().length() != 0 && passwordET.getText().toString() != "") {
                    if (connectionStatus > 0) {
                        if (phoneNumberET.getText().length() == 10) {
                            phoneNumber = phoneNumberET.getText().toString();
                            password = passwordET.getText().toString();
                            ConnectMySql connectMySql = new ConnectMySql();
                            connectMySql.execute();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check Your network connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password Can't be empty", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Mobile Number Can't be empty", Toast.LENGTH_LONG).show();
            }

        });


    }


    public class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
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
                String query = " insert into user_details (phone_number,password,device_id,create_on,expiry_on)"
                        + " values (?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setString(1, phoneNumber);
                preparedStmt.setString(2, password);
                preparedStmt.setString(3, uniqueNumber);
                preparedStmt.setString(4, current_date);
                preparedStmt.setString(5, expiry_date);
                insertStatus = preparedStmt.executeUpdate();
                con.close();


//                String result = "Database Connection Successful\n";
//                Statement st = con.createStatement();
//                ResultSet rs = st.executeQuery("insert into user_details (phone_number,password,device_id,create_on) values("+UserName+","+Password+","+userNumber+",now())");
//                ResultSetMetaData rsmd = rs.getMetaData();
//
//                while (rs.next()) {
//                    result += rs.getString(1).toString() + "\n";
//                }
//                res = result;
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
            if (insertStatus > 0) {
//                System.out.println("Record is inserted successfully !!!");
                saveInLocal = databaseHelper.saveRegisteredUser(phoneNumber, password, uniqueNumber, current_date, expiry_date);
                if (saveInLocal > 0) {
                    Toast.makeText(getApplicationContext(), "User Details Saved Successfully", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Registered Successfully..Please login to Continue", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, CheckLoginActivity.class);
                startActivity(intent);


            }
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
