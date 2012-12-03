package in.globalspace.android.expensereport;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewUtils {

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
		// return true;
	}

	public static void setTypeFaceForChilds(ViewGroup viewGroup,
			Typeface typeface) {
		final int childCount = viewGroup.getChildCount();

		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);

			if (view instanceof ViewGroup) {
				setTypeFaceForChilds((ViewGroup) view, typeface);
			} else if (view instanceof TextView) {
				((TextView) view).setTypeface(typeface);
			}

		}
	}
	
	


}
