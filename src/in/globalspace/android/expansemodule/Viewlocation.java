package in.globalspace.android.expansemodule;

import in.globalspace.android.constants.GreatRepConstants;
import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.expensereport.R;
import in.globalspace.android.expensereport.ViewUtils;
import in.globalspace.android.soapservice.model.SOAPNameValuePair;
import in.globalspace.android.soapservice.model.SOAPRequestHelper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Viewlocation extends Activity {

	Typeface font1;
	Context ctx;
	SyncManagerDBAdapter databaseManager;
	SQLiteDatabase sqdb;
	CheckBox chkAll, chkb;
	ArrayList<String> locationname = new ArrayList<String>();
	ArrayList<String> areatype = new ArrayList<String>();
	ArrayList<String> metrotype = new ArrayList<String>();
	ArrayList<Integer> pos1 = new ArrayList<Integer>();
	ArrayList<Integer> exp_type_id = new ArrayList<Integer>();
	ArrayList<String> exp_type_name = new ArrayList<String>();
	ArrayList<Integer> loc_id = new ArrayList<Integer>();
	ArrayList<String> server_id = new ArrayList<String>();
	ArrayList<String> locnamesubmit = new ArrayList<String>();
	ArrayList<String> atypesubmit = new ArrayList<String>();
	ArrayList<String> mtypesubmit = new ArrayList<String>();
	ArrayList<String> locidsubmit = new ArrayList<String>();
	ArrayList<String> serveridsubmit = new ArrayList<String>();

	ArrayList<String> newlocnamesubmit = new ArrayList<String>();
	ArrayList<String> newatypesubmit = new ArrayList<String>();
	ArrayList<String> newmtypesubmit = new ArrayList<String>();
	ArrayList<String> newlocidsubmit = new ArrayList<String>();
	ArrayList<String> newserveridsubmit = new ArrayList<String>();

	ArrayList<String> editlocnamesubmit = new ArrayList<String>();
	ArrayList<String> editatypesubmit = new ArrayList<String>();
	ArrayList<String> editmtypesubmit = new ArrayList<String>();
	ArrayList<String> editlocidsubmit = new ArrayList<String>();
	ArrayList<String> editserveridsubmit = new ArrayList<String>();

	private TextView viewlocation_name, view_areatype, view_metrotype;
	private int rowCounter = 0;
	private ViewGroup insertPoint1, rootView1, layoutRootView1;
	private Button submitlocation, deletelocation;
	String userid = "";
	public static final String PROVIDER_NAME = "in.globalspace.android.configurations";
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/configs");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.viewlocation, null);
		this.setContentView(viewToLoad);
		insertPoint1 = (ViewGroup) findViewById(R.id.headerlayout12);
		ctx = this;
		rowCounter = 0;
		databaseManager = SyncManagerDBAdapter.getSharedObject(ctx);

		rootView1 = (ViewGroup) findViewById(R.id.viewlocation_root);
		font1 = Typeface.createFromAsset(getAssets(), "MyriadPro-Cond.ttf");
		ViewUtils.setTypeFaceForChilds(rootView1, font1);

		chkAll = (CheckBox) findViewById(R.id.viewlocation_checkbox);
		bindDataForLocation();
		bindDataForId();
		displaydata();

		submitlocation = (Button) findViewById(R.id.submitvalues_viewlocation);
		deletelocation = (Button) findViewById(R.id.deletevalues_viewlocation);

		chkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checkall(isChecked);
			}
		});
		submitlocation.setOnClickListener(submitvalues);
		deletelocation.setOnClickListener(deletevalues);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UpdateListView();
	}
	
	public void UpdateListView(){
		insertPoint1.removeAllViews();
		rowCounter=0;
		clearArrayList();
		bindDataForLocation();
		bindDataForId();
		displaydata();
	}
	
	public void checkall(boolean ischecked) {
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint1.getChildAt(i);
			chkb = (CheckBox) getV.findViewById(R.id.viewdata_checbox);
			chkb.setChecked(ischecked);
		}

	}

	public void getSeedValue() {
		try {
			userid = getConfigValue("userID", this);
			
		} catch (Exception ex) {
			Log.d("SOAP ERROR", "Error fetching auth details from seed pp");
		}
	}
	
	public static String getConfigValue(String name, Context mContext) {
		Uri allConfigs = Uri.parse(CONTENT_URI + "/" + name);
		String returnVal = "";
		Cursor c = mContext.getContentResolver().query(allConfigs, null, null,
				null, null);
		if (c.moveToFirst()) {
			returnVal = c.getString(2);
			if (c != null)
				c.close();
		} else if (c != null)
			c.close();
		return returnVal;
	}

	OnClickListener submitvalues = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			final String LOCATION_ID = "location_id";

			String locname = "", atype = "", mtype = "", locationna = "", aType, mType, areaType = "", metroType1 = "", serverid = "", local_id = "";
			String editlocationna = "", editlocname = "", editaType, editareaType = "", editmType, editmetroType = "";
			pos1.clear();

			sqdb = databaseManager.getWritableDatabase();
			sqdb.beginTransaction();

			int getDposition = 0;
			for (int i = 0; i < rowCounter; i++) {
				View getV = insertPoint1.getChildAt(i);

				chkb = (CheckBox) getV.findViewById(R.id.viewdata_checbox);
				viewlocation_name = (TextView) getV
						.findViewById(R.id.view_location_name);
				view_areatype = (TextView) getV
						.findViewById(R.id.viewdata_areatype);
				view_metrotype = (TextView) getV
						.findViewById(R.id.viewdata_metrotype);
				if (chkb.isChecked()) {

					pos1.add(getDposition);
					locname = viewlocation_name.getText().toString();
					atype = view_areatype.getTag().toString();
					mtype = view_metrotype.getTag().toString();
					local_id = viewlocation_name.getTag().toString();
					serverid = chkb.getTag().toString();

					int getlocid1 = loc_id.get(getDposition);
					
					
					ContentValues cv = new ContentValues();
                    cv.put("status", 1);
                    sqdb.update(
                            "addlocation",
                            cv,   
                            "location_id=?",
                            new String[] { String
                                    .valueOf(getlocid1) });
					locnamesubmit.add(locname);
					atypesubmit.add(atype);
					mtypesubmit.add(mtype);
					locidsubmit.add(local_id);
					serveridsubmit.add(serverid);
				}
				getDposition++;

			}
			sqdb.setTransactionSuccessful();
			sqdb.endTransaction();
			sqdb.close();
			databaseManager.close();

			for (int k = 0; k < pos1.size(); k++) {
				int getPos = pos1.get(k);
				insertPoint1.removeViewAt(getPos - k);
				rowCounter--;
			}
			chkAll.setChecked(false);

			for (int i = 0; i < serveridsubmit.size(); i++) {

				String getServerId = serveridsubmit.get(i).toString();
				if (getServerId.equals("") || getServerId == null) {
					String getlocname = locnamesubmit.get(i).toString();
					newlocnamesubmit.add(getlocname);

					String getatype = atypesubmit.get(i).toString();
					newatypesubmit.add(getatype);

					String getmtype = mtypesubmit.get(i).toString();
					newmtypesubmit.add(getmtype);

					String getlocid = locidsubmit.get(i).toString();
					newlocidsubmit.add(getlocid);

					String getserid = serveridsubmit.get(i).toString();
					newserveridsubmit.add(getserid);

				} else {
					String geteditlocname = locnamesubmit.get(i).toString();
					editlocnamesubmit.add(geteditlocname);

					String geteditatype = atypesubmit.get(i).toString();
					editatypesubmit.add(geteditatype);

					String geteditmtype = mtypesubmit.get(i).toString();
					editmtypesubmit.add(geteditmtype);

					String geteditlocid = locidsubmit.get(i).toString();
					editlocidsubmit.add(geteditlocid);

					String geteditserid = serveridsubmit.get(i).toString();
					editserveridsubmit.add(geteditserid);

				}

			}

			String serverString = "", localidString = "", editlocalidString = "", editserverString = "";

			for (int i = 0; i < newlocnamesubmit.size(); i++) {
				String locationname = newlocnamesubmit.get(i);
				locname = locationname;
				locationna += locname + "~";

				String areatype = newatypesubmit.get(i);
				aType = areatype;
				areaType += aType + "~";

				String metrotype = newmtypesubmit.get(i);
				mType = metrotype;
				metroType1 += mType + "~";

				String lidString = newlocidsubmit.get(i);
				localidString += lidString + "~";

				String sServer = newserveridsubmit.get(i);
				serverString += sServer + "~";

			}

			for (int i = 0; i < editlocnamesubmit.size(); i++) {
				String locationname = editlocnamesubmit.get(i);
				editlocname = locationname;
				editlocationna += editlocname + "~";

				String areatype = editatypesubmit.get(i);
				editaType = areatype;
				editareaType += editaType + "~";

				String metrotype = editmtypesubmit.get(i);
				editmType = metrotype;
				editmetroType += editmType + "~";

				String lidString = editlocidsubmit.get(i);
				editlocalidString += lidString + "~";

				String sServer = editserveridsubmit.get(i);
				editserverString += sServer + "~";

			}

			if(newlocnamesubmit.size()>0){
				locationna = locationna.substring(0, locationna.length() - 1);
				areaType = areaType.substring(0, areaType.length() - 1);
				metroType1 = metroType1.substring(0, metroType1.length() - 1);
				serverString = serverString.substring(0, serverString.length() - 1);
				localidString = localidString.substring(0,
						localidString.length() - 1);
			}
			
			
			if(editlocnamesubmit.size()>0){

			editlocationna = editlocationna.substring(0,
					editlocationna.length() - 1);
			editareaType = editareaType.substring(0, editareaType.length() - 1);
			editmetroType = editmetroType.substring(0,
					editmetroType.length() - 1);
			editserverString = editserverString.substring(0,
					editserverString.length() - 1);
			editlocalidString = editlocalidString.substring(0,
					editlocalidString.length() - 1);
			}
			if (!(editserverString.equals("") || editserverString == null)) {

				String sendStrings = new String();

				sendStrings = "<?xml version='1.0' encoding='UTF-8'?><placedetails><auth><token_id>{$tokenAuth}</token_id>"
						+ "<user_id>{$user_id}</user_id>"
						+ "</auth><add><area_id>"
						+ localidString
						+ "</area_id>"
						+ "<areaname>"
						+ locationna
						+ "</areaname><areatype>"
						+ areaType
						+ "</areatype><metrotype>"
						+ metroType1
						+ "</metrotype></add><edit><dbid>"
						+ editserverString
						+ "</dbid><area_id>"
						+ editlocalidString
						+ "</area_id>"
						+ "<areaname>"
						+ editlocationna
						+ "</areaname><areatype>"
						+ editareaType
						+ "</areatype><metrotype>"
						+ editmetroType
						+ "</metrotype></edit></placedetails>";

				Log.e("xml String", sendStrings);

				 Send(sendStrings, ctx);

			} else {

				String sendStrings = new String();

				sendStrings = "<?xml version='1.0' encoding='UTF-8'?><placedetails><auth><token_id>{$tokenAuth}</token_id>"
						+ "<user_id>{$user_id}</user_id>"
						+ "</auth><add><area_id>"
						+ localidString
						+ "</area_id>"
						+ "<areaname>"
						+ locationna
						+ "</areaname><areatype>"
						+ areaType
						+ "</areatype><metrotype>"
						+ metroType1
						+ "</metrotype></add></placedetails>";

				 Send(sendStrings, ctx);

			}

			UpdateListView();

		}
	};

	OnClickListener deletevalues = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			final String LOCATION_ID = "location_id";
			pos1.clear();
			sqdb = databaseManager.getWritableDatabase();
			sqdb.beginTransaction();
			int getDelposition = 0;

			for (int i = 0; i < rowCounter; i++) {
				View getV = insertPoint1.getChildAt(i);

				chkb = (CheckBox) getV.findViewById(R.id.viewdata_checbox);
				if (chkb.isChecked()) {
					pos1.add(getDelposition);
					int getlocid = loc_id.get(getDelposition);
					sqdb.delete("addlocation", LOCATION_ID + "=" + getlocid,
							null);

				}
				getDelposition++;

			}
			for (int k = 0; k < pos1.size(); k++) {
				int getPos = pos1.get(k);
				insertPoint1.removeViewAt(getPos - k);
				rowCounter--;
			}
			sqdb.setTransactionSuccessful();
			sqdb.endTransaction();
			sqdb.close();
			databaseManager.close();
			UpdateListView();
		}
	};

	public void cleararray() {
		locnamesubmit.clear();
		atypesubmit.clear();
		mtypesubmit.clear();
		locidsubmit.clear();
		serveridsubmit.clear();
	}

	public void clearArrayList(){
		
		locationname.clear();
		areatype.clear();
		metrotype.clear();
		pos1.clear();
		exp_type_id.clear();
		exp_type_name.clear();
		loc_id.clear();
		server_id.clear();
		locnamesubmit.clear();
		atypesubmit.clear();
		mtypesubmit.clear();
		locidsubmit.clear();
		serveridsubmit.clear();

		newlocnamesubmit.clear();
		newatypesubmit.clear();
		newmtypesubmit.clear();
		newlocidsubmit.clear();
		newserveridsubmit.clear();

		editlocnamesubmit.clear();
		editatypesubmit.clear();
		editmtypesubmit.clear();
		editlocidsubmit.clear();
		editserveridsubmit.clear();
		
	}
	
	
	public void displaydata() {

		for (int i = 0; i < locationname.size(); i++) {
			View v = LayoutInflater.from(this.getParent()).inflate(
					R.layout.viewdata, null);

			layoutRootView1 = (ViewGroup) v.findViewById(R.id.viewlocation);
			ViewUtils.setTypeFaceForChilds(layoutRootView1, font1);

			CheckBox chkbx = (CheckBox) v.findViewById(R.id.viewdata_checbox);
			viewlocation_name = (TextView) v
					.findViewById(R.id.view_location_name);
			view_areatype = (TextView) v.findViewById(R.id.viewdata_areatype);
			view_metrotype = (TextView) v.findViewById(R.id.viewdata_metrotype);

			viewlocation_name.setText(locationname.get(i).toString());
			viewlocation_name.setTag(String.valueOf(loc_id.get(i).toString()));
			try {
				chkbx.setTag(String.valueOf(server_id.get(i).toString()));
			} catch (Exception e) {
				chkbx.setTag("");
			}

			String aty = areatype.get(i).toString();
			int getpo = exp_type_id.indexOf(Integer.parseInt(aty));
			String expareaName = exp_type_name.get(getpo);
			view_areatype.setText(expareaName);
			view_areatype.setTag(aty);
			String a3 = metrotype.get(i).toString();
			int a4 = exp_type_id.indexOf(Integer.parseInt(a3));
			String expmetroName = exp_type_name.get(a4);
			view_metrotype.setText(expmetroName);
			view_metrotype.setTag(a3);
			// view_metrotype.setText(metrotype.get(i).toString());

			insertPoint1.addView(v, rowCounter, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			rowCounter++;
		}

	}

	public void bindDataForLocation() {

		sqdb = databaseManager.getWritableDatabase();
		Cursor c = sqdb.rawQuery("select * from addlocation Where status=0",
				null);

		int count = c.getCount();
		if (count > 0) {
			c.moveToFirst();
			for (int i = 0; i < count; i++) {

				int locid = c.getInt(c.getColumnIndexOrThrow("location_id"));
				loc_id.add(locid);

				String loc_name = c.getString(c
						.getColumnIndexOrThrow("location_name"));
				locationname.add(loc_name);
				String area_ty = c.getString(c
						.getColumnIndexOrThrow("area_type"));
				areatype.add(area_ty);

				String metro_ty = c.getString(c
						.getColumnIndexOrThrow("metro_type"));
				metrotype.add(metro_ty);

				String serverid = c.getString(c
						.getColumnIndexOrThrow("server_id"));
				server_id.add(serverid);
				c.moveToNext();
			}
		}
		sqdb.close();
		

	}

	public static void Send(String sendStrings, Context gctx) {
		GreatRepConstants constant = new GreatRepConstants();
		constant.SOAPWebService(gctx);

		String namespace = GreatRepConstants.SOAP_NAMESPACE;
		String url = GreatRepConstants.SOAP_URL;
		String methodName = GreatRepConstants.SET_EXPENSE_LOCATION;
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

	public void bindDataForId() {
		sqdb = databaseManager.getWritableDatabase();

		Cursor c = sqdb.rawQuery(
				"select exp_type_id,exp_type_name from gst_exp_type_master ",
				null);

		int count = c.getCount();
		if (count > 0) {
			c.moveToFirst();
			for (int i = 0; i < count; i++) {

				int expid = c.getInt(c.getColumnIndexOrThrow("exp_type_id"));
				exp_type_id.add(expid);
				String exptypaname = c.getString(c
						.getColumnIndexOrThrow("exp_type_name"));
				exp_type_name.add(exptypaname);
				c.moveToNext();
			}
		}
	}

}
