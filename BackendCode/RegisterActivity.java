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

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import microbi.organic.coworkuser.R;

public class RegisterActivity extends AppCompatActivity {

    EditText mMobile, mPassword,mUserName,mEmail,mOrganization;
    Button mRegister;

    String strJsonBody;
    Apis apis;
    String jsonResponse;
    ProgressDialog progressDialog;
    String message,libuser_id;
    PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mMobile = (EditText) findViewById(R.id.register_mobile);
        mPassword = (EditText) findViewById(R.id.register_pass);
        mEmail = (EditText) findViewById(R.id.register_email);
        mUserName = (EditText) findViewById(R.id.register_name);
        mRegister = (Button) findViewById(R.id.register);
        mOrganization = (EditText) findViewById(R.id.organization_name);

        apis = new Apis();
        preferenceManager = new PreferenceManager(getApplicationContext());

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    new RegisterAsyncTask().execute();
                }


            }
        });


    }

    private boolean validate(){
        if(mMobile.getText().toString().trim().equals(""))
            return false;
        else if(mPassword.getText().toString().trim().equals(""))
            return false;
            // else if(signup_confirm_password.getText().toString().trim().equals(""))
            //  return false;
        else
            return true;
    }



    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //  for (int i = 0; i < dataArray.length(); i++) {
            try {


                URL url = new URL(apis.getUserRegister());
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
                        + "\"name\": \"" + mUserName.getEditableText().toString()+ "\","
                        + "\"email\": \"" + mEmail.getEditableText().toString()+ "\","
                        + "\"organization_name\": \"" + mOrganization.getEditableText().toString()+ "\","
                        + "\"mobile_number\": \"" + mMobile.getEditableText().toString()+ "\","
                        + "\"password\": \""+mPassword.getEditableText().toString()+"\""
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

//                JSONObject jsonObjectresult = jsonObject.getJSONObject("result");
//                JSONObject jsonObjectdetails = jsonObjectresult.getJSONObject("details");
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
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Register Fails...!", Toast.LENGTH_LONG).show();
            }

        }
    }


}