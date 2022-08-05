package microbi.organic.coworkuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import microbi.organic.coworkuser.R;

public class HomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //key 1-sharath123
    private ProgressDialog pDialog;
    private ListView lv;
    Button mSearch;
    EditText mdate;
    String finaldate;

    // URL to get contacts JSON
    PreferenceManager preferenceManager;
    Apis apis;
    String id;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferenceManager = new PreferenceManager(getApplicationContext());

        contactList = new ArrayList<>();
        apis = new Apis();



        FloatingActionButton fb =(FloatingActionButton) findViewById(R.id.floating);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this,MyBookingActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fprofile =(FloatingActionButton) findViewById(R.id.floatingprofile);
        fprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });


        mdate = (EditText) findViewById(R.id.date_id);
        mSearch = (Button) findViewById(R.id.search_id);

        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finaldate = mdate.getEditableText().toString();
                new GetContacts().execute();
            }
        });
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView venueid  =  view.findViewById(R.id._id_id);
        Toast.makeText(HomeActivity.this, venueid.getText().toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(HomeActivity.this,ViewFeaturesActivity.class);
        intent.putExtra("VenueId",venueid.getText().toString());
        startActivity(intent);
    }
});



    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apis.getVenueList()+finaldate);

            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);

                        String _id = c.getString("_id");
                        String name = c.getString("name");
                        String mobile_number = c.getString("mobile_number");
                        String address = c.getString("address");
                        String area = c.getString("area");
                        String city = c.getString("city");
                        String state = c.getString("state");
                        String Available = c.getString("available");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("_id", _id);
                        contact.put("name", name);
                        contact.put("mobile_number", mobile_number);
                        contact.put("address", address);
                        contact.put("area", area);
                        contact.put("city", city);
                        contact.put("state", state);
                        contact.put("Available", Available);

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
                    HomeActivity.this, contactList,
                    R.layout.list_venue_item, new String[]{"_id", "name",
                    "mobile_number","address","area","city","state","Available"}, new int[]{R.id._id_id,
                    R.id.name_id, R.id.mobile_number_id,R.id.address_id,R.id.area_id,R.id.city_id,R.id.state_id,R.id.available_id});

            lv.setAdapter(adapter);
        }



    }




    public void showDatePickerDialog(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        mdate.setText(date);
        mdate.setFocusable(false);
    }



}
