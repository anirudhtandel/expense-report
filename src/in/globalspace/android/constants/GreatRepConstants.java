/**
 * 
 */
package in.globalspace.android.constants;

import in.globalspace.android.constants.GetSettingConstants;
import android.content.Context;

public class GreatRepConstants {

	public static String ADD_PHARMACLIENT = "managePharmaClient";
	public static String POST_UNSCHEDULECALL_REPORT = "setUnscheduleCallReport";
	public static String SUBMIT_CCR = "wsCcrSetSchedule";

	public static String POST_OTHER_ACTIVITY_REPORT = "wsSetOtherActivityReport";
	public static String POST_STOCKIST_CALL_REPORT = "wsSetStockistCallReport";
	public static String GETALL_INVOICE = "wsGetAllInvoice";
	public static String GETALL_INVOICE_DETAILS = "wsGetInvoiceDetail";
	public static String GET_STOCKIST_CALLREPORT = "wsGetStockistCallReport";
	public static String SET_INVOICE_DETAILS = "wsSetInvoiceDetail";
	public static String MULTIMEDIA_PLAY_HISTORY = "wsSetMultimediaPlayHistory";
	public static String INIT_SYNC = "wsInitSync";
	public static String CREATE_ACTIVITY_PLANNER = "wsSetActivityPlanner";
	public static String SET_EXPENSE_LOCATION="wsSetplaceArea";
	public static String SET_EXPENSE_LOCATION_APR="wsApprovedplaceArea";
	public static String GET_AUTH = "getAuth";
	
	
	
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
