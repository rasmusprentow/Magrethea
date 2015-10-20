package com.parse.starter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by pseudo on 20/10/15.
 */
public class UnlockManager {


    public static String tableName = "Unlock";
    public static String masterKeyName = "MASTERKEY";

    private UnlockManager() {}

    private static UnlockManager instance;
    public static UnlockManager getInstance()
    {
        if(instance == null) {
         instance = new UnlockManager();
     }
        return instance;
    }
    public Task<Object> deleteAll()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);

        return  query.findInBackground().continueWith(new Continuation<List<ParseObject>, Object>() {
            @Override
            public Object then(Task<List<ParseObject>> task) throws Exception {
                return ParseObject.deleteAllInBackground(task.getResult());

            }
        });
    }

    public Task<Void> setNewCode(Activity activity, String code){

        String hashedValue = sha1Hash(code + ":" + getMasterKey(activity));

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        ParseObject gameScore = new ParseObject(tableName);
        gameScore.put("passkey", hashedValue);
        gameScore.put("code", code);
        gameScore.put("master", getMasterKey(activity));
        gameScore.put("unlock", true);

        gameScore.setACL(acl);
        return gameScore.saveInBackground();
    }

    public boolean hasMasterKey(Activity activity) {
        String key = getMasterKey(activity);
        Toast.makeText(activity, "Masterkey is: " + key, Toast.LENGTH_SHORT ).show();
        return (key != null && key.length() > 0);
    }

    private String getMasterKey(Activity activity){
        return activity.getSharedPreferences(this.getClass().getName(), 0).getString(masterKeyName, "");

    }

    public void setMasterKey(Activity activity, String masterCode){
        SharedPreferences settings = activity.getSharedPreferences(this.getClass().getName(), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(masterKeyName, masterCode);

        // Commit the edits!
        editor.commit();

    }


    String sha1Hash( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }


}
