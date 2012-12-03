package in.globalspace.android.expansemodule;

import in.globalspace.android.dbclass.DataHelperClass;


import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.expensereport.R;
import in.globalspace.android.expensereport.ViewUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExpensesAdd extends Activity {
	private Button btnExpDate, btnExpAdd, btnExpSave, btnExpSubmit;
	private EditText txtComment,txtTotalExp,txtTotalDa,txtTotalOther;
	final private int SELECT_DATE = 1;
	private int rowCounter = 0, rowCounter_da = 0, rowCounter_oe_one = 0,
			rowCounter_oe_two = 0, rowCounter_oe_three = 0;
	private ViewGroup insertView_ta, insertView_da, insertView_oe_one,
			insertView_oe_two, insertView_oe_three;

	private ArrayAdapter<String> placeName,travelMode,tripType;
	private ArrayAdapter<String> areatype, metrotype;
	private ArrayList<String> areatype_name, metrotype_name;
	private ArrayList<Integer> areatype_id, metrotype_id;
	private String[] placeArea_id, placeArea_name,travelMode_id, travelMode_name,tripType_id, tripType_name;
	private ViewGroup rootView;
	Typeface font1;
	private String[] daTypes ,daTypesLabels,oeTypes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expanseentry);
		SyncManagerDBAdapter syncDataManager = SyncManagerDBAdapter
				.getSharedObject(getApplicationContext());
		SQLiteDatabase sqdb = syncDataManager.getWritableDatabase();
		pulldb();
		//pushdb();
		
		btnExpDate = (Button) findViewById(R.id.expanse_date);
		btnExpAdd = (Button) findViewById(R.id.btnaddrow);
		btnExpSave = (Button) findViewById(R.id.btnexpsave);
		btnExpSubmit = (Button) findViewById(R.id.btnexpsubmit);
		
		txtComment = (EditText)findViewById(R.id.txtExpComment);
		txtTotalExp = (EditText)findViewById(R.id.txtTotalExp);
		txtTotalDa = (EditText)findViewById(R.id.txtTotalDa);
		txtTotalOther = (EditText)findViewById(R.id.txtTotalOther);
		
		insertView_ta = (ViewGroup) findViewById(R.id.mainheaderlayout);
		insertView_da = (ViewGroup) findViewById(R.id.ll_da_dynamic);
		
		insertView_oe_one = (ViewGroup) findViewById(R.id.vl_other_one);
		insertView_oe_two = (ViewGroup) findViewById(R.id.vl_other_two);
		insertView_oe_three = (ViewGroup) findViewById(R.id.vl_other_three);		

		btnExpAdd.setOnClickListener(expAddListner);
		btnExpDate.setOnClickListener(expDateListner);
		btnExpSave.setOnClickListener(expSaveListner);
		btnExpSubmit.setOnClickListener(expSubmitListner);
		
		daTypes = new String[] { "1", "2"};
		oeTypes = new String[] { "3"};
		daTypesLabels = new String[] { "City", "Area"};
				
		load_da_dynamic();
		load_oe_dynamic();
		fetchDataforSpinners();		
		rootView = (ViewGroup) findViewById(R.id.expenseview_root);
		font1 = Typeface.createFromAsset(getAssets(), "MyriadPro-Cond.ttf");
		ViewUtils.setTypeFaceForChilds(rootView, font1);		
	}
	
	public void fetchDataforSpinners(){
		DataHelperClass DHC = new DataHelperClass(getApplicationContext());
		String[][] placeAreaData = DHC.getPlaceArea("");
		placeArea_id=placeAreaData[0];
		placeArea_name=placeAreaData[1];
		placeName = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_item, placeArea_name);
		placeName
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		
		String[][] travelModeData = DHC.getTravelMode("");
		travelMode_id=travelModeData[0];
		travelMode_name=travelModeData[1];
		travelMode = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_item, travelMode_name);
		travelMode
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		String[][] tripTypeDate = DHC.getTripType("");
		tripType_id=tripTypeDate[0];
		tripType_name=tripTypeDate[1];
		tripType = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_item, tripType_name);
		tripType
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}
	
	

	OnClickListener expDateListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(SELECT_DATE);
		}
	};

	OnClickListener expAddListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			newRow();
		}
	};

	public void newRow() {
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.addnew_expanses_row, null);
		ViewGroup layoutRootView = (ViewGroup) v.findViewById(R.id.newrow);
		ViewUtils.setTypeFaceForChilds(layoutRootView, font1);
		Spinner txtfrom = (Spinner) v.findViewById(R.id.txtfrom);
		txtfrom.setAdapter(placeName);
		Spinner txtto = (Spinner) v.findViewById(R.id.txtto);
		txtto.setAdapter(placeName);
		Spinner spntravelmode = (Spinner) v.findViewById(R.id.spnmode);
		spntravelmode.setAdapter(travelMode);
		Spinner spntype = (Spinner) v.findViewById(R.id.spntype);
		spntype.setAdapter(tripType);

		EditText txttotalkm = (EditText) v.findViewById(R.id.txttotalkm);

		txttotalkm.setOnFocusChangeListener(onfcschange);
		txttotalkm.setTag(rowCounter);
		insertView_ta.addView(v, rowCounter, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		rowCounter++;
	}

	OnClickListener expSaveListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onSaveFunctionality();
		}
	};

	OnClickListener expSubmitListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Submit Button",
					Toast.LENGTH_SHORT).show();
		}
	};

	OnFocusChangeListener onfcschange = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			int indexC = Integer.parseInt(v.getTag().toString());
			EditText travelDist = (EditText) v;
			try {
				int tDistance = Integer.parseInt(travelDist.getText()
						.toString());
				// calculateExpanse(tDistance, 2, indexC);
			} catch (Exception e) {
			}
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SELECT_DATE:
			return new DatePickerDialog(this, dateListener, 2010, 1, 1);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int yr, int monthOfYear,
				int dayOfMonth) {			
			String setdate = String.valueOf(yr) + "-"
					+ String.valueOf(monthOfYear + 1) + "-"
					+ String.valueOf(dayOfMonth);
			btnExpDate.setText(setdate);
		}
	};

	public void onSaveFunctionality(){
		try{
			DataHelperClass DHC = new DataHelperClass(getApplicationContext());
			String exp_Date 	= btnExpDate.getText().toString();			
			String exp_Comment 	= txtComment.getText().toString();
			
			String exp_travel 	= txtTotalExp.getText().toString();			
			String exp_da 		= txtTotalDa.getText().toString();
			String exp_other 	= txtTotalOther.getText().toString();
			
			int expHeadId = DHC.InsertExpenseHeadData(new String[]{"1",exp_Date,"0",exp_Comment});
			
			int expDetailTA_id = DHC.InsertExpenseDetailData(new String[]{String.valueOf(expHeadId),"2",exp_travel});
			int expDetailDA_id = DHC.InsertExpenseDetailData(new String[]{String.valueOf(expHeadId),"6",exp_da});
			int expDetailOE_id = DHC.InsertExpenseDetailData(new String[]{String.valueOf(expHeadId),"11",exp_other});
			
			
			//exp_rep_dtl_localKey,expense_policy_id,location_from,location_to,km_travelled
			//transport_mode_id,trip_type_id,ticket_number,amount
			
			String[][] travelAllows = new String[rowCounter][];
			for (int iC = 0; iC < rowCounter; iC++) {
				View getV = (View)insertView_ta.getChildAt(iC);
				travelAllows[iC]=getTravelRowsdata(getV);				
				DHC.InsertExpenseReportData(new String[]{String.valueOf(expDetailTA_id),"1",travelAllows[iC][0],travelAllows[iC][1],travelAllows[iC][4],travelAllows[iC][2],travelAllows[iC][3],"",travelAllows[iC][5]});
			}
			
			
			
			String[] daAllows =  new String[daTypes.length];
			daAllows = getDaRowsData();
			
			//exp_rep_dtl_localKey,expense_policy_id,location_from,location_to,km_travelled
			//transport_mode_id,trip_type_id,ticket_number,amount
			
			for(int iC=0;iC<daAllows.length;iC++){
				DHC.InsertExpenseReportData(new String[]{String.valueOf(expDetailDA_id),"2","","","","","","","100"});
			}
			
			
			String[] oeAllows =  new String[rowCounter_oe_one+rowCounter_oe_two+rowCounter_oe_three];
			String[] oeAllows_one =  new String[rowCounter_oe_one];
			String[] oeAllows_two =  new String[rowCounter_oe_two];
			String[] oeAllows_three =  new String[rowCounter_oe_three];
			
			oeAllows_one=getOeRowsData(rowCounter_oe_one,insertView_oe_one);
			oeAllows_two=getOeRowsData(rowCounter_oe_two,insertView_oe_two);
			oeAllows_three=getOeRowsData(rowCounter_oe_three,insertView_oe_three);
			
			for(int iC=0;iC<oeAllows_one.length;iC++){
				oeAllows[iC]=oeAllows_one[iC];
			}
			
			for(int iC=0;iC<oeAllows_two.length;iC++){
				oeAllows[iC]=oeAllows_two[oeAllows_one.length+iC];
			}
			
			for(int iC=0;iC<oeAllows_three.length;iC++){
				oeAllows[iC]=oeAllows_three[oeAllows_two.length+iC];
			}			
			
			
			//exp_rep_dtl_localKey,expense_policy_id,location_from,location_to,km_travelled
			//transport_mode_id,trip_type_id,ticket_number,amount
			for(int iC=0;iC<oeAllows.length;iC++){
				DHC.InsertExpenseReportData(new String[]{String.valueOf(expDetailOE_id),"3","","","","","","",oeAllows[iC].split("~")[0]});
			}
			
			String outputStirng ="Travel Rows"+rowCounter;
			outputStirng+="\t  DA Rows"+rowCounter_da;
			outputStirng+="\t  OE Rows"+rowCounter_oe_one;
			outputStirng+="\t  OE Rows"+rowCounter_oe_two;
			outputStirng+="\t  OE Rows"+rowCounter_oe_three;
			Toast.makeText(getApplicationContext(), outputStirng, Toast.LENGTH_SHORT).show();
		}catch(Exception e)
		{
			e.printStackTrace();
			}
	}
	
	public String[] getTravelRowsdata(View getV) {
		String strFrom,strTo,strMode,strType,strTotlKm,strTotalAmnt="";
		String[] returnList = new String[6];
		try{
			Spinner txtfrom = (Spinner) getV.findViewById(R.id.txtfrom);
			Spinner txtto = (Spinner) getV.findViewById(R.id.txtto);
			Spinner spntravelmode = (Spinner) getV.findViewById(R.id.spnmode);	
			Spinner spntype = (Spinner) getV.findViewById(R.id.spntype);
			EditText txttotalkm = (EditText) getV.findViewById(R.id.txttotalkm);
			EditText txttravelallow = (EditText) getV.findViewById(R.id.txttravelallow);

			strFrom = txtfrom.getSelectedItem().toString();
			strTo = txtto.getSelectedItem().toString();
			strMode = spntravelmode.getSelectedItem().toString();
			strType = spntype.getSelectedItem().toString();
			strTotlKm = txttotalkm.getText().toString();
			strTotalAmnt = txttravelallow.getText().toString();
			
			int strFrom_pos = searchIndexOfArray(strFrom, placeArea_name);
			int strTo_pos = searchIndexOfArray(strTo, placeArea_name);
			int strMode_pos = searchIndexOfArray(strMode, travelMode_name);
			int strType_pos = searchIndexOfArray(strType, tripType_name);
			
			strFrom = placeArea_id[strFrom_pos];
			strTo = placeArea_id[strTo_pos];
			strMode = travelMode_id[strMode_pos];
			strType = tripType_id[strType_pos];			

			returnList[0]=strFrom;
			returnList[1]=strTo;
			returnList[2]=strMode;
			returnList[3]=strType;
			returnList[4]=strTotlKm;
			returnList[5]=strTotalAmnt;

		}catch(Exception e){
		e.printStackTrace();	
		}
				
		return returnList;
	}
	
	public String[] getDaRowsData(){
		String[] returnList = new String[daTypes.length];
		try{
			DataHelperClass DHC = new DataHelperClass(getApplicationContext());
			String daTypeSelected="";			
			for (int iC = 0; iC < daTypes.length; iC++) {
				String[][] daTypeData = DHC.getDaDatas(daTypes[iC]);
				View getV = (View)insertView_da.getChildAt(iC);
				Spinner daType = (Spinner) getV.findViewById(R.id.daType);
				daTypeSelected = daType.getSelectedItem().toString();
				int daType_pos = searchIndexOfArray(daTypeSelected, daTypeData[1]);
				daTypeSelected = daTypeData[0][daType_pos];
				returnList[iC] = daTypeSelected;
			}
		}catch(Exception e){	
			e.printStackTrace();
		}
		return returnList;
	}
	
	public String[] getOeRowsData(int rowcounter,ViewGroup vG){
		String[] returnList = new String[rowcounter];
		String idValue="";
		try{
			for (int iC = 0; iC < rowcounter; iC++) {
				View otherView = vG.getChildAt(iC);
				EditText oeTypeValue = (EditText) otherView
						.findViewById(R.id.txtexp_otherValue);				
				idValue = oeTypeValue.getText().toString();
				idValue += idValue+"~"+oeTypeValue.getTag().toString();
				returnList[iC]=idValue;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnList;
	}
	
	public void load_da_dynamic() {
		try {
			DataHelperClass DHC = new DataHelperClass(getApplicationContext());			

			for (int iC = 0; iC < daTypes.length; iC++) {
				String[][] daTypeData = DHC.getDaDatas(daTypes[iC]);
				ArrayAdapter<String> daAdapter = new ArrayAdapter<String>(
						getApplicationContext(),
						android.R.layout.simple_spinner_item, daTypeData[1]);
				
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = vi.inflate(R.layout.addnew_expense_da_data, null);				
				ViewGroup layoutRootView = (ViewGroup) v.findViewById(R.id.newdaRow);
				ViewUtils.setTypeFaceForChilds(layoutRootView, font1);				
				daAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				TextView daTypeLabel = (TextView) v.findViewById(R.id.daTypeLabel);
				
				daTypeLabel.setText(daTypesLabels[iC]);
				Spinner daType = (Spinner) v.findViewById(R.id.daType);
				daType.setAdapter(daAdapter);

				daTypeLabel.setTag(rowCounter_da);
				insertView_da.addView(v, rowCounter_da,
						new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.FILL_PARENT,
								ViewGroup.LayoutParams.WRAP_CONTENT));
				rowCounter_da++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load_oe_dynamic() {
		try {
			DataHelperClass DHC = new DataHelperClass(getApplicationContext());			
			int vCounter = 0, lCounter = 0;
			String[][] daTypeData = DHC.getDaDatas(oeTypes[0]);

			for (int iC = 0; iC < daTypeData[1].length; iC++) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View vl = vi.inflate(R.layout.otherexpenses_view, null);
				
				ViewGroup layoutRootView = (ViewGroup) vl.findViewById(R.id.newOeRow);
				ViewUtils.setTypeFaceForChilds(layoutRootView, font1);
				
				if (lCounter <= daTypeData[1].length) {{
					vCounter = lCounter;
					if (vCounter <= daTypeData[1].length) {
						TextView oeTypeLabel = (TextView) vl
								.findViewById(R.id.txtexp_otherLabel);
						oeTypeLabel.setText(daTypeData[1][vCounter]);
						EditText oeTypeValue = (EditText) vl
								.findViewById(R.id.txtexp_otherValue);
						oeTypeValue.setText("");
						oeTypeValue.setTag(daTypeData[0][iC]);
						oeTypeLabel.setTag(rowCounter_oe_one);
						insertView_oe_one.addView(vl, rowCounter_oe_one,
								new ViewGroup.LayoutParams(
										ViewGroup.LayoutParams.FILL_PARENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
						rowCounter_oe_one++;
						vCounter = vCounter + 1;
						if (vCounter <= daTypeData[1].length) {
							vl = vi.inflate(R.layout.otherexpenses_view, null);
							oeTypeLabel = (TextView) vl
									.findViewById(R.id.txtexp_otherLabel);
							oeTypeLabel.setText(daTypeData[1][vCounter]);
							oeTypeValue = (EditText) vl
									.findViewById(R.id.txtexp_otherValue);
							oeTypeValue.setText("");
							oeTypeValue.setTag(daTypeData[0][iC]);
							oeTypeLabel.setTag(rowCounter_oe_two);
							insertView_oe_two
									.addView(
											vl,
											rowCounter_oe_two,
											new ViewGroup.LayoutParams(
													ViewGroup.LayoutParams.FILL_PARENT,
													ViewGroup.LayoutParams.WRAP_CONTENT));
							rowCounter_oe_two++;
							vCounter = vCounter + 1;
							if (vCounter <= daTypeData[1].length) {
								vl = vi.inflate(R.layout.otherexpenses_view,
										null);
								oeTypeLabel = (TextView) vl
										.findViewById(R.id.txtexp_otherLabel);
								oeTypeLabel.setText(daTypeData[1][vCounter]);
								oeTypeValue = (EditText) vl
										.findViewById(R.id.txtexp_otherValue);
								oeTypeValue.setText("");
								oeTypeValue.setTag(daTypeData[0][iC]);
								oeTypeLabel.setTag(rowCounter_oe_three);
								insertView_oe_three
										.addView(
												vl,
												rowCounter_oe_three,
												new ViewGroup.LayoutParams(
														ViewGroup.LayoutParams.FILL_PARENT,
														ViewGroup.LayoutParams.WRAP_CONTENT));
								rowCounter_oe_three++;
							}
						}
					}
				}
				lCounter = lCounter + 3;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pushdb() {
		try {
			copy(new File(Environment.getExternalStorageDirectory().toString()
					+ File.separator + "expanses_master.db"),
					new File(
							"/data/data/in.globalspace.android.expensereport/databases/expanses_master.db"));
		} catch (IOException es) {
			es.printStackTrace();
		}
	}
	
	public void pulldb() {
		try {
			copy(new File(
							"/data/data/in.globalspace.android.expensereport/databases/expanses_master.db"),new File(Environment.getExternalStorageDirectory().toString()
					+ File.separator + "expanses_master.db"));
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
	
	public int searchIndexOfArray(String searchValue,String[] searchArray){
		int returnPos=-1;
		for(int i=0;i<searchArray.length;i++){
			if(searchArray[i].equals(searchValue))
				returnPos=i;
		}
		return returnPos;
	}
}
