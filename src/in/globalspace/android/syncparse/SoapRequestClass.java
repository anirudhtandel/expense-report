package in.globalspace.android.syncparse;

import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SoapRequestClass {
	public static String response="";
	public static Context ctx;
	
	static SyncManagerDBAdapter databaseManager;
	
	public SoapRequestClass(Context mContext){
		ctx = mContext;
		databaseManager = SyncManagerDBAdapter.getSharedObject(mContext);
	}
	
	public  String ConcatPrimaryString(String requestType) {
		
		StringBuilder returnString=new StringBuilder();
		returnString.append("<presentId>");		
		
		if( requestType.equals("all")){
			returnString.append("<plac_are_data>");
			returnString.append(presentmumtimediainfo("addlocation","location_id"));
			returnString.append("</plac_are_data>");
		}
		
		if( requestType.equals("all")){
			returnString.append("<group_type>");
			returnString.append(presentmumtimediainfo("gst_group_type","group_id"));
			returnString.append("</group_type>");
		}
		
		if(requestType.equals("all")){			
			returnString.append("<expense_type_master>");
			returnString.append(presentmumtimediainfo("gst_exp_type_master","exp_type_id"));
			returnString.append("</expense_type_master>");			
		}
		
		if ( requestType.equals("all")) {

			returnString.append("<trip_type>");
			returnString.append(presentmumtimediainfo("gst_trip_type","trip_id"));
			returnString.append("</trip_type>");
		}
		
		if( requestType.equals("all")){
			
			returnString.append("<status>");
			returnString.append(presentmumtimediainfo("statustype","status_id"));
			returnString.append("</status>");			
			
		}
		if(requestType.equals("all")){
			
			returnString.append("<travel_mode>");
			returnString.append(presentmumtimediainfo("travelmode","travel_id"));
			returnString.append("</travel_mode>");		
		}
		
		if(requestType.equals("all")){			
			returnString.append("<pr_gr_dtl>");
			returnString.append(presentmumtimediainfo("emp_detail","employee_id"));
			returnString.append("</pr_gr_dtl>");			
		}
		
		if ( requestType.equals("all")) {

			returnString.append("<rel_exp_grp>");
			returnString.append(presentmumtimediainfo("gst_exp_grp_rel","rel_id"));
			returnString.append("</rel_exp_grp>");
		}
		
		if( requestType.equals("all")){
			
			returnString.append("<expense_master>");
			returnString.append(presentmumtimediainfo("gst_expense_master","exp_id"));
			returnString.append("</expense_master>");			
			
		}
		if(requestType.equals("all")){
			
			returnString.append("<expense_policy>");
			returnString.append(presentmumtimediainfo("gst_exp_policy","exp_id"));
			returnString.append("</expense_policy>");		
		}
		returnString.append("</presentId>");
		
		return returnString.toString();
	}

	private static String presentmumtimediainfo(String tableName,String columnname){
		String returnString="0";
		String defaultId="0";
		SQLiteDatabase sqdb = databaseManager.getWritableDatabase();
	    Cursor cursor =sqdb.rawQuery("SELECT synctimestamp FROM syncTimeTable where tableName = '"+tableName+"';",null);		
	    try {
			int count = cursor.getCount();
			if (!(count == 0)) {
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					   		//If asked for present Id
							defaultId=cursor.getString(cursor.getColumnIndex("synctimestamp"));
							cursor.moveToNext();
				}
			} else {
				cursor.deactivate();
				cursor.close();
			}
		} catch (Exception E) {
			cursor.deactivate();
			cursor.close();
		}		
		cursor =sqdb.rawQuery("SELECT MAX("+columnname+") FROM "+tableName+"",null);
		try {
			int count = cursor.getCount();
			if (!(count == 0)) {
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					   //If asked for present Id
							returnString=cursor.getString(0);
							cursor.moveToNext();
				}
			} else {
				cursor.deactivate();
				cursor.close();
			}
		} catch (Exception E) {
			cursor.deactivate();
			cursor.close();
		}
		if(returnString==null){
			returnString="0";
		}
		if(defaultId==null){
			defaultId="0";
		}
		sqdb.close();
		returnString = returnString+"~"+defaultId;
	return returnString;	
	}
}