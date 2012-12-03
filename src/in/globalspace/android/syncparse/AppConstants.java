package in.globalspace.android.syncparse;

import android.content.Context;

public class AppConstants {

	public static final String REQUEST_TYPE = "RequestType";
	
	public static final String SYNC_TYPE_ALL="all";
	public static final String REQUEST_FOLDER="RequestFiles";
	
	
	public static String INIT_SYNC = "wsExpenseSync";
	public static String LAT_LONG_METHOD = "wsaddlonglat";
	
	
	public static String IP_ADDRESS = "ip_address";
	public static String SOAP_URL = "soap_url";
	public static String SOAP_ACTION = "soap_action";
	public static String SOAP_NAMESPACE = "soap_namespace";
	public static String SOAP_ADDRESS = "soap_address";

	public void SOAPWebService(Context ctx) {
		IP_ADDRESS = GetSettingConstants.getIpAddress(ctx);
		SOAP_NAMESPACE = GetSettingConstants.getSoapNameSpace(ctx);
		SOAP_URL = GetSettingConstants.getSoapUrl(ctx);
		SOAP_ACTION = GetSettingConstants.getSoapAction(ctx);
		SOAP_ADDRESS = GetSettingConstants.getSoapAddress(ctx);
	}
	
	
	
}