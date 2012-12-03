package in.globalspace.android.expansemodule;

import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.expensereport.R;
import in.globalspace.android.expensereport.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Displaylocation extends ListActivity {

	ViewGroup rootVIew, rootVIew1;
	Typeface font1;
	static Context dContext1;
	SyncManagerDBAdapter databaseManager;
	SQLiteDatabase sqdb;
	private static ArrayAdapter<String> areaadapter, metroadapter;
	ArrayList<Integer> exp_metro_id = new ArrayList<Integer>();
	ArrayList<String> get_metro_type = new ArrayList<String>();
	ArrayList<String> get_area_type = new ArrayList<String>();
	ArrayList<Integer> exp_area_id = new ArrayList<Integer>();
	String userid = "";

	public static final String PROVIDER_NAME = "in.globalspace.android.configurations";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/configs");

	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	ArrayList<Integer> status = new ArrayList<Integer>();
	ArrayList<String> locationName = new ArrayList<String>();
	ArrayList<Integer> locid = new ArrayList<Integer>();
	ArrayList<Integer> serverid = new ArrayList<Integer>();
	ArrayList<String> areaType = new ArrayList<String>();
	ArrayList<String> metroType = new ArrayList<String>();
	ArrayList<String> checkstatus = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		View viewToLoad1 = LayoutInflater.from(this.getParent()).inflate(
				R.layout.listview, null);
		this.setContentView(viewToLoad1);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		dContext1 = this;
		databaseManager = SyncManagerDBAdapter.getSharedObject(dContext1);
		rootVIew = (ViewGroup) findViewById(R.id.listview_root);
		/* ViewUtils.setTypeFaceForChilds(rootVIew, addExpenseReport.font1); */

		font1 = Typeface.createFromAsset(getAssets(), "MyriadPro-Cond.ttf");

		ViewUtils.setTypeFaceForChilds(rootVIew, font1);

		databinderFunction();

		binddataareatype();
		binddatametrotype();
		populateList();
		areaadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, get_area_type);

		areaadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		metroadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, get_metro_type);

		metroadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		SimpleAdapter adapter = new SimpleAdapter(dContext1, list,
				R.layout.customlayout, new String[] { "Location name",
						"Area type", "Metro type", "Status", "Edit" },
				new int[] { R.id.Locationname, R.id.Areatype, R.id.Metrotype,
						R.id.Status, R.id.viewedit

				}) {

			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
			}

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {

				View view = super.getView(position, convertView, parent);
				rootVIew1 = (ViewGroup) view
						.findViewById(R.id.rootcustomlayout1);
				ViewUtils.setTypeFaceForChilds(rootVIew1, font1);

				Button editButton = (Button) view.findViewById(R.id.viewedit);
				final Button statusCheck = (Button) view
						.findViewById(R.id.Status);

				int Requeststatus = status.get(position);
				editButton.setText("EDIT");
				if (Requeststatus == 1) {
					statusCheck.setText("Pending");
					editButton.setEnabled(false);
					statusCheck.setEnabled(false);
				} else if (Requeststatus == 3) {
					statusCheck.setText("Rejected");
					editButton.setEnabled(true);
					statusCheck.setEnabled(true);
				} else {

				}

				statusCheck.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						int Requeststatus = status.get(position);
						if (Requeststatus == 1) {
							Toast.makeText(dContext1,
									"Your Request is Pending",
									Toast.LENGTH_SHORT).show();

						} else if (Requeststatus == 3) {

							final Dialog dialog = new Dialog(getParent());
							dialog
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.custom);

							Typeface face = Typeface.createFromAsset(
									getAssets(), "UbuntuCondensed-Regular.ttf");

							TextView rejectstatus = (TextView) dialog
									.findViewById(R.id.rejectedstatus);

							Button dialogbuttonoK = (Button) dialog
									.findViewById(R.id.dialogbuttonoK);
							dialogbuttonoK.setTypeface(face);
							rejectstatus.setTypeface(face);

							rejectstatus.setText(checkstatus.get(position));
							dialogbuttonoK
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											dialog.dismiss();
										}
									});
							dialog.show();

						}

					}
				});

				editButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						final String POSITION_ID = "position_id";

						int Requeststatus = status.get(position);
						if (Requeststatus == 1) {
							Toast
									.makeText(
											dContext1,
											"Your Request is Pending You Are Not Able To Edit",
											Toast.LENGTH_SHORT).show();
						} else {

							final int loc_id = locid.get(position);
							String locationname = locationName.get(position)
									.toString();
							int areatype = Integer.parseInt(areaType.get(
									position).toString());
							int metrotype = Integer.parseInt(metroType.get(
									position).toString());

							final Dialog dialog = new Dialog(getParent());
							dialog
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.customlocation_edit);

							Typeface face = Typeface.createFromAsset(
									getAssets(), "UbuntuCondensed-Regular.ttf");

							TextView Locname = (TextView) dialog
									.findViewById(R.id.loc_name);

							final EditText locName = (EditText) dialog
									.findViewById(R.id.edit_locname);

							final Spinner areaType = (Spinner) dialog
									.findViewById(R.id.spinner_areatype);

							final Spinner metroType = (Spinner) dialog
									.findViewById(R.id.spinner_metrotype);

							TextView Areatype = (TextView) dialog
									.findViewById(R.id.area_type);
							TextView Metrotype = (TextView) dialog
									.findViewById(R.id.metro_type);

							Button buttonedit = (Button) dialog
									.findViewById(R.id.button_edit);
							buttonedit.setTypeface(face);
							buttonedit.setTypeface(face);

							locName.setText(locationname);
							metroType.setAdapter(metroadapter);
							areaType.setAdapter(areaadapter);

							int iA = exp_area_id.indexOf(areatype);

							int iM = exp_metro_id.indexOf(metrotype);

							areaType.setSelection(iA);
							metroType.setSelection(iM);

							/*
							 * areaType.setText(areatype);
							 * metroType.setText(metrotype);
							 */

							buttonedit
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {

											int getareaspin = areaType
													.getSelectedItemPosition();
											int areva = exp_area_id
													.get(getareaspin);
											String areatypevalue = Integer
													.toString(areva);

											int getmetrospin = metroType
													.getSelectedItemPosition();
											int metva = exp_metro_id
													.get(getmetrospin);
											String metrotypevalue = Integer
													.toString(metva);
											if (sqdb.isOpen()) {
												sqdb.close();
											}
											sqdb = databaseManager
													.getWritableDatabase();

											sqdb.beginTransaction();
											try {

												ContentValues cv = new ContentValues();
												cv.put("location_name", locName
														.getText().toString());
												cv.put("area_type",
														areatypevalue);
												cv.put("metro_type",
														metrotypevalue);
												cv.put("status", "0");
												sqdb
														.update(
																"addlocation",
																cv,
																"location_id=?",
																new String[] { String
																		.valueOf(loc_id) });
											} catch (SQLException e) {
												e.printStackTrace();
											}

											sqdb.setTransactionSuccessful();
											sqdb.endTransaction();
											sqdb.close();
											databaseManager.close();

											dialog.dismiss();

										}
									});
							dialog.show();

						}

					}

				});

				return view;

			}

		};
		getListView().setDivider(null);
		setListAdapter(adapter);
	}

	public void databinderFunction() {

		// userid = getConfigValue("userID", this);

		sqdb = databaseManager.getWritableDatabase();

		try {

			Cursor c = sqdb.rawQuery(
					"select * from addlocation Where status in(1,3)", null);

			int count = c.getCount();
			if (count > 0) {
				c.moveToFirst();
				for (int i = 0; i < count; i++) {

					int locId = c
							.getInt(c.getColumnIndexOrThrow("location_id"));
					locid.add(locId);

					String areaname = c.getString(c
							.getColumnIndexOrThrow("location_name"));
					locationName.add(areaname);

					String areatype = c.getString(c
							.getColumnIndexOrThrow("area_type"));
					areaType.add(areatype);

					String metrotype = c.getString(c
							.getColumnIndexOrThrow("metro_type"));
					metroType.add(metrotype);

					int Status = c.getInt(c.getColumnIndexOrThrow("status"));
					status.add(Status);

					String CheckStatus = c.getString(c
							.getColumnIndexOrThrow("reason"));
					checkstatus.add(CheckStatus);

					int server_id = c.getInt(c
							.getColumnIndexOrThrow("server_id"));
					serverid.add(server_id);
					c.moveToNext();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
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

	private void populateList() {
		int size = locationName.size();

		for (int k = 0; k < size; k++) {
			HashMap map = new HashMap();
			try {

				map.put("Location name", locationName.get(k).toString());
				map
						.put("Area type", get_area_type.get(exp_area_id
								.indexOf(Integer.parseInt(areaType.get(k)
										.toString()))));
				map.put("Metro type", get_metro_type
						.get(exp_metro_id.indexOf(Integer.parseInt(metroType
								.get(k).toString()))));
				map.put("Status", status.get(k).toString());

				list.add(map);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	/*
	 * public void getSeedValue() { try { userid = getConfigValue("userID",
	 * this); String token = getConfigValue("token", this); String deviceID =
	 * getConfigValue("deviceID", this);
	 * 
	 * } catch (Exception ex) { Log.d("SOAP ERROR",
	 * "Error fetching auth details from seed pp"); } }
	 */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		clearArrayList();

		databinderFunction();
		binddataareatype();
		binddatametrotype();
		populateList();

		areaadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, get_area_type);
		areaadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		metroadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, get_metro_type);
		metroadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public void clearData() {
		locationName.clear();
		areaType.clear();
		metroType.clear();
		status.clear();
	}

	public void clearArrayList() {
		areaadapter.clear();
		metroadapter.clear();
		exp_metro_id.clear();
		get_metro_type.clear();
		get_area_type.clear();
		exp_area_id.clear();
		list.clear();

		status.clear();
		locationName.clear();
		locid.clear();
		serverid.clear();
		areaType.clear();
		metroType.clear();
		checkstatus.clear();
	}

	public void binddataareatype() {
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

	public void binddatametrotype() {
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

}
