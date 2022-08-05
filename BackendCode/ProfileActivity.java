package microbi.organic.coworkuser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import microbi.organic.coworkuser.R;

public class ProfileActivity extends AppCompatActivity {

    TextView mName, mMobile, mEmail;
    ProgressDialog pDialog;
    Apis apis;
    PreferenceManager preferenceManager;

    String name, mobile_number,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = (TextView) findViewById(R.id.name_id);
        mMobile = (TextView) findViewById(R.id.mobile_id);
        mEmail = (TextView) findViewById(R.id.email_id);

        preferenceManager = new PreferenceManager(getApplicationContext());
        apis = new Apis();
        new GetProfileList().execute();

    }

    private class GetProfileList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(apis.getMyProfile()+preferenceManager.getKeyUserid("id"));

            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONObject result = jsonObj.getJSONObject("result");

                    // looping through All Contacts


                        String _id = result.getString("_id");
                         name = result.getString("name");
                         mobile_number = result.getString("mobile_number");
                         email = result.getString("email");



                        // tmp hash map for single contact


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
            mName.setText(name);
            mEmail.setText(email);
            mMobile.setText(mobile_number);

            /**
             * Updating parsed JSON data into ListView
             * */

        }



    }




}