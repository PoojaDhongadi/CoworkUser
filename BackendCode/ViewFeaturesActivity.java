package microbi.organic.coworkuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import microbi.organic.coworkuser.R;

public class ViewFeaturesActivity extends AppCompatActivity {

    ListView mlv;
    Apis apis;
    String venueid;
    PreferenceManager preferenceManager;
    ArrayList<HashMap<String, String>> contactList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_features);

        Intent intent = getIntent();
        venueid = intent.getStringExtra("VenueId");
       System.out.println("venueidis" +venueid);

       apis = new Apis();
        contactList = new ArrayList<>();
       preferenceManager = new PreferenceManager(getApplicationContext());

       mlv = (ListView) findViewById(R.id.features_is);
       mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               TextView venueid = view.findViewById(R.id.venue_id);
               TextView fetureid = view.findViewById(R.id.feature_id);
               TextView DayPass = view.findViewById(R.id.day_pass);
               TextView MonthlyPass = view.findViewById(R.id.monthly_pass);
               TextView NAME = view.findViewById(R.id.name);

               Intent intents = new Intent(ViewFeaturesActivity.this,SubFeatureActivity.class);
               intents.putExtra("VENUEID",venueid.getText().toString());
               intents.putExtra("FEATUREID",fetureid.getText().toString());
               intents.putExtra("DAYPASS",DayPass.getText().toString());
               intents.putExtra("MONTHLYPASS",MonthlyPass.getText().toString());
               intents.putExtra("NAME",NAME.getText().toString());

               startActivity(intents);
           }
       });

        new GetFeaturesList().execute();

    }

    private class GetFeaturesList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ViewFeaturesActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apis.getFeatureList()+venueid);

            Log.e("TAG", "Response from url: " + apis.getFeatureList()+venueid);
            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        JSONObject purchase_cost = c.getJSONObject("purchase_cost");
                        String feature_id = c.getString("_id");

                        String name = c.getString("name");
                        String seats = c.getString("seats");
                        String day_pass = purchase_cost.getString("day_pass");
                        String monthly_pass = purchase_cost.getString("monthly_pass");
                        JSONObject venueJson = c.getJSONObject("venue_id");
                        String venue_id = venueJson.getString("_id");




                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("feature_id", feature_id);
                        contact.put("venue_id", venue_id);
                        contact.put("name", name);
                        contact.put("seats", seats);
                        contact.put("day_pass", day_pass);
                        contact.put("monthly_pass", monthly_pass);


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
                    ViewFeaturesActivity.this, contactList,
                    R.layout.list_feature_item, new String[]{"feature_id", "venue_id",
                    "name","seats","day_pass","monthly_pass"}, new int[]{R.id.feature_id,
                    R.id.venue_id, R.id.name,R.id.seats,R.id.day_pass,R.id.monthly_pass});

            mlv.setAdapter(adapter);
        }



    }





}