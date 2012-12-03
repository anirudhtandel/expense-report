package in.globalspace.android.expansemodule;

import in.globalspace.android.constants.GreatRepConstants;
import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.expensereport.R;
import in.globalspace.android.expensereport.ViewUtils;
import in.globalspace.android.soapservice.model.SOAPNameValuePair;
import in.globalspace.android.soapservice.model.SOAPRequestHelper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ApproveRequest extends Activity {

	ListView listView;
	Typeface font1;
	ViewGroup rootVIew, rootView1;
	ArrayAdapter<Model> adapter;
	static Context dContext;
	EditText rejectstatus;
	Button Selectall, approve, reject;
	public static boolean Selected = true;
	SyncManagerDBAdapter databaseManager;
	SQLiteDatabase sqdb;
	int s4;
	int getpositionids;

	String getRejectedStatus = "";
	List<Model> list = new ArrayList<Model>();
	ArrayList<Integer> pos = new ArrayList<Integer>();
	ArrayList<Integer> locatioid = new ArrayList<Integer>();

	ArrayList<String> requestedBy = new ArrayList<String>();

	ArrayList<String> positionName = new ArrayList<String>();
	ArrayList<Integer> positionid = new ArrayList<Integer>();
	ArrayList<Integer> getPositionid = new ArrayList<Integer>();
	ArrayList<String> getServerid = new ArrayList<String>();
	ArrayList<Integer> getStatus = new ArrayList<Integer>();

	ArrayList<String> Areaname = new ArrayList<String>();
	ArrayList<String> Areatype = new ArrayList<String>();
	ArrayList<String> Metrotype = new ArrayList<String>();
	ArrayList<Integer> Status = new ArrayList<Integer>();
	ArrayList<String> serverid = new ArrayList<String>();

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.approvelist_view);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		dContext = this;
		databaseManager = SyncManagerDBAdapter.getSharedObject(dContext);
		listView = (ListView) findViewById(R.id.my_list);
		Selectall = (Button) findViewById(R.id.Selectall);
		approve = (Button) findViewById(R.id.approve);
		reject = (Button) findViewById(R.id.reject);

		rootVIew = (ViewGroup) findViewById(R.id.approveview_root);
		font1 = Typeface.createFromAsset(getAssets(), "MyriadPro-Cond.ttf");
		ViewUtils.setTypeFaceForChilds(rootVIew, font1);

		getvalue();

		adapter = new MyAdapter(this, getModel());
		listView.setAdapter(adapter);

		Selectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Selected) {
					for (Model m : list) {
						m.setSelected(true);
					}
					adapter.notifyDataSetChanged();
					listView.setAdapter(adapter);
					Selectall.setText("UnSelect All");
					Selected = false;
				} else {
					for (Model m : list) {
						m.setSelected(false);
					}
					adapter.notifyDataSetChanged();
					listView.setAdapter(adapter);
					Selectall.setText("SELECT ALL");
					Selected = true;
				}

			}
		});

		approve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sqdb = databaseManager.getWritableDatabase();
				sqdb.beginTransaction();
				getSelectedValues();
				getPositionid.clear();
				for (int i = 0; i < pos.size(); i++) {
					int s1 = pos.get(i);

					String getserverid = serverid.get(s1).toString();
					getServerid.add(getserverid);
					int getpositionid = positionid.get(s1);
					getPositionid.add(getpositionid);
					adapter.remove(adapter.getItem(s1 - i));
				}
				sendRequest(getServerid);
				for (int j = 0; j < getPositionid.size(); j++) {

					positionid.remove(getPositionid.get(j));
					
					sqdb.delete("approverequest", "position_id=?" , new String[]{getPositionid.get(j).toString()});
				}
				sqdb.setTransactionSuccessful();
				sqdb.endTransaction();
				sqdb.close();
				databaseManager.close();
				getPositionIdValue();
			}
		});

		reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			int posistions = 	getSelectedValues();
				if (pos.size() > 1) {
					Toast.makeText(dContext, "Please Select One Value",
							Toast.LENGTH_SHORT).show();
				} else {

					final String rejectserverid = serverid.get(posistions);
					s4 = positionid.get(pos.get(0));

					Log.e("Position value", "" + s4);

					final Dialog dialog = new Dialog(getParent());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialogcustom);

					Typeface face = Typeface.createFromAsset(getAssets(),
							"UbuntuCondensed-Regular.ttf");

					rejectstatus = (EditText) dialog
							.findViewById(R.id.rejected);
					Button dialogbuttonoK = (Button) dialog
							.findViewById(R.id.dialogbuttonoK);
					dialogbuttonoK.setTypeface(face);
					rejectstatus.setTypeface(face);

					dialogbuttonoK.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							getRejectedStatus = rejectstatus.getText()
									.toString();

							if (getRejectedStatus == ""
									|| getRejectedStatus.equals("")) {
								Toast.makeText(dContext,
										"Please Enter Valid Reason",
										Toast.LENGTH_SHORT).show();
							} else {

								sendDeletionRequest(rejectserverid,
										getRejectedStatus);
								sqdb = databaseManager.getWritableDatabase();
								sqdb.beginTransaction();
								sqdb.delete("approverequest", "position_id"
										+ "=" + s4, null);
								sqdb.setTransactionSuccessful();
								sqdb.endTransaction();
								sqdb.close();
								databaseManager.close();
								adapter.remove(adapter.getItem(pos.get(0)));
								adapter.notifyDataSetChanged();
								dialog.dismiss();
								getvalue();

								// getPositionIdValue();
							}

						}

					});
					dialog.show();

				}
			}
		});

	}

	public void getPositionIdValue() {
		sqdb = databaseManager.getWritableDatabase();
		positionid.clear();

		Cursor c = sqdb.query("approverequest", null, null, null, null, null,
				null);
		int count = c.getCount();
		if (count > 0) {
			c.moveToFirst();
			for (int i = 0; i < count; i++) {

				int positionid1 = c.getInt(c
						.getColumnIndexOrThrow("position_id"));
				positionid.add(positionid1);
				c.moveToNext();
			}
		}
	}

	public void sendDeletionRequest(String serverid, String rejectedReason) {

		String sendStrings = new String();
		String rereason =  "3^" + serverid + "^" + rejectedReason;

		sendStrings = "<?xml version='1.0' encoding='UTF-8'?><apprved_are_data><auth><token_id>{$tokenAuth}</token_id>"
				+ "<user_id>{$user_id}</user_id>"
				+ "</auth><plac_approved>"
				+ rereason
				+ "</plac_approved></apprved_are_data>";
		
		Log.e("rejectedxml ", sendStrings);

		Send(sendStrings, dContext);
		// addExpenseReport.Send(sendStrings, dContext);
	}

	public void getvalue() {
		sqdb = databaseManager.getWritableDatabase();
		cleararray();

		Cursor c = sqdb.query("approverequest", null, null, null, null, null,
				null);
		int count = c.getCount();

		if (count > 0) {
			c.moveToFirst();
			for (int i = 0; i < count; i++) {

				int positionid1 = c.getInt(c
						.getColumnIndexOrThrow("position_id"));
				positionid.add(positionid1);
				String requestedby = c.getString(c
						.getColumnIndexOrThrow("requestedby_id"));
				requestedBy.add(requestedby);

				String posname = c.getString(c
						.getColumnIndexOrThrow("position_name"));
				positionName.add(posname);

				String areaName = c.getString(c
						.getColumnIndexOrThrow("area_name"));
				Areaname.add(areaName);

				String areaType = c.getString(c
						.getColumnIndexOrThrow("area_type"));
				Areatype.add(areaType);

				String metrotype = c.getString(c
						.getColumnIndexOrThrow("metro_type"));
				Metrotype.add(metrotype);

				String serverid1 = c.getString(c
						.getColumnIndexOrThrow("server_id"));
				serverid.add(serverid1);

				/*
				 * int status =
				 * c.getInt(c.getColumnIndexOrThrow("approve_flag"));
				 * Status.add(status);
				 */

				c.moveToNext();
			}
		} else {

		}

	}

	private List<Model> getModel() {

		for (int j = 0; j < positionid.size(); j++)

			list.add(new Model(requestedBy.get(j).toString(), positionName.get(
					j).toString(), Areaname.get(j).toString(), Areatype.get(j)
					.toString(), Metrotype.get(j).toString()));

		return list;
	}

	public int getSelectedValues() {
		int position = 0;

		pos.clear();

		for (Model m : list) {
			if (m.getSelected() == true) {
				pos.add(position);

			}
			position++;

		}
		return position-1;
	}

	public void sendRequest(ArrayList<String> posId) {
		String sendStrings = new String();

		String s2 = "", s3;

		for (int i = 0; i < posId.size(); i++) {
			String id = posId.get(i).toString();

			s3 = id;
			s2 +=   "2^" + s3 + "~";

		}
		s2 = s2.substring(0, s2.length() - 1);


		
		sendStrings = "<?xml version='1.0' encoding='UTF-8'?><apprved_are_data><auth><token_id>{$tokenAuth}</token_id>"
				+ "<user_id>{$user_id}</user_id>"
				+ "</auth><plac_approved>"
				+ s2
				+ "</plac_approved></apprved_are_data>";

		Log.e("xml Value", sendStrings);
		Send(sendStrings, dContext);

	}

	public void cleararray() {
		positionid.clear();
		requestedBy.clear();
		Areaname.clear();
		Areatype.clear();
		Metrotype.clear();
		Status.clear();
	}

	public static void Send(String sendStrings, Context gctx) {
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

}
