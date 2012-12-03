package in.globalspace.android.expensereport;

import in.globalspace.android.expansemodule.MainScreen;
import in.globalspace.android.model.DashBoardOthersGridAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class DashBoardActivity extends Activity {
	
	private DashBoardOthersGridAdapter dashboardAdapter;
	private ArrayList<DashBoardOthersGridAdapter.DashboardGridModel> dashboardItems;
	private GridView dashboardGrid;
	Context gcontext;
	private ImageView titleBarIcon;
	public static Typeface GREAT_REP_FONT;
	public static Typeface GREAT_REP_FONT_CONDENCED;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gcontext=this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dashboard_others);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.great_rep_titlebar);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		titleBarIcon = (ImageView) findViewById(R.id.great_rep_titlebar_icon);
		titleBarIcon.setImageResource(R.drawable.title_expense_report);
		
		dashboardGrid = (GridView) findViewById(R.id.dashboard_grid);
		dashboardItems = new ArrayList<DashBoardOthersGridAdapter.DashboardGridModel>();
		dashboardAdapter = new DashBoardOthersGridAdapter(this, dashboardItems);

		dashboardGrid.setAdapter(dashboardAdapter);
		dashboardGrid.setEnabled(false);
		
	/*	Intent placeArea = new Intent();
		placeArea.setClass(gcontext, DashBordReport.class);*/
		
		Intent ExpenseEntry=new Intent();
		ExpenseEntry.setClass(gcontext, MainScreen.class);
		
		
		/*addDashboardItems(R.drawable.ic_dashboard_history, placeArea);*/
		
		addDashboardItems(R.drawable.ic_dashboard_history, ExpenseEntry);
		
		Intent iIntent = new Intent();
		addDashboardItems(R.drawable.dashboard_download, iIntent);

	}

	private void addDashboardItems(int resourceId, Intent intent) {
		dashboardItems.add(new DashBoardOthersGridAdapter.DashboardGridModel(
				resourceId, intent));
	}
	
	public void pushdb(){
		try {
			copy(new File(Environment.getExternalStorageDirectory()
					.toString() + File.separator + "expanses_master.db"),new File(
					"/data/data/in.globalspace.android.expensereport/databases/expanses_master.db")
					);
		} catch (IOException es) {
			es.printStackTrace();
		}
	}
	
	void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
	
	private AdapterView.OnItemClickListener getDashboardClickListener() {
		return new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long rowid) {
				try {
					startActivity(dashboardItems.get(position).getIntent());
				}

				catch (ActivityNotFoundException ex) {
					ex.printStackTrace();
				}
			}
		};
	}
}
