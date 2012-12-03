package in.globalspace.android.dbclass;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataHelperClass {

	private Context mContext;
	private static SQLiteDatabase dbinstance;
	private SyncManagerDBAdapter syncDataManager;

	public DataHelperClass(Context con, SQLiteDatabase db) {
		mContext = con;
		dbinstance = db;
	}

	public DataHelperClass(Context con) {
		mContext = con;

	}

	public void _DataHelperClass() {
		dbinstance.close();
	}

	public String[][] getDaDatas(String da_id){
		String[][] returnList = new String[2][];
		String[] daId = null,daName = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		String sql="Select exp_type_id,exp_type_label from gst_exp_type_master where exp_group_id ="+da_id;		
		Cursor tCursor = sqdb.rawQuery(sql, null);			
		int i = 0;
		if (tCursor != null) {
			daId=new String[tCursor.getCount()];
			daName=new String[tCursor.getCount()];
			if (tCursor.moveToFirst()) {
				do {
					try {				
						daId[i]=String.valueOf(tCursor.getString(0));
						daName[i]=String.valueOf(tCursor.getString(1));								
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				} while (tCursor.moveToNext());
			}
		}
		tCursor.close();
		returnList[0]=daId;
		returnList[1]=daName;
		return returnList;
	}
	
	public String[][] getPlaceArea(String placearea_id){
		String[][] returnList = new String[2][];
		String[] placeId = null,placeName = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try{			
			String sql="select server_id,location_name from addlocation ";
			if(!placearea_id.equals(""))
				sql+="where server_id = "+placearea_id+" order by location_name ASC";
			else			
				sql+="where status='1' order by location_name ASC";			
			Cursor tCursor = sqdb.rawQuery(sql, null);			
			int i = 0;
			if (tCursor != null) {
				placeId=new String[tCursor.getCount()];
				placeName=new String[tCursor.getCount()];
				if (tCursor.moveToFirst()) {
					do {
						try {				
							placeId[i]=String.valueOf(tCursor.getString(0));
							placeName[i]=String.valueOf(tCursor.getString(1));								
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (tCursor.moveToNext());
				}
			}
			tCursor.close();
			returnList[0]=placeId;
			returnList[1]=placeName;
		}catch(Exception e){
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}
	
	public String[][] getTripType(String tmode_id){
		String[][] returnList = new String[2][];
		String[] travelId = null,travelName = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try{			
			String sql="select trip_id,trip_name from gst_trip_type ";
			if(!tmode_id.equals(""))
				sql+="where trip_id = "+tmode_id+" order by trip_name ASC";
			else			
				sql+="order by trip_name ASC";			
			Cursor tCursor = sqdb.rawQuery(sql, null);			
			int i = 0;
			if (tCursor != null) {
				travelId=new String[tCursor.getCount()];
				travelName=new String[tCursor.getCount()];
				if (tCursor.moveToFirst()) {
					do {
						try {				
							travelId[i]=String.valueOf(tCursor.getString(0));
							travelName[i]=String.valueOf(tCursor.getString(1));								
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (tCursor.moveToNext());
				}
			}
			tCursor.close();
			returnList[0]=travelId;
			returnList[1]=travelName;
		}catch(Exception e){
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}
	
	public String[][] getTravelMode(String tmode_id){
		String[][] returnList = new String[2][];
		String[] travelId = null,travelName = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try{			
			String sql="select travel_id,travel_mode from travelmode ";
			if(!tmode_id.equals(""))
				sql+="where travel_id = "+tmode_id+" order by travel_mode ASC";
			else			
				sql+="order by travel_mode ASC";			
			Cursor tCursor = sqdb.rawQuery(sql, null);			
			int i = 0;
			if (tCursor != null) {
				travelId=new String[tCursor.getCount()];
				travelName=new String[tCursor.getCount()];
				if (tCursor.moveToFirst()) {
					do {
						try {				
							travelId[i]=String.valueOf(tCursor.getString(0));
							travelName[i]=String.valueOf(tCursor.getString(1));								
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (tCursor.moveToNext());
				}
			}
			tCursor.close();
			returnList[0]=travelId;
			returnList[1]=travelName;
		}catch(Exception e){
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}

	public String[][] getOtherExpenses(String oExpens_id){
		String[][] returnList = new String[2][];
		String[] expanseId = null,expanseName = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try{			
			String sql="select expanse_id,expanse_name from expanse_type ";
			if(!oExpens_id.equals(""))
				sql+="where expanse_id = "+oExpens_id+" order by expanse_name ASC";
			else			
				sql+="order by expanse_name ASC";			
			Cursor tCursor = sqdb.rawQuery(sql, null);			
			int i = 0;
			if (tCursor != null) {
				expanseId=new String[tCursor.getCount()];
				expanseName=new String[tCursor.getCount()];
				if (tCursor.moveToFirst()) {
					do {
						try {				
							expanseId[i]=String.valueOf(tCursor.getString(0));
							expanseName[i]=String.valueOf(tCursor.getString(1));								
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (tCursor.moveToNext());
				}
			}
			tCursor.close();
			returnList[0]=expanseId;
			returnList[1]=expanseName;
		}catch(Exception e){
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}
	
	public int InsertExpenseHeadData(String[] headData){
		int returnID=-1;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("employee_id", headData[0]);
		cv.put("date", headData[1]);
		cv.put("status", headData[2]);
		cv.put("Comments", headData[3]);
		sqdb.beginTransaction();
		long rowID = sqdb.insert("gst_exp_report_head", null, cv);
		sqdb.setTransactionSuccessful();
		sqdb.endTransaction();
		sqdb.close();
		returnID = (int) rowID;
		return returnID;
	}
	
	public int InsertExpenseDetailData(String[] detailData){
		int returnID=-1;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("exp_rep_hd_localKey", detailData[0]);
		cv.put("expense_master_id", detailData[1]);
		cv.put("total_amount", detailData[2]);		
		sqdb.beginTransaction();
		long rowID = sqdb.insert("gst_exp_report_detail", null, cv);
		sqdb.setTransactionSuccessful();
		sqdb.endTransaction();
		sqdb.close();
		returnID = (int) rowID;
		return returnID;
	}
	
	
	public void InsertExpenseReportData(String[] reportData){
		int returnID=-1;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		cv.put("exp_rep_dtl_localKey", reportData[0]);
		cv.put("expense_policy_id", reportData[1]);
		cv.put("location_from", reportData[2]);		
		cv.put("location_to", reportData[3]);
		cv.put("km_travelled", reportData[4]);
		cv.put("transport_mode_id", reportData[5]);		
		cv.put("trip_type_id", reportData[6]);
		cv.put("ticket_number", reportData[7]);
		cv.put("amount", reportData[8]);
		
		sqdb.beginTransaction();
		sqdb.insert("gst_expense_report", null, cv);
		sqdb.setTransactionSuccessful();
		sqdb.endTransaction();
		sqdb.close();
		
	}
	
	
	public String[][] getExpenseSummaryData(String expenseID){
		String[][] returnSet=null;
		String sql="";
		
		sql="select localkey,expense_master_id,total_amount from gst_exp_report_detail " +
					"where  gst_exp_report_detail.exp_rep_hd_localKey=?  order by gst_exp_report_detail.localkey ASC;";
		
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		
		Cursor c =null;
		c = sqdb.rawQuery(sql, new String[]{expenseID});
				
		if(c!=null){
			int i=0;
			returnSet = new String[c.getCount()][3];			
			if (c.moveToFirst()) {
				do {
					try {				
							returnSet[i][0] = c.getString(c.getColumnIndex("localkey"));
							returnSet[i][1] = c.getString(c.getColumnIndex("expense_master_id"));
							returnSet[i][2] = c.getString(c.getColumnIndex("total_amount"));
							
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				} while (c.moveToNext());
			}
		}		
		return returnSet;
	}
	
	
	public String[][] getExpesesDates_summary(String expenseFromDate,String expenseToDate){
		String[][] returnDates=null;		
		String sql="";
		if(!expenseFromDate.equals(""))
			sql="select localkey,date from gst_exp_report_head " +					
					"where date BETWEEN '"+expenseFromDate+"' and '"+expenseToDate+"' and status!=1  order by localkey ASC;";	
		else
			sql="select localkey,date from gst_exp_report_head " +					
					"where status!=1  order by localkey ASC;";
		
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		Cursor c =null;
		c = sqdb.rawQuery(sql, null);		
		
		if(c!=null){
			int i=0;
			returnDates = new String[c.getCount()][2];			
			if (c.moveToFirst()) {
				do {
					try {				
						returnDates[i][0] = c.getString(c.getColumnIndex("localkey"));
						returnDates[i][1] = c.getString(c.getColumnIndex("date"));														
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				} while (c.moveToNext());
			}
		}		
		
		return returnDates;
	}
	
	
	/*
	public void insertExpanseRow(ArrayList<String> expanseRow) {
		try {
			syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
			SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
			String sql="";
			if(!expanseRow.get(12).equals("")){
				sql="Update expanse_transactions SET trans_date='"+expanseRow.get(0)+"',from_place='"+expanseRow.get(1)+"'," +
						"to_place='"+expanseRow.get(2)+"',travel_km='"+expanseRow.get(3)+"',travel_allow='"+expanseRow.get(4)+"'," +
						"travel_mode='"+expanseRow.get(5)+"',travel_type='"+expanseRow.get(6)+"',hq_allow='"+expanseRow.get(7)+"'," +
						"os_allow='"+expanseRow.get(8)+"',other_head='"+expanseRow.get(9)+"',other_allow='"+expanseRow.get(10)+"'," +
						"total='"+expanseRow.get(11)+"' where local_id = '"+expanseRow.get(12)+"'";
			}else{
				sql = "Insert into expanse_transactions(created_by,created_name,trans_date,from_place,to_place,travel_km,travel_allow,travel_mode,travel_type,hq_allow,os_allow,other_head,other_allow,total,appr_flag)"
					+ "values (1,'Anirudh','"
					+ expanseRow.get(0)
					+ "','"
					+ expanseRow.get(1)
					+ "','"
					+ expanseRow.get(2)
					+ "','"
					+ expanseRow.get(3)
					+ "','"
					+ expanseRow.get(4)
					+ "','"
					+ expanseRow.get(5)
					+ "','"
					+ expanseRow.get(6)
					+ "'"
					+ ",'"
					+ expanseRow.get(7)
					+ "','"
					+ expanseRow.get(8)
					+ "','"
					+ expanseRow.get(9)
					+ "','"
					+ expanseRow.get(10)
					+ "','"
					+ expanseRow.get(11)
					+ "','0')";
			}
			//String sql = "delete from expanse_transactions where trans_date = '"
			//		+ expanseRow.get(0) + "';";
			//sqdb.execSQL(sql);
			
			sqdb.execSQL(sql);
			sqdb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	//transaction_id INTEGER NULL,created_by INTEGER NULL,created_name
	public String[][] getexpanserequestdatas(String userID,String searchMonth) {
		Cursor rCursor = null;
		String[][] returnList = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try {			
			String sql = "select trans_date,from_place,to_place,travel_km,travel_allow,travel_mode,travel_type,hq_allow,os_allow,other_head,other_allow,total,transaction_id,created_name,appr_flag " +
					"from expanse_transactions where created_by !="+userID+" " +
					"order by transaction_id ASC;";
			if(!searchMonth.equals("")){
				sql = "select trans_date,from_place,to_place,travel_km,travel_allow,travel_mode,travel_type,hq_allow,os_allow,other_head,other_allow,total,transaction_id,created_name,appr_flag " +
				"from expanse_transactions where created_by !="+userID+" and trans_date like '%"	+ searchMonth	+ "'" +
				" order by transaction_id ASC;";
			}
			rCursor = sqdb.rawQuery(sql, null);
			String[][] returnLists = new String[rCursor.getCount()][14];
			int i = 0;
			if (rCursor != null) {
				if (rCursor.moveToFirst()) {
					do {

						try {
							for (int j = 0; j <= 13; j++) {
								returnLists[i][j] = String.valueOf(rCursor
										.getString(j));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (rCursor.moveToNext());
				}
			}
			rCursor.close();
			returnList = returnLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}
	
	public void updateExpense(String[] eData){
		ContentValues cv = new ContentValues();		
		cv.put("travel_km", eData[0]);
		cv.put("travel_allow", eData[1]);
		cv.put("hq_allow", eData[2]);
		cv.put("os_allow", eData[3]);
		cv.put("other_allow", eData[4]);
		cv.put("total", eData[5]);
		String transID = eData[6];
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		sqdb.update("expanse_transactions", cv, "transaction_id=?",new String[]{transID});
	}
	
	public String[][] getexpansedatas(String userid) {
		Cursor rCursor = null;
		String[][] returnList = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try {
			String sql = "select trans_date,from_place,to_place,travel_km,travel_allow,travel_mode,travel_type,hq_allow,os_allow,other_head,other_allow,total,local_id,appr_flag " +
					"from expanse_transactions where created_by ="+userid+" " +
					"order by local_id ASC;";
			rCursor = sqdb.rawQuery(sql, null);
			String[][] returnLists = new String[rCursor.getCount()][14];
			int i = 0;
			if (rCursor != null) {
				if (rCursor.moveToFirst()) {
					do {

						try {
							for (int j = 0; j <= 13; j++) {
								returnLists[i][j] = String.valueOf(rCursor
										.getString(j));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (rCursor.moveToNext());
				}
			}
			rCursor.close();
			returnList = returnLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}

	public String[][] getexpansedatas(String userid,String monthYear,String statusType) {
		Cursor rCursor = null;
		String[][] returnList = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try {
			String sql = "select trans_date,from_place,to_place,travel_km,travel_allow,travel_mode,travel_type,hq_allow,os_allow,other_head,other_allow,total,local_id,appr_flag "
					+ "	from expanse_transactions "
					+ "	where created_by ="+userid+" and trans_date like '%"
					+ monthYear
					+ "' and appr_flag = '"+statusType+"' order by local_id ASC;";

			rCursor = sqdb.rawQuery(sql, null);

			String[][] returnLists = new String[rCursor.getCount()][14];
			int i = 0;
			if (rCursor != null) {
				if (rCursor.moveToFirst()) {
					do {

						try {
							for (int j = 0; j <= 13; j++) {
								returnLists[i][j] = String.valueOf(rCursor
										.getString(j));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (rCursor.moveToNext());
				}
			}
			rCursor.close();
			returnList = returnLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}
	
	public String[][] getexpansedata_edit(String rowIdArray) {
		Cursor rCursor = null;
		String[][] returnList = null;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try {
			String sql = "select trans_date,from_place,to_place,travel_km,travel_allow,travel_mode,travel_type,hq_allow,os_allow,other_head,other_allow,total,local_id,appr_flag "
					+ "	from expanse_transactions "
					+ "	where local_id in ("
					+ rowIdArray
					+ ") order by local_id ASC;";

			rCursor = sqdb.rawQuery(sql, null);

			String[][] returnLists = new String[rCursor.getCount()][14];
			int i = 0;
			if (rCursor != null) {
				if (rCursor.moveToFirst()) {
					do {

						try {
							for (int j = 0; j <= 13; j++) {
								returnLists[i][j] = String.valueOf(rCursor
										.getString(j));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					} while (rCursor.moveToNext());
				}
			}
			rCursor.close();
			returnList = returnLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.close();
		return returnList;
	}*/
	
	public int searchIndexOfArray(String searchValue,String[] searchArray){
		int returnPos=-1;
		for(int i=0;i<searchArray.length;i++){
			if(searchArray[i].equals(searchValue))
				returnPos=i;
		}
		return returnPos;
	}
}
