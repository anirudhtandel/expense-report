package in.globalspace.android.dbclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SyncManagerDBAdapter extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "expanses_master.db";
	private static final int DATABASE_VERSION = 2;
	private static SyncManagerDBAdapter instance;
	public Context context;

	public static SyncManagerDBAdapter getSharedObject(Context context) {
		if (instance == null) {
			instance = new SyncManagerDBAdapter(context);
		}
		instance.context = context;

		return instance;
	}

	public SyncManagerDBAdapter(Context context) {

		super(context, DATABASE_NAME, new LeaklessCursorFactory(),
				DATABASE_VERSION);

	}

	public SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase sqdb = super.getWritableDatabase();

		sqdb.setLockingEnabled(true);
		return sqdb;
	}

	public SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
				
		db.execSQL("CREATE TABLE addlocation(location_id INTEGER PRIMARY KEY,location_name TEXT NULL,area_type TEXT NULL,metro_type TEXT NULL,server_id INTEGER NULL,status INTEGER NULL,reason TEXT NULL)");
        db.execSQL("CREATE TABLE approverequest(position_id INTEGER PRIMARY KEY,requestedby_id INTEGER NULL,position_name TEXT NULL,area_name TEXT NULL,area_type TEXT NULL,metro_type TEXT NULL,approve_flag INTEGER NULL,reason TEXT NULL,server_id INTEGER NULL)");
        		
		db.execSQL("CREATE TABLE statustype(status_type_id INTEGER PRIMARY KEY,status_id INTEGER NULL,status_name TEXT NULL);");
		db.execSQL("CREATE TABLE travelmode(travel_mode_id INTEGER PRIMARY KEY,travel_id TEXT NULL,travel_mode TEXT NULL);");

		db.execSQL("CREATE TABLE expanse_transactions_ta (local_id INTEGER PRIMARY KEY, transaction_id INTEGER NULL, created_by INTEGER NULL, created_name TEXT NULL, trans_date TEXT NULL, from_place TEXT NULL, to_place TEXT NULL, travel_km TEXT NULL, travel_mode TEXT NULL, travel_type TEXT, amount TEXT NULL, appr_flag INTEGER)");
		db.execSQL("CREATE TABLE expanse_transactions_da (local_id INTEGER PRIMARY KEY, transaction_id INTEGER, created_by INTEGER NULL, created_name TEXT NULL, trans_date TEXT NULL,exp_type_name INTEGER NULL, amount TEXT, appr_flag INTEGER NULL)");
		db.execSQL("CREATE TABLE expanse_transactions_oe (local_id INTEGER PRIMARY KEY, transaction_id INTEGER, created_by INTEGER, created_name TEXT NULL, trans_date TEXT,exp_type_name INTEGER NULL, amount TEXT, appr_flag INTEGER NULL)");
		
		
		db.execSQL("CREATE TABLE gst_exp_report_head ("+
				  "localkey INTEGER PRIMARY KEY,"+
				  "exp_rep_hd_id INTEGER NULL,"+
				  "employee_id INTEGER NULL,"+
				  "date TEXT NULL,"+
				  "status INTEGER NULL,"+
				  "Approved_by INTEGER NULL,"+
				  "Approved_date TEXT NULL,"+
				  "rejected_remarks TEXT NULL,"+
				  "comments TEXT NULL);");
		
		db.execSQL("CREATE TABLE gst_exp_report_detail ("+
				  "localkey INTEGER PRIMARY KEY,"+
				  "exp_rep_hd_localKey INTEGER NOT NULL,"+
				  "exp_rep_dtl_id INTEGER NULL,"+
				  "exp_rep_hd_id INTEGER NULL,"+
				  "expense_master_id INTEGER NULL,"+
				  "total_amount INTEGER NULL);");
		
		
		db.execSQL("CREATE TABLE gst_expense_report ("+
				  "localkey INTEGER PRIMARY KEY,"+
				  "expense_report_id INTEGER NULL,"+
				  "exp_rep_dtl_localKey INTEGER NOT NULL,"+
				  "exp_rep_dtl_id INTEGER NULL,"+
				  "expense_policy_id INTEGER NULL,"+
				  "location_from INTEGER NULL,"+
				  "location_to INTEGER NULL,"+
				  "km_travelled INTEGER NULL,"+
				  "transport_mode_id INTEGER NULL,"+
				  "trip_type_id INTEGER NULL,"+
				  "ticket_number INTEGER NULL,"+
				  "amount INTEGER NULL);");
		
		db.execSQL("CREATE TABLE expanse_transactions_appr (local_id INTEGER PRIMARY KEY, transaction_id INTEGER, created_by INTEGER, created_name TEXT, trans_date TEXT, from_place TEXT, to_place TEXT, travel_km TEXT, travel_allow TEXT, travel_mode TEXT, travel_type TEXT, da_allow TEXT, da_area TEXT, other_head TEXT, other_allow TEXT, total TEXT, appr_flag INTEGER)");
		//db.execSQL("CREATE TABLE gst_grp_exp_relation (local_id INTEGER PRIMARY KEY,exp_id INTEGER, group_id INTEGER)");
		db.execSQL("CREATE TABLE gst_trip_type (local_id INTEGER PRIMARY KEY,trip_id INTEGER, trip_name TEXT NULL)");
		db.execSQL("CREATE TABLE gst_exp_policy (local_id INTEGER PRIMARY KEY,exp_id TEXT NULL,  exptype_id TEXT NULL,  exp_title TEXT,exp_desc TEXT, value_type TEXT,range_from INTEGER, range_to INTEGER, amount TEXT, effective_date TEXT,actual TEXT NULL)");
		db.execSQL("CREATE TABLE gst_exp_type_master (local_id INTEGER PRIMARY KEY,exp_group_id INTEGER, exp_type_id INTEGER, exp_type_name TEXT,exp_type_label TEXT)");
		db.execSQL("CREATE TABLE gst_group_type (local_id INTEGER PRIMARY KEY,group_id INTEGER, group_name TEXT)");
		db.execSQL("CREATE TABLE gst_exp_grp_rel (local_id INTEGER PRIMARY KEY,rel_id INTEGER NULL,group_id INTEGER NULL, exp_id INTEGER NULL)");		
		db.execSQL("CREATE TABLE gst_expense_master (local_id INTEGER PRIMARY KEY,exp_id INTEGER NULL,exp_lable TEXT NULL, exp_title TEXT NULL, exp_desc TEXT NULL)");
		db.execSQL("CREATE TABLE emp_detail (local_id INTEGER PRIMARY KEY,employee_id INTEGER NULL,employee_name TEXT NULL, designation TEXT NULL,position TEXT NULL,approval_status TEXT NULL)");
		
        db
		.execSQL("CREATE TABLE syncTimeTable(tableName TEXT PRIMARY KEY,synctimestamp TEXT NULL)");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('addlocation',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('approverequest',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('statustype',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('travelmode',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('expanse_transactions_ta',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('expanse_transactions_da',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('expanse_transactions_oe',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('expense_trans',NULL);");		
				
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('gst_exp_policy',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('gst_exp_type_master',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('gst_group_type',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('gst_exp_grp_rel',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('gst_expense_master',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('emp_detail',NULL);");
		db.execSQL("INSERT INTO syncTimeTable(tableName,synctimestamp) values('gst_trip_type',NULL);");}

	public void clearDB() {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
