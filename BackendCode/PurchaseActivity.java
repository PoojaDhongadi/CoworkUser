package microbi.organic.coworkuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import microbi.organic.coworkuser.R;

public class PurchaseActivity extends AppCompatActivity {

    String VENUEID, FEATUREID,strJsonBody,TOTALAMOUNT,JARRAY,Array;
    EditText mdate,mpasstype,mtotalamount,mcardname,mcardnumber,mcardcvv,mcardexpiry;
    Button mConfirm;
    ProgressDialog progressDialog;
    String jsonResponse;
    String message,libuser_id;
    PreferenceManager preferenceManager;
    Apis apis;
    JSONArray jArray;
    JSONObject jResult;
    SeletedFeature selected;

     AdapterFish mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Intent intent = getIntent();
        VENUEID = intent.getStringExtra("VENUEID");
        FEATUREID = intent.getStringExtra("FEATUREID");
        TOTALAMOUNT = intent.getStringExtra("TOTALAMOUNT");

        apis = new Apis();
        preferenceManager = new PreferenceManager(getApplicationContext());
        System.out.println("purchase"+preferenceManager.getKeyAssetid("id"));

        mdate = (EditText) findViewById(R.id.date);
        mpasstype = (EditText) findViewById(R.id.passtype);
        mtotalamount = (EditText) findViewById(R.id.totalamount);
        mcardname = (EditText) findViewById(R.id.cardname);
        mcardnumber = (EditText) findViewById(R.id.cardnumber);
        mcardcvv = (EditText) findViewById(R.id.cardcvv);
        mcardexpiry = (EditText) findViewById(R.id.cardexpiry);
        mConfirm = (Button) findViewById(R.id.confirm);

        mtotalamount.setText(TOTALAMOUNT);
        System.out.println(JARRAY);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    new PurchaseAsyncTask().execute();
                }

            }
        });



    }

    private boolean validate(){
        if(mcardnumber.getText().toString().trim().equals(""))
            return false;
        else if(mtotalamount.getText().toString().trim().equals(""))
            return false;
            // else if(signup_confirm_password.getText().toString().trim().equals(""))
            //  return false;
        else
            return true;
    }


    private class PurchaseAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PurchaseActivity.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  for (int i = 0; i < dataArray.length(); i++) {
            try {


                URL url = new URL(apis.getPurchase());
                // URL url = new URL("http://192.168.1.6:8080/backup_api/calls/multicreate.php");
                // http://localhost/backup_api/calls/multicreate.php
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic NzY3OWM3MzctNTY3ZS00ZTI4LWE0MzctMzM5ODNhYjdkNmJk");
                con.setRequestMethod("POST");


                // Log.v("tag", "playerid array content is " + Arrays.toString(IDS));
                strJsonBody = "{"
                        + "\"venue_id\": \"" +VENUEID+ "\","
                        + "\"user_id\": \"" +preferenceManager.getKeyUserid("id")+ "\","
                        + "\"feature_id\": \"" +FEATUREID+ "\","
                        + "\"date\": \"" +mdate.getEditableText().toString()+ "\","
                        + "\"pass_type\": \"" +mpasstype.getEditableText().toString()+ "\","
                        + "\"total_amount\": \"" + mtotalamount.getEditableText().toString()+ "\","
                        + "\"card_name\": \"" + mcardname.getEditableText().toString()+ "\","
                        + "\"card_number\": \"" + mcardnumber.getEditableText().toString()+ "\","
                        + "\"card_cvv\": \"" + mcardcvv.getEditableText().toString()+ "\","
                        + "\"card_expiry\": \""+mcardexpiry.getEditableText().toString()+"\","
                        + "}";

                System.out.println("strJsonBody:\n" + strJsonBody);
                //d7cf76e1-cfb7-41ba-8687-53e1cdf59c50


                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);


                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                } else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);

                JSONObject jsonObject = new JSONObject(jsonResponse);
                Log.v("TAG","JSON OBJECT IS"+jsonObject);
                JSONObject jsonObjectstatus = jsonObject.getJSONObject("status");
                Log.v("TAG","jsonObjectstatus"+jsonObjectstatus);
                message = jsonObjectstatus.getString("message");

                JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
                JSONObject jsonObjectdetails = jsonObjectresult.getJSONObject("details");
                libuser_id =  jsonObjectdetails.getString("_id");
                Log.v("TAG","libuser_id"+libuser_id);
                preferenceManager.setKeyUserid(libuser_id);


            } catch (Throwable t) {
                t.printStackTrace();
            }
            // }
            return jsonResponse;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String jsonResponse) {
            // Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            Log.v("TAG","response is"+jsonResponse);
            progressDialog.dismiss();
            // String success = "success";
            if(jsonResponse==null){
                Toast.makeText(getBaseContext(), "Connection time out", Toast.LENGTH_LONG).show();
            }else if(message.equalsIgnoreCase("Success")){
                Intent intent = new Intent(PurchaseActivity.this,HomeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Purchase Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }


}