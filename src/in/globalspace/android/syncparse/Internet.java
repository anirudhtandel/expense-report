package in.globalspace.android.syncparse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

public class Internet {
	
	public static boolean checkConnection(Context c) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager telephonyManager = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnected()
				|| telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)
			return true;
		else
			return false;
		
	}

}