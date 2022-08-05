package microbi.organic.coworkuser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import microbi.organic.coworkuser.R;

public class SubFeatureActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String VENUEID,FEATUREID,DAYPASS,MONTHLYPASS,NAME,strJsonBody;

    TextView mFeature,mDailyPass,mMonthlypass;
    String PassType;

    Button mConfirm;
    ProgressDialog progressDialog;
    String jsonResponse;
    String message,libuser_id;


    ListView mlv;
    Selected selected;
    Apis apis;
    String venueid;
    PreferenceManager preferenceManager;
    List<DataFish> contactList=new ArrayList<>();
    private ProgressDialog pDialog;
    private RecyclerView mRVFishPrice;
    private AdapterFish mAdapter;
    Button mPRoccedToBuy;
    JSONArray jArray;
    JSONObject jResult;
    int totalamount;



    EditText mdate,mpasstype,mtotalamount,mcardname,mcardnumber,mcardcvv,mcardexpiry;
  //  ArrayList<SeletedFeature> finalselection = new ArrayList<>();


    String[] country = { "PASS TYPE", "day", "monthly"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_feature);


        mFeature = (TextView) findViewById(R.id.featurename);
        mDailyPass = (TextView) findViewById(R.id.daypass_id);
        mMonthlypass = (TextView) findViewById(R.id.montlypass_id);
        mPRoccedToBuy = (Button) findViewById(R.id.proccedtoby);

       // mlv = (ListView) findViewById(R.id.list_sub);


        selected = new Selected();
        apis = new Apis();
        contactList = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        Spinner spin = (Spinner) findViewById(R.id.spinner_id);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        Intent intent = getIntent();
        VENUEID = intent.getStringExtra("VENUEID");
        FEATUREID = intent.getStringExtra("FEATUREID");
        DAYPASS = intent.getStringExtra("DAYPASS");
        MONTHLYPASS = intent.getStringExtra("MONTHLYPASS");
        NAME = intent.getStringExtra("NAME");


        mFeature.setText(NAME);
        mDailyPass.setText(DAYPASS);
        mMonthlypass.setText(MONTHLYPASS);

        System.out.println("DETAILS ARE"+VENUEID+FEATUREID+DAYPASS+MONTHLYPASS+NAME);

        new GetSubFeaturesList().execute();


        mPRoccedToBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ArrayList<SeletedFeature> newlist = mAdapter.getArrayList();

                System.out.println("clicked"+newlist.size());
                for (Object s : newlist){
                    Log.d("My array list content: ", String.valueOf(s));
                }
               // Gson gson=new Gson();
                for(int i=0;i<newlist.size();i++){
                    System.out.println(newlist.get(i).id);
                    System.out.println(newlist.get(i).quantity);
                    System.out.println(newlist.get(i).rate);
                    totalamount += Integer.parseInt(newlist.get(i).rate)*Integer.parseInt(newlist.get(i).quantity);
                }

                jArray = new JSONArray();
                jResult = new JSONObject();
                for (int i = 0; i < newlist.size(); i++) {
                    JSONObject jGroup = new JSONObject();// /sub Object

                    try {
                        jGroup.put("sub_feature_id", newlist.get(i).id);
                        jGroup.put("quantity", newlist.get(i).quantity);
                        jArray.put(jGroup);

                        // /itemDetail Name is JsonArray Name
                        jResult.put("features", jArray);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                System.out.println(jArray);
                preferenceManager.setKeyNomAssetid(jArray);
                totalamount = totalamount+Integer.parseInt(MONTHLYPASS);
                System.out.println(totalamount);

                AlerttoAddSubFeature(totalamount);

            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      // Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
        PassType = country[position];
       // selected.setPASSTYPE(country[position]);
        preferenceManager.setKeyStatus(country[position]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    void  AlerttoAddSubFeature(int value){

        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubFeatureActivity.this);
        alertDialog.setTitle("Payment");
        alertDialog.setView(promptsView);

         mdate = (EditText) promptsView.findViewById(R.id.date);
         mpasstype = (EditText) promptsView.findViewById(R.id.passtype);
         mtotalamount = (EditText) promptsView.findViewById(R.id.totalamount);
        mcardname = (EditText) promptsView.findViewById(R.id.cardname);
         mcardnumber = (EditText)promptsView.findViewById(R.id.cardnumber);
         mcardcvv = (EditText) promptsView.findViewById(R.id.cardcvv);
        mcardexpiry = (EditText) promptsView.findViewById(R.id.cardexpiry);

       mtotalamount.setText(String.valueOf(value));


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        new  PurchaseAsyncTask().execute();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }
    private class GetSubFeaturesList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SubFeatureActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apis.getSubFeature()+VENUEID);

            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {

                        DataFish fishData = new DataFish();
                        JSONObject c = result.getJSONObject(i);

                        // JSONObject user_id = c.getJSONObject("user_id");
                        JSONObject venue_id = c.getJSONObject("venue_id");

                        String subfeature = c.getString("name");
                        String day_price = c.getString("day_price");
                        String monthly_price = c.getString("monthly_price");
                        String Venuename = venue_id.getString("name");
                        String Sub_fea_id = c.getString("_id");

                        fishData.set_id(Sub_fea_id);
                        fishData.setName(subfeature);
                        fishData.setDaypass(day_price);
                        fishData.setMonthlypass(monthly_price);
                        // String email = feature_id.getString("email");
                        contactList.add(fishData);

                    }


                    //  mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);

                 //   mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("TAG", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
            mAdapter = new AdapterFish(SubFeatureActivity.this, contactList);
            mRVFishPrice.setAdapter(mAdapter);
            mRVFishPrice.setLayoutManager(new LinearLayoutManager(SubFeatureActivity.this));

        }



    }


    private class PurchaseAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SubFeatureActivity.this);
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
                        + "\"features\": "+jArray+","
                        + "\"date\": \"" +mdate.getEditableText().toString()+ "\","
                        + "\"pass_type\": \""+PassType+ "\","
                        + "\"total_amount\": \"" + mtotalamount.getEditableText().toString()+ "\","
                        + "\"card_name\": \"" + mcardname.getEditableText().toString()+ "\","
                        + "\"card_number\": \"" + mcardnumber.getEditableText().toString()+ "\","
                        + "\"card_cvv\": \"" + mcardcvv.getEditableText().toString()+ "\","
                        + "\"card_expiry\": \""+mcardexpiry.getEditableText().toString()+"\""
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
              //  JSONObject jsonObjectdetails = jsonObjectresult.getJSONObject("details");
//                libuser_id =  jsonObjectdetails.getString("_id");
//                Log.v("TAG","libuser_id"+libuser_id);
//                preferenceManager.setKeyUserid(libuser_id);


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
                Intent intent = new Intent(SubFeatureActivity.this,HomeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Purchase Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }




}