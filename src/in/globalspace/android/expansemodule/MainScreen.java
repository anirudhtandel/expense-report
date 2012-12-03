package in.globalspace.android.expansemodule;

import in.globalspace.android.expensereport.R;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainScreen extends TabActivity {

	private ImageView titleBarIcon;
	protected TabWidget tabWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mainscreen);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.great_rep_titlebar);

		titleBarIcon = (ImageView) findViewById(R.id.great_rep_titlebar_icon);
		titleBarIcon.setImageResource(R.drawable.title_expense_report);

		setupTabs();

	}

	private void setupTabs() {
		ArrayList<GreatRepTabModel> tabs = defineTabs();
		if (tabs != null) {
			for (GreatRepTabModel current : tabs) {
				getTabHost().addTab(
						getTabHost()
								.newTabSpec(current.getTabTag())
								.setContent(current.getChildTabIntent())
								.setIndicator(
										createIndicatorView(getTabHost(),
												current.getTabTag())));
			}
		}
	}

	public ArrayList<GreatRepTabModel> defineTabs() {
		ArrayList<GreatRepTabModel> tabs = new ArrayList<GreatRepTabModel>();

		tabs.add(new GreatRepTabModel(new Intent(MainScreen.this,
				ExpensesAdd.class), R.drawable.ic_launcher)
				.setTabTag("Add Expenses"));
		tabs.add(new GreatRepTabModel(new Intent(MainScreen.this,
				Expensesummary.class), R.drawable.ic_launcher)
				.setTabTag("View Expenses"));
		tabs.add(new GreatRepTabModel(new Intent(MainScreen.this,
				ExpensesAdd.class), R.drawable.ic_launcher)
				.setTabTag("Approve Expenses"));

		tabs.add(new GreatRepTabModel(new Intent(MainScreen.this,
				AddLocationTab.class), R.drawable.ic_launcher)
				.setTabTag("Add Location"));

		return tabs;
	}

	private View createIndicatorView(TabHost tabHost, String text) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View tabIndicator = inflater.inflate(R.layout.ccr_ti,
				tabHost.getTabWidget(), false);

		final TextView textView = (TextView) tabIndicator
				.findViewById(R.id.great_rep_ti_txt);
		textView.setTextSize(17);
		textView.setText(text);

		return tabIndicator;
	}
}
