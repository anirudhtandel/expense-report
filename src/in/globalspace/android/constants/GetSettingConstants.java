package in.globalspace.android.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class GetSettingConstants {
	
	private static final String TAG = "GetSettingConstants";
	private static final String SETTING_PACKAGE = "com.shared.gretrepsettings";
	
	
	private static final String IP_PREFERENCE = "IP_PREFERENCE";
	private static final String SOAP_URL_PREFERENCE = "SOAP_URL_PREFERENCE";
	private static final String SOAP_ACTION_PREFERENCE = "SOAP_ACTION_PREFERENCE";
	private static final String SOAP_NAMESPACE_PREFERENCE = "SOAP_NAMESPACE_PREFERENCE";
	private static final String SOAP_ADDRESS_PREFERENCE = "SOAP_ADDRESS_PREFERENCE";
	
	private static final String IP_ADDRESS = "ip_address";
    private static final String SOAP_URL = "soap_url";
	private static final String SOAP_ACTION = "soap_action";
	private static final String SOAP_NAMESPACE = "soap_namespace";
	private static final String SOAP_ADDRESS = "soap_address";
	
	public static String getIpAddress(Context ctx){
		Context con = null;
		try {
			con=ctx.createPackageContext(SETTING_PACKAGE, 0);
		} catch (NameNotFoundException e) {
		};
		SharedPreferences sharedPreferences = con.getSharedPreferences(IP_PREFERENCE,Context.MODE_WORLD_READABLE);
		String ipAddress = sharedPreferences.getString(IP_ADDRESS,IP_ADDRESS);
		Log.e(TAG+ " IP Address", String.valueOf(ipAddress));
		return ipAddress;
	}
	
	public static String getSoapAddress(Context ctx){
		Context con = null;
		try {
			con=ctx.createPackageContext(SETTING_PACKAGE, 0);
		} catch (NameNotFoundException e) {
		};
		SharedPreferences sharedPreferences = con.getSharedPreferences(SOAP_ADDRESS_PREFERENCE,Context.MODE_WORLD_READABLE);
		String soapAddress = sharedPreferences.getString(SOAP_ADDRESS,SOAP_ADDRESS);
		Log.e(TAG+ " Soap Address", String.valueOf(soapAddress));
		return soapAddress;
		
	}
	
	public static String getSoapNameSpace(Context ctx){
		Context con = null;
		try {
			con=ctx.createPackageContext(SETTING_PACKAGE, 0);
		} catch (NameNotFoundException e) {
		};
		SharedPreferences sharedPreferences = con.getSharedPreferences(SOAP_NAMESPACE_PREFERENCE,Context.MODE_WORLD_READABLE);
		String soapNameSpace = sharedPreferences.getString(SOAP_NAMESPACE,SOAP_NAMESPACE);
		Log.e(TAG+ " Soap NameSpace", String.valueOf(soapNameSpace));
		return soapNameSpace;
		
	}
	
	public static String getSoapUrl(Context ctx){
		Context con = null;
		try {
			con=ctx.createPackageContext(SETTING_PACKAGE, 0);
		} catch (NameNotFoundException e) {
		};
		SharedPreferences sharedPreferences = con.getSharedPreferences(SOAP_URL_PREFERENCE,Context.MODE_WORLD_READABLE);
		String soapUrl = sharedPreferences.getString(SOAP_URL,SOAP_URL);
		Log.e(TAG+ " Soap Url", String.valueOf(soapUrl));
		return soapUrl;
	}
	
	public static String getSoapAction(Context ctx){
		Context con = null;
		try {
			con=ctx.createPackageContext(SETTING_PACKAGE, 0);
		} catch (NameNotFoundException e) {
		};
		SharedPreferences sharedPreferences = con.getSharedPreferences(SOAP_ACTION_PREFERENCE,Context.MODE_WORLD_READABLE);
		String soapAction = sharedPreferences.getString(SOAP_ACTION,SOAP_ACTION);
		Log.e(TAG + " Soap Action", String.valueOf(soapAction));
		return soapAction;
	}

}
