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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

public class AddNewLocation extends Activity {

	Typeface font1;
	Context ctx;
	private int rowCounter = 0;
	private ViewGroup rootView, layoutRootView, insertPoint;
	private Button addnewRow, saveselectedValue, deleteselectedvalue,
			getdataBaseValues;
	private static ArrayAdapter<String> areatypeadapter, metrotypeadapter;
	ArrayList<String> get_area_type = new ArrayList<String>();
	ArrayList<Integer> exp_area_id = new ArrayList<Integer>();
	ArrayList<Integer> exp_metro_id = new ArrayList<Integer>();
	ArrayList<String> get_metro_type = new ArrayList<String>();
	ArrayList<String> get_area_type_db = new ArrayList<String>();
	ArrayList<String> get_metro_type_db = new ArrayList<String>();
	ArrayList<Integer> get_location_id = new ArrayList<Integer>();
	ArrayList<String> get_location_name = new ArrayList<String>();

	ArrayList<Integer> pos = new ArrayList<Integer>();
	EditText location_name;
	SyncManagerDBAdapter databaseManager;
	SQLiteDatabase sqdb;
	CheckBox chkAll;
	CheckBox chkb;
	Spinner areatypespinner, metrotypespinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.add_newlocation, null);
		this.setContentView(viewToLoad);
		insertPoint = (ViewGroup) findViewById(R.id.headerlayout1);
		ctx = this;
		rowCounter = 0;
		databaseManager = SyncManagerDBAdapter.getSharedObject(ctx);

		rootView = (ViewGroup) findViewById(R.id.addnewlocation_root);
		font1 = Typeface.createFromAsset(getAssets(), "MyriadPro-Cond.ttf");
		ViewUtils.setTypeFaceForChilds(rootView, font1);

		//bindData();
		binddataforareatype();
		binddataformetrotype();

		areatypeadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, get_area_type);

		areatypeadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		metrotypeadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, get_metro_type);

		metrotypeadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		addnewRow = (Button) findViewById(R.id.btnaddrow_newlocation);

		deleteselectedvalue = (Button) findViewById(R.id.deletevalues);
		saveselectedValue = (Button) findViewById(R.id.savevalues);
		chkAll = (CheckBox) findViewById(R.id.selectall_checkbox);

		chkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checkall(isChecked);
			}
		});

		deleteselectedvalue.setOnClickListener(deleteselectedrow);
		addnewRow.setOnClickListener(addnewRowListner);

		saveselectedValue.setOnClickListener(saveselectedListner);
		newRow();
	}

	OnClickListener addnewRowListner = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			newRow();
		}
	};

	OnClickListener saveselectedListner = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			pos.clear();
			sqdb = databaseManager.getWritableDatabase();
			sqdb.beginTransaction();
			int getDposition = 0;
			for (int i = 0; i < rowCounter; i++) {
				View getV = insertPoint.getChildAt(i);
				chkb = (CheckBox) getV.findViewById(R.id.selected_checbox);
				areatypespinner = (Spinner) getV
						.findViewById(R.id.spn_areatype);
				location_name = (EditText) getV
						.findViewById(R.id.location_name);
				metrotypespinner = (Spinner) getV
						.findViewById(R.id.spn_metrotype);
				if (chkb.isChecked()) {
					pos.add(getDposition);
					int getareaspin = areatypespinner.getSelectedItemPosition();
					int areva = exp_area_id.get(getareaspin);
					String areatypevalue = Integer.toString(areva);

					int getmetrospin = metrotypespinner
							.getSelectedItemPosition();
					int metva = exp_metro_id.get(getmetrospin);
					String metrotypevalue = Integer.toString(metva);

					String locationname = location_name.getText().toString();

					try {
						ContentValues cv = new ContentValues();
						cv.put("location_name", locationname);
						cv.put("area_type", areatypevalue);
						cv.put("metro_type", metrotypevalue);
						cv.put("status", 0);
						sqdb.insertOrThrow("addlocation", null, cv);

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				getDposition++;

			}
			removeRows();
			chkAll.setChecked(false);

			sqdb.setTransactionSuccessful();
			sqdb.endTransaction();
			sqdb.close();
			databaseManager.close();
			newRow();
		}
	};

	OnClickListener deleteselectedrow = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			pos.clear();
			int position = 0;
			try {

				for (int i = 0; i < rowCounter; i++) {
					View getV = insertPoint.getChildAt(i);
					chkb = (CheckBox) getV.findViewById(R.id.selected_checbox);
					if (chkb.isChecked()) {
						pos.add(position);
					}
					position++;

				}
				for (int j = 0; j < pos.size(); j++) {
					int s1 = pos.get(j);
					insertPoint.removeViewAt(s1 - j);
					rowCounter--;
				}
				chkAll.setChecked(false);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void newRow() {

		View v = LayoutInflater.from(this.getParent()).inflate(
				R.layout.addnew_location_row, null);

		layoutRootView = (ViewGroup) v.findViewById(R.id.newlocationrow);
		ViewUtils.setTypeFaceForChilds(layoutRootView, font1);
		location_name = (EditText) v.findViewById(R.id.location_name);
		areatypespinner = (Spinner) v.findViewById(R.id.spn_areatype);
		metrotypespinner = (Spinner) v.findViewById(R.id.spn_metrotype);
		chkb = (CheckBox) v.findViewById(R.id.selected_checbox);
		chkb.setChecked(true);
		areatypespinner.setAdapter(areatypeadapter);
		metrotypespinner.setAdapter(metrotypeadapter);
		location_name.requestFocus();
		insertPoint.addView(v, rowCounter, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		rowCounter++;

	}

	public void checkall(boolean ischecked) {
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint.getChildAt(i);
			chkb = (CheckBox) getV.findViewById(R.id.selected_checbox);
			chkb.setChecked(ischecked);
		}

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

	public void clearActivity() {
		chkAll.setChecked(false);
		removeRows();
	}

	public void removeRows() {
		try {
			insertPoint.removeViews(0, rowCounter);

		} catch (Exception e) {
			e.printStackTrace();
		}
		rowCounter = 0;
	}

	public void bindData() {

		int flag = 2;
		sqdb = databaseManager.getWritableDatabase();
		sqdb.beginTransaction();
		try {
			for (int i = 0; i < 500; i++) {

				ContentValues cv = new ContentValues();
				cv.put("position_id", i);
				cv.put("requested_by", "requestedby" + i);
				cv.put("area_name", "AreaName" + i);
				cv.put("area_type", "AreaType" + i);
				cv.put("metro_type", "MetroType" + i);
				cv.put("reason", "Please Insert" + i);
				cv.put("approve_flag", flag);

				sqdb.insertOrThrow("approverequest", null, cv);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		sqdb.setTransactionSuccessful();
		sqdb.endTransaction();
		sqdb.close();
		databaseManager.close();
	}

	public void binddataforareatype() {
		sqdb = databaseManager.getWritableDatabase();
		Cursor c = sqdb
				.rawQuery(
						"select exp_type_id,exp_type_name from gst_exp_type_master where exp_group_id=2",
						null);

		int count = c.getCount();
		if (count > 0) {
			c.moveToFirst();
			for (int i = 0; i < count; i++) {

				int exptypeid = c
						.getInt(c.getColumnIndexOrThrow("exp_type_id"));
				exp_area_id.add(exptypeid);
				String typename = c.getString(c
						.getColumnIndexOrThrow("exp_type_name"));
				get_area_type.add(typename);
				c.moveToNext();
			}
		}
	}

	public void binddataformetrotype() {
		Cursor c = sqdb
				.rawQuery(
						"select exp_type_id,exp_type_name from gst_exp_type_master where exp_group_id=1",
						null);

		int count = c.getCount();
		if (count > 0) {
			c.moveToFirst();
			for (int i = 0; i < count; i++) {

				int exptypid = c.getInt(c.getColumnIndexOrThrow("exp_type_id"));
				exp_metro_id.add(exptypid);

				String categoryname = c.getString(c
						.getColumnIndexOrThrow("exp_type_name"));
				get_metro_type.add(categoryname);
				c.moveToNext();
			}
		}

	}

	public void cleararray() {
		get_location_id.clear();
		get_area_type_db.clear();
		get_metro_type_db.clear();

	}

}
