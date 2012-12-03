package in.globalspace.android.model;

import in.globalspace.android.expensereport.GreatRep;
import in.globalspace.android.expensereport.R;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public abstract class GreatRepTabActivity extends TabActivity {

	private ImageView titleBarIcon;
	protected TabWidget tabWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.great_rep_tab);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.great_rep_titlebar);

		titleBarIcon = (ImageView) findViewById(R.id.great_rep_titlebar_icon);
		titleBarIcon.setImageResource(getTitleDrawableRes());

		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabWidget.setOrientation(1);

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

	private View createIndicatorView(TabHost tabHost, String text) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Typeface font1 = Typeface.createFromAsset(getAssets(),
				"MyriadPro-Cond.ttf");
		View tabIndicator = inflater.inflate(R.layout.great_rep_ti,
				tabHost.getTabWidget(), false);

		final TextView textView = (TextView) tabIndicator
				.findViewById(R.id.great_rep_ti_txt);

		textView.setTypeface(GreatRep.GREAT_REP_FONT_CONDENCED);
		textView.setTypeface(font1);

		textView.setTextColor(getResources()
				.getColor(R.color.text_color_4f4f4f));

		textView.setTextSize(20);
		textView.setText(text);

		return tabIndicator;
	}

	public abstract int getTitleDrawableRes();

	public abstract ArrayList<GreatRepTabModel> defineTabs();

	public ImageView getTitleBarIcon() {
		return titleBarIcon;
	}

}
