package microbi.organic.coworkuser.activity;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.util.HashMap;

public class PreferenceManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "DOAapp";

    // All Shared Preferences Keys

    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";

    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_REGISTERED = "isregistered";
    private static final String KEY_NOMINEE_ID= "idnominee";
    private static final String KEY_USERID = "userid";
    private static final String KEY_ASSETID = "subid";
    private static final String KEY_NOM_ASSETID = "nomidasset";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";





    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public  void setKeyDate(String date) {
        editor.putString(KEY_DATE, date);
        editor.commit();
    }

    public String getKeyDate(String date) {
        return pref.getString(KEY_DATE, null);
    }

    public  void setKeyUserid(String userid) {
        editor.putString(KEY_USERID, userid);
        editor.commit();
    }

    public String getKeyUserid(String id) {
        return pref.getString(KEY_USERID,null);
    }
    public  void setKeyStatus(String status) {
        editor.putString(KEY_STATUS, status);
        editor.commit();
    }
    public String getKeyStatus(String STATUS) {
        return pref.getString(KEY_STATUS,null);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }


    public  void setKeyAssetid(String idSub) {
        editor.putString(KEY_ASSETID, idSub);
        editor.commit();
    }

    public String getKeyAssetid(String idSub) {
        return pref.getString(KEY_ASSETID,null);
    }


    public  void setKeyNomAssetid(JSONArray nomidasset) {
        editor.putString(KEY_NOM_ASSETID, String.valueOf(nomidasset));
        editor.commit();
    }
    public String getKeyNomAssetid(String asssetid) {
        return pref.getString(KEY_NOM_ASSETID,null);
    }



    ///////////setters id nominee
    public String getKeyNomineeId(String idnomine) {
        return pref.getString(KEY_NOMINEE_ID,null);
    }

    public void  setKeyNomineeId(String idnominee) {
        editor.putString(KEY_NOMINEE_ID, idnominee);
        editor.commit();
    }

    public void createLogin(String name, String email, String mobile, String location, String password) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PASSWORD,password);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        profile.put("password",pref.getString(KEY_PASSWORD,null));
        return profile;
    }
    public void setKeyIsLoggedIn(String mobileNumber) {
        editor.putString(KEY_IS_LOGGED_IN, mobileNumber);
        editor.commit();
    }
    //retreiving the userid using key
    public String getKeyIsLoggedIn() {
        return pref.getString(KEY_IS_LOGGED_IN, null);
    }

    public void setKeyIsRegistered(String mobileNumber) {
        editor.putString(KEY_IS_REGISTERED, mobileNumber);
        editor.commit();
    }
    //retreiving the userid using key
    public String getKeyIsRegistered() {
        return pref.getString(KEY_IS_REGISTERED, null);
    }


}



