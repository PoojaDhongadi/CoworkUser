package microbi.organic.coworkuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import microbi.organic.coworkuser.R;

public class MyBookingActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    PreferenceManager preferenceManager;
    Apis apis;
    String id;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        preferenceManager = new PreferenceManager(getApplicationContext());
        lv = (ListView) findViewById(R.id.listid);


        contactList = new ArrayList<>();
        apis = new Apis();

        new ViewBookingFeatures().execute();

    }

    private class ViewBookingFeatures extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MyBookingActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apis.getMyPurchase()+preferenceManager.getKeyUserid("id"));
            //String jsonStr = sh.makeServiceCall(apis.getViewBookingFeatures()+"604dca811b13079b3d88acf4");

            Log.v("TAG", "Response from url: " + apis.getMyPurchase()+preferenceManager.getKeyUserid("id"));
            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);

                        JSONObject user_id = c.getJSONObject("user_id");
                        JSONObject feature_id = c.getJSONObject("feature_id");

                        String date = c.getString("date");
                        String pass_type = c.getString("pass_type");
                        String name = c.getString("card_name");
                        String total_amount = c.getString("total_amount");
                        String mobile_number = user_id.getString("mobile_number");
                        String email = user_id.getString("email");
                        String featurename = feature_id.getString("name");
                        // String email = feature_id.getString("email");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("date", date);
                        contact.put("pass_type", pass_type);
                        contact.put("name", name);
                        contact.put("total_amount", total_amount);
                        contact.put("mobile_number", mobile_number);
                        contact.put("email", email);
                        contact.put("featurename", featurename);

                        //"date","pass_type","name","total_amount"
                        // adding contact to contact list
                        contactList.add(contact);
                    }
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
            ListAdapter adapter = new SimpleAdapter(
                    MyBookingActivity.this, contactList,
                    R.layout.list_my_book_item, new String[]{"date","pass_type","name","total_amount","mobile_number","email","featurename"}, new int[]{R.id.date,
                    R.id.pass_type, R.id.card_name,R.id.total_amount,R.id.mobile_number,R.id.email,R.id.featurename});


            lv.setAdapter(adapter);
        }



    }


}