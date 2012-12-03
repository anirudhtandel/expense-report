package in.globalspace.android.expansemodule;

import in.globalspace.android.constants.GreatRepConstants;
import in.globalspace.android.dbclass.DataHelperClass;
import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.expensereport.R;
import in.globalspace.android.soapservice.model.SOAPNameValuePair;
import in.globalspace.android.soapservice.model.SOAPRequestHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PlaceAreaApprovalRequest extends Activity {

	private Context mContext;
	private ViewGroup insertPoint;
	private Button btnapprove, btnreject;
	private CheckBox chkall;
	private Integer rowCounter;
	private SyncManagerDBAdapter syncDataManager;
	private DataHelperClass DHC;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_approve_request);

		mContext = this;
		syncDataManager = SyncManagerDBAdapter.getSharedObject(mContext);
		insertPoint = (ViewGroup) findViewById(R.id.place_approve_request_row);
		btnapprove = (Button) findViewById(R.id.approve);
		btnreject = (Button) findViewById(R.id.reject);

		btnapprove.setOnClickListener(approvebtn);
		btnreject.setOnClickListener(rejectbtn);
		
		chkall = (CheckBox) findViewById(R.id.Selectall);

		rowCounter = 0;

		resetActivity();
		
		chkall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				// TODO Auto-generated method stub
				checkall(checked);
			}
		});
	}
	
	OnClickListener approvebtn = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub		
			Send(getViewsForSubmit(), mContext);
			resetActivity();
		}
	};
	
	OnClickListener rejectbtn = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(checkmultiple())
				savefilename(getSelected());
		}
	};

	public boolean checkmultiple() {
		boolean returnflag=false;
		int cCount=0;
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint.getChildAt(i);
			CheckBox chkb = (CheckBox) getV.findViewById(R.id.rowCheck);
			if(chkb.isChecked()){
				cCount++;
			}			
		}
		if(cCount==1){
			returnflag=true;
		}else{
			returnflag=false;
		}
		return returnflag;
	}
	
	public int getSelected() {
		int cnt = -1;
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint.getChildAt(i);
			CheckBox chkb = (CheckBox) getV.findViewById(R.id.rowCheck);
			if (chkb.isChecked()) {
				cnt=i;
			}
		}		
		return cnt;
	}
	public void removeRows() {
		try {
			insertPoint.removeViews(0, rowCounter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rowCounter = 0;
	}
	
	public void resetActivity() {
		removeRows();
		String[][] rowData = getApproveRequest();
		displayData(rowData);
		chkall.setChecked(false);
	}
	
	public String getViewsForSubmit() {
		String selectedIDs = "";
		String SEPARATOR="2^";
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint.getChildAt(i);
			CheckBox chkb = (CheckBox) getV.findViewById(R.id.rowCheck);
			TextView txtDB = (TextView) getV.findViewById(R.id.metro_type);
			if (chkb.isChecked()) {					
					selectedIDs += SEPARATOR+txtDB.getTag().toString() + "~";
					removeRecord(txtDB.getTag().toString());
			}
		}
		if (!selectedIDs.equals("")) {
			selectedIDs = selectedIDs.substring(0, selectedIDs.length() - 1);
		}
		return selectedIDs;
	}
	
	private void removeRecord(String DBID){
		try{
			SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
			sqdb.delete("approverequest", "server_id=?", new String[]{DBID});
			sqdb.close();
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public static void Send(String values, Context gctx) {
		
		String sendStrings = "<?xml version='1.0' encoding='UTF-8'?><apprved_are_data><auth><token_id>{$tokenAuth}</token_id>"
			+ "<user_id>{$user_id}</user_id>"
			+ "</auth><plac_approved>"
			+ values
			+ "</plac_approved></apprved_are_data>";
		
		GreatRepConstants constant = new GreatRepConstants();
		constant.SOAPWebService(gctx);

		String namespace = GreatRepConstants.SOAP_NAMESPACE;
		String url = GreatRepConstants.SOAP_URL;
		String methodName = GreatRepConstants.SET_EXPENSE_LOCATION_APR;
		String soapAction = GreatRepConstants.SOAP_ACTION + methodName;

		SOAPRequestHelper soapRequestHelper = new SOAPRequestHelper(namespace,
				soapAction, methodName, url);
		soapRequestHelper.setRequestType(SOAPRequestHelper.REQUEST_TYPE_POST);

		SOAPNameValuePair[] props = new SOAPNameValuePair[] { new SOAPNameValuePair(
				"xmlString", sendStrings), };

		soapRequestHelper.setRequestType(1);
		soapRequestHelper.setProperties(props);
		soapRequestHelper.performSOAPRequest(gctx);

	}
	
	
	public void checkall(boolean ischecked) {
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint.getChildAt(i);
			CheckBox chkb = (CheckBox) getV.findViewById(R.id.rowCheck);
			chkb.setChecked(ischecked);
		}
	}
	
	public void displayData(String[][] eData) {
		try {
			if (eData != null) {
				for (int i = 0; i < eData.length; i++) {
					String[] tString = eData[i];

					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v = vi.inflate(R.layout.place_approve_request_row,
							null);
					
					TextView requestby = (TextView) v.findViewById(R.id.req_id);
					TextView positionName = (TextView) v
							.findViewById(R.id.position_name);
					TextView areaName = (TextView) v
							.findViewById(R.id.area_name);
					TextView areaType = (TextView) v
							.findViewById(R.id.area_type);
					TextView metroType = (TextView) v
							.findViewById(R.id.metro_type);

					CheckBox chkrow = (CheckBox) v.findViewById(R.id.rowCheck);

					requestby.setText(tString[0]);
					positionName.setText(tString[1]);
					areaName.setText(tString[2]);
					areaType.setText(tString[3]);
					metroType.setText(tString[4]);
					metroType.setTag(tString[5]);
					//chkrow.setHint(tString[5]);
					chkrow.setTag(rowCounter);
					insertPoint.addView(v, rowCounter,
							new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.FILL_PARENT,
									ViewGroup.LayoutParams.WRAP_CONTENT));
					rowCounter++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[][] getApproveRequest() {
		String[][] returnData = null;
		Cursor rCursor = null;
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		try {
			// position_id,requestedby_id,position_name,area_name,area_type,metro_type,approve_flag,reason,server_id)
			String sql = "select requestedby_id,position_name,area_name,area_type,metro_type,server_id from approverequest;";
			rCursor = sqdb.rawQuery(sql, null);
			String[][] returnLists = new String[rCursor.getCount()][6];
			int i = 0;
			if (rCursor != null) {
				if (rCursor.moveToFirst()) {
					do {

						try {
							for (int j = 0; j < 6; j++) {
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
			returnData = returnLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.close();
		return returnData;
	}




	private void savefilename(int indexC) {

		final Dialog dialog = new Dialog(getParent());
		final int indexCnt = indexC;		
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialogcustom);
		
		Button dialogbuttonyes = (Button) dialog.findViewById(R.id.dialogbuttonoK);
		Button diButtonno = (Button) dialog.findViewById(R.id.dialogbuttonno);

		final EditText txtrejectedReason = (EditText) dialog.findViewById(R.id.rejected);
		ViewGroup getV = (ViewGroup) insertPoint.getChildAt(indexCnt);
		final TextView dbID = (TextView) getV.findViewById(R.id.metro_type);
		
		
		dialog.show();
		
		dialogbuttonyes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String rReason = txtrejectedReason.getText().toString();
					String dbid = dbID.getTag().toString();
					String rString="3^"+dbid+"^"+rReason;
					Send(rString,mContext);
					removeRecord(dbid);
					resetActivity();
					dialog.dismiss();
				} catch (Exception e) {

				}
			}
		});
		
		diButtonno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txtrejectedReason.setText("");
				dialog.dismiss();
			}
		});
	}



}
