package amits.parking.com.parking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by jassi on 03-03-2018.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "SpotPark";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public static final String KEY_ID = "ID";
    public static final String KEY_USERNAME = "username";
    private static final String KEY_CONTACT = "contact";

    public static final String KEY_TYPE = "type";
    private static final String KEY_LISTID_1 = "0";
    private static final String KEY_LISTID_2 = "0";
    private static final String KEY_SERVICE_LISTID = "0";
    private static final String KEY_SERVICE_LISTID1 = "0";
    private static final String KEY_BID = "0";
    private static final String KEY_PID = "0";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setType(String type){
        editor.putString(KEY_TYPE, type);
        editor.apply();
    }

    public String getType(){
        return pref.getString(KEY_TYPE, "type");
    }


    public void setId(String id){
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    public String getId(){
        return pref.getString(KEY_ID, "ID");
    }

    public void setListSize(int id){
        editor.putInt(KEY_LISTID_1, id);
        editor.apply();
    }

    public int getListSize(){
        return pref.getInt(KEY_LISTID_1, 0);
    }

    public void removeListSize(){
        editor.remove(KEY_LISTID_1);
    }


    public void setListSize2(int id){
        editor.putInt(KEY_LISTID_2, id);
        editor.apply();
    }

    public int getListSize2(){
        return pref.getInt(KEY_LISTID_2, 0);
    }

    public void removeListSize2(){
        editor.remove(KEY_LISTID_2);
    }


    public void setServiceListSize(int id){
        editor.putInt(KEY_SERVICE_LISTID, id);
        editor.apply();
    }

    public int getServiceListSize(){
        return pref.getInt(KEY_SERVICE_LISTID, 0);
    }

    public void removeServiceListSize(){
        editor.remove(KEY_SERVICE_LISTID);
    }


    public void setServiceListSize2(int id){
        editor.putInt(KEY_SERVICE_LISTID1, id);
        editor.apply();
    }

    public int getServiceListSize2(){
        return pref.getInt(KEY_SERVICE_LISTID1, 0);
    }

    public void removeServiceListSize1(){
        editor.remove(KEY_SERVICE_LISTID1);
    }


    public void setBid(String id){
        editor.putString(KEY_BID, id);
        editor.apply();
    }

    public String getBid(){
       return pref.getString(KEY_BID, "0");
    }

    public void setPid(String id){
        editor.putString(KEY_PID, id);
        editor.apply();
    }

    public String getPid(){
        return pref.getString(KEY_PID, "0");
    }



}
