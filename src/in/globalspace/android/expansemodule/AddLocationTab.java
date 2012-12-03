package in.globalspace.android.expansemodule;

import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.expensereport.R;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class AddLocationTab extends TabActivity {

	private ImageView titleBarIcon;
	protected TabWidget tabWidget;
	SyncManagerDBAdapter databaseManager;
	SQLiteDatabase sqdb;
	Context dContext;
	String userid = "";
	String empvalue = "";
	public static final String PROVIDER_NAME = "in.globalspace.android.configurations";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/configs");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dContext = this;
		databaseManager = SyncManagerDBAdapter.getSharedObject(dContext);
		getSeedValue();
		getuserid();

		/*
		 * requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		 * setContentView(R.layout.mainscreen);
		 * getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		 * R.layout.great_rep_titlebar);
		 * 
		 * titleBarIcon = (ImageView)
		 * findViewById(R.id.great_rep_titlebar_icon);
		 * titleBarIcon.setImageResource(R.drawable.title_planner);
		 */

		setupTabs();

	}

	private void setupTabs() {
		ArrayList<GreatRepTabModel> tabs = defineTabs();
		if (tabs != null) {
			for (GreatRepTabModel current : tabs) {
				getTabHost().addTab(
						getTabHost().newTabSpec(current.getTabTag())
								.setContent(current.getChildTabIntent())
								.setIndicator(
										createIndicatorView(getTabHost(),
												current.getTabTag())));
			}
		}
	}

	public ArrayList<GreatRepTabModel> defineTabs() {
		ArrayList<GreatRepTabModel> tabs = new ArrayList<GreatRepTabModel>();

		tabs.add(new GreatRepTabModel(new Intent(AddLocationTab.this,
				AddNewLocation.class), R.drawable.ic_launcher)
				.setTabTag("Add New Location"));

		tabs.add(new GreatRepTabModel(new Intent(AddLocationTab.this,
				Displaylocation.class), R.drawable.ic_launcher)
				.setTabTag("View/Edit"));

		tabs.add(new GreatRepTabModel(new Intent(AddLocationTab.this,
				Viewlocation.class), R.drawable.ic_launcher)
				.setTabTag("View/Submit"));

		if (!empvalue.equals("2")) {
			tabs.add(new GreatRepTabModel(new Intent(AddLocationTab.this,
					PlaceAreaApprovalRequest.class), R.drawable.ic_launcher)
					.setTabTag("Approve"));
		}
		return tabs;
	}

	private View createIndicatorView(TabHost tabHost, String text) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View tabIndicator = inflater.inflate(R.layout.ccr_ti, tabHost
				.getTabWidget(), false);

		final TextView textView = (TextView) tabIndicator
				.findViewById(R.id.great_rep_ti_txt);
		textView.setTextSize(17);
		textView.setText(text);

		return tabIndicator;
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

	public void getSeedValue() {
		try {
			userid = getConfigValue("userID", this);

		} catch (Exception ex) {
			Log.d("SOAP ERROR", "Error fetching auth details from seed pp");
		}
	}

	public void getuserid() {
		sqdb = databaseManager.getWritableDatabase();
		Cursor c = sqdb.rawQuery(
				"select approval_status from emp_detail where employee_id="
						+ userid, null);
		int count = c.getCount();
		c.moveToFirst();
		for (int i = 0; i < count; i++) {

			empvalue = c.getString(c.getColumnIndexOrThrow("approval_status"));
			c.moveToNext();
		}
		c.close();
	}
}
