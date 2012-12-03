package in.globalspace.android.expansemodule;

import in.globalspace.android.dbclass.DataHelperClass;
import in.globalspace.android.expensereport.R;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

public class Expensesummary extends Activity {

	final private int SELECT_DATE = 1;
	private boolean fromtoflag;
	private Button btnfromdate, btntodate, btnsearch, btnshow, btnclear,
			btnsubmit;
	private Spinner spnMonth;
	private ViewGroup insertPoint;
	private int rowCounter = 0;
	private CheckBox chkAll;
	private String[] monthList = new String[] { "Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec" };
	private int year ;
	private Calendar calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_summary);
		
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		
		spnMonth = (Spinner) findViewById(R.id.spnmonth);

		ArrayAdapter<String> monthName = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				monthList);
		monthName
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spnMonth.setAdapter(monthName);

		btnsearch = (Button) findViewById(R.id.btnsearch);
		btnsubmit = (Button) findViewById(R.id.btnsubmit);
		btnclear = (Button) findViewById(R.id.btnclear);

		chkAll = (CheckBox) findViewById(R.id.checkAll);
		insertPoint = (ViewGroup) findViewById(R.id.exp_sum_cont);

		btnsearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String selectedMonth = String.valueOf(spnMonth.getSelectedItemPosition()+1); 
				String fromDate = year+"-"+selectedMonth+"-1";
				String toDate = year+"-"+selectedMonth+"-30";
				resetPage();
				DataHelperClass DHC = new DataHelperClass(
						getApplicationContext());
				String[][] displayDataDates = DHC.getExpesesDates_summary(
						fromDate, toDate);
				displayData(displayDataDates, DHC);
			}
		});

		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < rowCounter; i++) {
					View getV = insertPoint.getChildAt(i);
					CheckBox chkb = (CheckBox) getV
							.findViewById(R.id.chkdata_row);
					if (chkb.isChecked()) {

					}
				}
			}
		});

		btnclear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetPage();
			}
		});

		chkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checkall(isChecked);
			}
		});
		displayDefault();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		displayDefault();
	}

	public void displayDefault() {
		resetPage();
		DataHelperClass DHC = new DataHelperClass(getApplicationContext());
		String[][] displayDataDates = DHC.getExpesesDates_summary("", "");
		displayData(displayDataDates, DHC);
	}

	public void resetPage() {
		insertPoint.removeAllViews();
		rowCounter = 0;
	}

	public void checkall(boolean ischecked) {
		for (int i = 0; i < rowCounter; i++) {
			View getV = insertPoint.getChildAt(i);
			CheckBox chkb = (CheckBox) getV.findViewById(R.id.chkdata_row);
			chkb.setChecked(ischecked);
		}
	}

	public void displayData(String[][] eData, DataHelperClass DHC) {
		try {
			if (eData != null) {
				for (int i = 0; i < eData.length; i++) {
					try {
						String[] tString = eData[i];
						String[][] tStringData = DHC
								.getExpenseSummaryData(tString[0]);

						LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View v = vi.inflate(
								R.layout.addnew_expense_summery_row, null);

						TextView txtedate = (TextView) v
								.findViewById(R.id.txtdate);
						TextView txttravelAllow = (TextView) v
								.findViewById(R.id.txttarvelAllow);
						TextView txtdaAllow = (TextView) v
								.findViewById(R.id.txtdaAllow);
						TextView txtotherAllow = (TextView) v
								.findViewById(R.id.txtotherAllow);
						TextView txttotalAllow = (TextView) v
								.findViewById(R.id.txttotalAllow);
						CheckBox chkrow = (CheckBox) v
								.findViewById(R.id.chkdata_row);

						txtedate.setText(tString[1]);
						txttravelAllow.setText(tStringData[0][2]);
						txtdaAllow.setText(tStringData[1][2]);
						txtotherAllow.setText(tStringData[2][2]);
						int total = Integer.parseInt(tStringData[0][2])
								+ Integer.parseInt(tStringData[1][2])
								+ Integer.parseInt(tStringData[2][2]);
						txttotalAllow.setText(String.valueOf(total));
						chkrow.setTag(tString[0]);

						// chkrow.setHint(tString[13]);
						insertPoint.addView(v, rowCounter,
								new ViewGroup.LayoutParams(
										ViewGroup.LayoutParams.FILL_PARENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
						rowCounter++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
