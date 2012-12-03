package in.globalspace.android.syncparse;

import in.globalspace.android.dbclass.SyncManagerDBAdapter;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ExpanseParser {
	
	SyncManagerDBAdapter databaseManager;
	Context context;
	private boolean[] updateFlag;
	public boolean ParseXML(String xmlContent, Context context,
			String requestType) {
		this.context = context;
		this.updateFlag = new boolean[16];
		if (xmlContent.length() > 0) {

			databaseManager = SyncManagerDBAdapter.getSharedObject(context);
			SQLiteDatabase sqdb = databaseManager.getWritableDatabase();
			try {

				if (requestType.equals("all")) {					
					
					try {
						if (xmlContent.contains("</plac_are_data>")) {
							ArrayList<String> placeArray = Retrieve_placeArea_Tag("<plac_view></plac_view>"	+ xmlContent);
							for (int i = 0; i < placeArray.size(); i++) {
								String replacestr = placeArray.get(i).toString();
								ParsePlaceAreaData(replacestr,sqdb);
							}
							placeArray.clear();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (xmlContent.contains("</plac_are_approval>")) {
							
							ArrayList<String> placeArray = Retrieve_placeAreaApprv_Tag("<ta_avp></ta_avp>"	+ xmlContent);
							for (int i = 0; i < placeArray.size(); i++) {
								String replacestr = placeArray.get(i).toString();
								ParsePlaceAreaApproval(replacestr,sqdb);
							}
							placeArray.clear();
							
							//ParsePlaceAreaApproval(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					try {
						if (xmlContent.contains("</expense_policy>")) {
							ParseExpansePolicy(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						if (xmlContent.contains("</status>")) {
							ParseStatus(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						if (xmlContent.contains("</travel_mode>")) {
							ParseTravelMode(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						if (xmlContent.contains("</pr_gr_dtl>")) {
							ParsePeerGroup(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						if (xmlContent.contains("</group_type>")) {
							Parsegrouptype(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						if (xmlContent.contains("</expense_type_master>")) {
							ParseExpanseType(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						if (xmlContent.contains("</trip_type>")) {
							ParseTripType(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}					

					try {
						if (xmlContent.contains("</exp_rep_resp>")) {
							ParseExpRepResp(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						if (xmlContent.contains("</exp_rep_approval>")) {
							ParseExpRepApproval(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					try {
						if (xmlContent.contains("</rel_exp_grp>")) {
							ParseRelExpGrp(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						if (xmlContent.contains("</expense_master>")) {
							ParseExpenseMaster(xmlContent,sqdb);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					ContentValues cv = new ContentValues();
					long now = System.currentTimeMillis();
					String tDate = "";
					SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					tDate = df2.format(now);
					cv.put("synctimestamp", tDate);					
					updateTimeStamp(cv,sqdb);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		} else {
			return false;
		}
	}

//Parsing and storing contact entries starts from here
	
	private ArrayList<String> Retrieve_placeArea_Tag(String xmlContent) {
		Pattern regex = Pattern.compile("<plac_view>(.*?)</plac_view>",
				Pattern.DOTALL);
		Matcher matcher = regex.matcher(xmlContent);
		ArrayList<String> contactsArray = new ArrayList<String>();
		if (matcher.find()) {
			while (matcher.find()) {
				contactsArray.add((matcher.group(1)));
			}
		}
		return contactsArray;
	}
	
	private ArrayList<String> Retrieve_placeAreaApprv_Tag(String xmlContent) {
		Pattern regex = Pattern.compile("<ta_avp>(.*?)</ta_avp>",
				Pattern.DOTALL);
		Matcher matcher = regex.matcher(xmlContent);
		ArrayList<String> contactsArray = new ArrayList<String>();
		if (matcher.find()) {
			while (matcher.find()) {
				contactsArray.add((matcher.group(1)));
			}
		}
		return contactsArray;
	}
	
	public void updateTimeStamp(ContentValues cv,SQLiteDatabase sqdb){
		/*if(fileTable){
			try{					
				updateSyncTime(cv,"FileMaster",sqdb);
				fileTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}			
		if(contTable){
			try{
				updateSyncTime(cv,"ContactTable",sqdb);
				contTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		
		if(patchTable){
			try{					
				updateSyncTime(cv,"Patch",sqdb);
				patchTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}			
		if(qlyTable){
			try{
				updateSyncTime(cv,"Qualification",sqdb);
				qlyTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		
		if(specTable){
			try{					
				updateSyncTime(cv,"Speciality",sqdb);
				specTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}			
		if(focuTable){
			try{
				updateSyncTime(cv,"FocusBrand",sqdb);
				focuTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		
		if(brandTable){
			try{					
				updateSyncTime(cv,"BrandMaster",sqdb);
				brandTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}			
		if(activityTable){
			try{
				updateSyncTime(cv,"ActivityMaster",sqdb);
				activityTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		
		if(patchTable){
			try{
				updateSyncTime(cv,"MapLocation",sqdb);
				patchTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		
		if(schedTable){
			try{					
				updateSyncTime(cv,"ScheduleTable",sqdb);
				schedTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}			
		if(pmTable){
			try{
				updateSyncTime(cv,"PAMTable",sqdb);
				pmTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		
		if(productTable){
			try{					
				updateSyncTime(cv,"ProductTable",sqdb);
				productTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}			
		if(pearTable){
			try{
				updateSyncTime(cv,"PeerGroupTable",sqdb);
				pearTable=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}
		if(productMaster){
			try{
				updateSyncTime(cv,"ProductMaster",sqdb);
				productMaster=false;
			}catch(Exception e){
				e.printStackTrace();
			}				
		}*/
	}
	
	
	public void updateSyncTime(ContentValues cv,String syncedTable,SQLiteDatabase sqdb){
		
		sqdb.beginTransaction();
		try {			 
			sqdb.update("syncTimeTable", cv, "tableName=?",new String[]{syncedTable});				
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}

		sqdb.setTransactionSuccessful();
		sqdb.endTransaction();
		
	}
	
	
	
	
	
	private void ParseExpRepResp(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areacatNodes = doc.getElementsByTagName("exp_rep_resp");
			int getcountExpRepResp = areacatNodes.getLength();

			for (int i = 0; i < getcountExpRepResp; i++) {

				Element element = (Element) areacatNodes.item(i);
				
				NodeList nL = element.getElementsByTagName("resp_date");
				if(nL.getLength()>0){
					for (int j = 0; j < nL.getLength(); j++) {
						element = (Element) nL.item(j);
						
						String[] ta_string=getElementValues("ta", "ta_view", element);						
						String[] da_string=getElementValues("da", "da_view", element);						
						String[] oe_string=getElementValues("oe", "oe_view", element);	
						
						for(int tadata=0;tadata<ta_string.length;tadata++){							
							String ta_breakDown[] = ta_string[tadata].split("~");
							StoreExpRepRespTa("2","1",ta_breakDown,sqdb);							
						}
						
						for(int dadata=0;dadata<da_string.length;dadata++){
							String da_breakDown[] = da_string[dadata].split("~");
							StoreExpRepResp("6","1",da_breakDown,sqdb);
						}
						
						for(int oedata=0;oedata<oe_string.length;oedata++){
							String oe_breakDown[] = oe_string[oedata].split("~");
							StoreExpRepResp("11","1",oe_breakDown,sqdb);
						}
					}
				}
				else{
					String[] ta_string=getElementValues("ta", "ta_view", element);
					String[] da_string=getElementValues("da", "da_view", element);
					String[] oe_string=getElementValues("oe", "oe_view", element);
					
					for(int tadata=0;tadata<ta_string.length;tadata++){					
						String ta_breakDown[] = ta_string[tadata].split("~");
						updateExpRepResp(ta_breakDown,sqdb);
						
					}					
					for(int dadata=0;dadata<da_string.length;dadata++){
						String da_breakDown[] = da_string[dadata].split("~");
						updateExpRepResp(da_breakDown,sqdb);
					}					
					for(int oedata=0;oedata<oe_string.length;oedata++){
						String oe_breakDown[] = oe_string[oedata].split("~");
						updateExpRepResp(oe_breakDown,sqdb);
					}
				}

			}

		} catch (Exception E) {

		}

	}

	private void updateExpRepResp(String[] edata,SQLiteDatabase sqdb){
		//tab id~DBID~status~RejectsReason("none" if not rejected)
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();						 
			cv.put("server_id", Integer.valueOf(edata[1])); 			
			cv.put("appr_status", edata[2]);
			if(edata[2].equals("4")){
				cv.put("reason", edata[3]);
			}
			try {
				sqdb.update("expense_trans", cv, "local_id=?", new String[]{edata[0]});
				updateFlag[9]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void ParseExpRepApproval(String xmlContent,SQLiteDatabase sqdb){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList exprepapprovalNodes = doc.getElementsByTagName("exp_rep_approval");
			int getcountExpRepApprv = exprepapprovalNodes.getLength();
			for (int i = 0; i < getcountExpRepApprv; i++) {
				Element element = (Element) exprepapprovalNodes.item(i);				
				String[] ta_string=getElementValues("ta_avp", "ta_viewavp", element);
				String[] da_string=getElementValues("da_avp", "da_viewavp", element);
				String[] oe_string=getElementValues("oe_avp", "oe_viewavp", element);
				StoreExpRepApproval_ta(ta_string,sqdb);
				StoreExpRepApproval_da(da_string,sqdb);
				StoreExpRepApproval_oe(oe_string,sqdb);
			}
		}catch(Exception e){
			
		}
		
	}
	
	private void StoreExpRepApproval_ta(String[] edata,SQLiteDatabase sqdb) {
		//DBID~date~from~to~mode~type~distance~amount~requested by~status	
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();			
			cv.put("transaction_id", Integer.valueOf(edata[0])); 
			cv.put("trans_date", edata[1]); 
			cv.put("from_place", edata[2]); 
			cv.put("to_place", edata[3]); 
			cv.put("travel_mode", edata[4]); 
			cv.put("travel_type", edata[5]); 
			cv.put("travel_km", edata[6]);
			cv.put("amount", edata[7]);
			cv.put("created_by", edata[8]);
			cv.put("appr_flag", edata[9]); 			
			try {
				sqdb.insertOrThrow("expanse_transactions_ta", null, cv);
				updateFlag[10]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void StoreExpRepApproval_da(String[] edata,SQLiteDatabase sqdb) {
		//DBID~date~exp type name~amount~requested by
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();			
			cv.put("transaction_id", Integer.valueOf(edata[0])); 
			cv.put("trans_date", edata[1]); 
			cv.put("exp_type_name", edata[2]); 			
			cv.put("amount", edata[3]);
			cv.put("created_by", edata[4]);			
			try {
				sqdb.insertOrThrow("expanse_transactions_da", null, cv);
				updateFlag[11]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void StoreExpRepApproval_oe(String[] edata,SQLiteDatabase sqdb) {
		//DBID~date~exp type name~amount~requested by
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();			
			cv.put("transaction_id", Integer.valueOf(edata[0])); 
			cv.put("trans_date", edata[1]); 
			cv.put("exp_type_name", edata[2]); 			
			cv.put("amount", edata[3]);
			cv.put("created_by", edata[4]);			
			try {
				sqdb.insertOrThrow("expanse_transactions_oe", null, cv);
				updateFlag[12]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void StoreExpRepRespTa(String policy_type,String emp_id,String[] edata,SQLiteDatabase sqdb) {
		//tab id~DBID~date~from_id~to_id~mode_id~type_id~distance~amount~status		
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();			
			cv.put("exp_policy_id", Integer.valueOf(policy_type));
			cv.put("employee_id", Integer.valueOf(emp_id));
			cv.put("local_id", Integer.valueOf(edata[0])); 
			cv.put("server_id", Integer.valueOf(edata[1])); 
			cv.put("exp_date", edata[2]); 
			cv.put("loc_from", edata[3]); 
			cv.put("loc_to", edata[4]); 
			cv.put("travel_mode", edata[5]); 
			cv.put("trip_type", edata[6]); 
			cv.put("tarvel_km", edata[7]);
			cv.put("amount", edata[9]);
			cv.put("appr_status", edata[8]); 			
			try {
				sqdb.insertOrThrow("expense_trans", null, cv);
				updateFlag[9]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	
	private void StoreExpRepResp(String policy_type,String emp_id,String[] edata,SQLiteDatabase sqdb) {
		//tab id~DBID~date~exp type Id~amount
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();			
			cv.put("exp_policy_id", Integer.valueOf(edata[3]));
			cv.put("employee_id", Integer.valueOf(emp_id));
			cv.put("local_id", Integer.valueOf(edata[0])); 
			cv.put("server_id", Integer.valueOf(edata[1])); 
			cv.put("exp_date", Integer.valueOf(edata[2])); 
			cv.put("amount", edata[4]);
			cv.put("appr_status", edata[5]); 			
			try {
				sqdb.insertOrThrow("expense_trans", null, cv);
				updateFlag[9]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	
	
	public String[] getElementValues(String tagName,String elementName,Element element){
		NodeList nl = element.getElementsByTagName(tagName);
		String[] ta_string = new String[nl.getLength()];
		try {
			if (nl.getLength() > 0) {
				for (int ta = 0; ta < nl.getLength(); ta++) {
					Element elm = (Element) nl.item(ta);
					NodeList nlta = elm.getElementsByTagName(elementName);
					Element elmta = (Element) nlta.item(0);
					ta_string[ta] = getCharacterDataFromElement(elmta);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ta_string;
	}
	
	
	
	private void ParseExpansePolicy(String xmlContent,SQLiteDatabase sqdb) {
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("expense_policy");
			
			int getcount = nodes.getLength();
			
			
			for (int i = 0; i < getcount; i++) {
				String[] rows=new String[]{"","","","","","","","","",""};
				Element element = (Element) nodes.item(i);
				
				NodeList nl = element.getElementsByTagName("exp_id");
				Element elm = (Element) nl.item(0);
				rows[0] = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("exptype_id");
				elm = (Element) nl.item(0);
				rows[1] = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("exp_title");
				elm = (Element) nl.item(0);
				rows[2] = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("exp_desc");
				elm = (Element) nl.item(0);
				rows[3] = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("value_type");
				elm = (Element) nl.item(0);
				rows[4] = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("range_from");
				elm = (Element) nl.item(0);
				rows[5] = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("range_to");
				elm = (Element) nl.item(0);
				rows[6] = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("amount");
				elm = (Element) nl.item(0);
				rows[7] = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("effective_date");
				elm = (Element) nl.item(0);
				rows[8] = getCharacterDataFromElement(elm);
										
				nl = element.getElementsByTagName("actual");
				elm = (Element) nl.item(0);
				rows[9] = getCharacterDataFromElement(elm);
				
				StoreExpensePolicy(rows, sqdb);
				
			}
		} catch (Exception E) {
			E.printStackTrace();
		}

	}

	private void updateexpensedata(String[] edata,SQLiteDatabase sqdb){
		String sql = "Update expanse_transactions SET transaction_id = " +edata[0]+
		",appr_flag = "+edata[16]+" where local_id="+edata[1];
		sqdb.execSQL(sql);
	}
	
	private void StoreExpensePolicy(String[] edata,SQLiteDatabase sqdb) {
		sqdb.execSQL("DELETE from gst_exp_policy where exp_id="
				+ edata[0]);
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("exp_id", Integer.valueOf(edata[0])); 
			cv.put("exptype_id", edata[1]); 
			cv.put("exp_title", edata[2]); 
			cv.put("exp_desc", edata[3]); 
			cv.put("value_type", edata[4]); 
			cv.put("range_from", edata[5]); 
			cv.put("range_to", edata[6]); 
			cv.put("amount", edata[7]);
			cv.put("actual", edata[9]);
			cv.put("effective_date", edata[8]); 			
			try {
				sqdb.insertOrThrow("gst_exp_policy", null, cv);
				updateFlag[2]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void ParseRelExpGrp(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areatypeNodes = doc.getElementsByTagName("rel_exp_grp");
			int getcountareatype = areatypeNodes.getLength();

			for (int i = 0; i < getcountareatype; i++) {

				Element element = (Element) areatypeNodes.item(i);

				NodeList nl = element.getElementsByTagName("rel_id");
				Element elm = (Element) nl.item(0);
				String rel_id = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("grp_id");
				elm = (Element) nl.item(0);
				String grp_id = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("exp_id");
				elm = (Element) nl.item(0);
				String exp_id = getCharacterDataFromElement(elm);
				

				StoreRelExpGrp(rel_id,grp_id,exp_id, sqdb);

			}

		} catch (Exception E) {

		}

	}
	
	private void ParseExpenseMaster(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areatypeNodes = doc.getElementsByTagName("expense_master");
			int getcountareatype = areatypeNodes.getLength();

			for (int i = 0; i < getcountareatype; i++) {

				Element element = (Element) areatypeNodes.item(i);

				NodeList nl = element.getElementsByTagName("exp_id");
				Element elm = (Element) nl.item(0);
				String exp_id = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("exp_lable");
				elm = (Element) nl.item(0);
				String exp_lable = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("exp_title");
				elm = (Element) nl.item(0);
				String exp_title = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("exp_desc");
				elm = (Element) nl.item(0);
				String exp_desc = getCharacterDataFromElement(elm);
				
				StoreExpMstr(exp_id,exp_lable,exp_title,exp_desc, sqdb);

			}

		} catch (Exception E) {

		}

	}
	
	
	
	private void ParseTravelMode(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areatypeNodes = doc.getElementsByTagName("travel_mode");
			int getcountareatype = areatypeNodes.getLength();

			for (int i = 0; i < getcountareatype; i++) {

				Element element = (Element) areatypeNodes.item(i);

				NodeList nl = element.getElementsByTagName("tr_id");
				Element elm = (Element) nl.item(0);
				String travelID = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("tr_type");
				elm= (Element) nl.item(0);
				String travelName = getCharacterDataFromElement(elm);
				
				Storetravelmode(travelID, travelName,sqdb);
				

			}

		} catch (Exception E) {
			E.printStackTrace();
		}

	}


	private void ParsePeerGroup(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areatypeNodes = doc.getElementsByTagName("pr_gr_dtl");
			int getcountareatype = areatypeNodes.getLength();

			for (int i = 0; i < getcountareatype; i++) {

				Element element = (Element) areatypeNodes.item(i);

				NodeList nl = element.getElementsByTagName("emp_detail");
				Element elm = (Element) nl.item(0);
				String emp_detail = getCharacterDataFromElement(elm);
				
				Storepeergoup(emp_detail, sqdb);
			}

		} catch (Exception E) {
			E.printStackTrace();
		}

	}
	
	
	private void Parsegrouptype(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areacatNodes = doc.getElementsByTagName("group_type");
			int getcountgroup = areacatNodes.getLength();

			for (int i = 0; i < getcountgroup; i++) {

				Element element = (Element) areacatNodes.item(i);

				NodeList nl = element.getElementsByTagName("cid");
				Element elm = (Element) nl.item(0);
				String groupID = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("type_id");
				elm= (Element) nl.item(0);
				String group_name = getCharacterDataFromElement(elm);

				Storegrouptype(groupID, group_name,sqdb);
				

			}

		} catch (Exception E) {

		}

	}
	
	
	private void ParseExpanseType(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areacatNodes = doc.getElementsByTagName("expense_type_master");
			int getcountareacat = areacatNodes.getLength();

			for (int i = 0; i < getcountareacat; i++) {

				Element element = (Element) areacatNodes.item(i);

				NodeList nl = element.getElementsByTagName("exp_ty_id");
				Element elm = (Element) nl.item(0);
				String exptypeid = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("exp_gr_id");
				elm= (Element) nl.item(0);
				String expgrpid = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("exp_ty_lable");
				elm = (Element) nl.item(0);
				String exptypelabel = getCharacterDataFromElement(elm);
				
				nl = element.getElementsByTagName("exp_type_master");
				elm = (Element) nl.item(0);
				String exptypename = getCharacterDataFromElement(elm);
				

				Storeexpansetype(exptypeid, expgrpid,exptypelabel,exptypename,sqdb);

			}

		} catch (Exception E) {
			E.printStackTrace();
		}

	}
	
	private void ParseTripType(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList areacatNodes = doc.getElementsByTagName("trip_type");
			int getcountareacat = areacatNodes.getLength();

			for (int i = 0; i < getcountareacat; i++) {

				Element element = (Element) areacatNodes.item(i);

				NodeList nl = element.getElementsByTagName("trip_id");
				Element elm = (Element) nl.item(0);
				String trip_id = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("status_name");
				elm= (Element) nl.item(0);
				String trip_name = getCharacterDataFromElement(elm);
								
				
				StoreTriptype(trip_id, trip_name,sqdb);
				

			}

		} catch (Exception E) {

		}

	}

	
	private void ParsePlaceAreaData(String xmlContent,SQLiteDatabase sqdb) {

		try {

			
			String[] placeData = new String[1];
			placeData[0]=xmlContent;
			
			//tab_id~DBID~reject reason~flag
			//tab_id~DBID~place name~area type Id~metro type Id~flag~reason
			
			String[] rawData = placeData[0].split("~");
			if (rawData.length > 4) {
				StorePlaceArea(rawData, sqdb);
			}else{
				UpdatePlaceArea(rawData, sqdb);
			}
			
			
		} catch (Exception E) {
			E.printStackTrace();
		}

	}

	private void StorePlaceArea(String[] raws,SQLiteDatabase sqdb) {
		
		sqdb.beginTransaction();
		
		try {

			ContentValues cv = new ContentValues();
			if(!raws[0].equals("0")){
				cv.put("location_id", raws[0]);
				//sqdb.delete("addlocation", "server_id=?",new String[]{raws[1]});
			}	else{
				//sqdb.delete("addlocation", "location_id=?",new String[]{raws[0]});
			}
			cv.put("location_name", raws[2]);
			cv.put("area_type", raws[3]);
			cv.put("metro_type", raws[4]);
			cv.put("server_id", raws[1]);
			cv.put("status", raws[5]);
			try{
				cv.put("reason", raws[6]);
			}catch(Exception e){
				cv.put("reason", "");
			}
			try {
				sqdb.insertOrThrow("addlocation", null, cv);
				updateFlag[0]=true;
			} catch (SQLiteConstraintException e) {
				Log.e("Already Stored", "true");
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void UpdatePlaceArea(String[] raws,SQLiteDatabase sqdb) {
		
		sqdb.beginTransaction();
		try {

			ContentValues cv = new ContentValues();
			
			cv.put("server_id", raws[1]);			
			cv.put("status", raws[2]);
			try{
				cv.put("reason", raws[3]);
			}catch(Exception e){
				cv.put("reason", "");
			}
			try {
				sqdb.update("addlocation", cv, "location_id=?", new String[]{raws[0]});
				updateFlag[0]=true;
			} catch (SQLiteConstraintException e) {
				Log.e("Already Stored", "true");
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
	}
	
	private void Storeapproverequest(String[] dData,SQLiteDatabase sqdb) {
		//0~1~2~3~4~5~6
		//DBID~place name~area type Id~metro type Id~Requested By~possition_name~flag
		//7~goregaon~OS~METRO~Magan Singh~MH-SEO-PUN-01~1
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("area_name", dData[1]);
			cv.put("area_type", dData[2]);
			cv.put("metro_type", dData[3]);
			//cv.put("reason", reason);
			cv.put("approve_flag", dData[6]);
			cv.put("requestedby_id", dData[4]);
			cv.put("position_name", dData[5]);
			cv.put("server_id", dData[0]);
			try {
				sqdb.insertOrThrow("approverequest", null, cv);
				updateFlag[1]=true;
			} catch (SQLiteConstraintException e) {
				Log.e("Already Stored", "true");
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		sqdb.endTransaction();
		
	}
	
	
	private void ParsePlaceAreaApproval(String xmlContent,SQLiteDatabase sqdb) {

		try {
						
			String[] placeData = new String[1];
			
			//DBID~place name~area type Id~metro type Id~Requested By~possition_name~flag
			//7~goregaon~OS~METRO~Magan Singh~MH-SEO-PUN-01~1
				String[] rawData = xmlContent.split("~");
				if(rawData.length>0)
					Storeapproverequest(rawData,sqdb);
			
		} catch (Exception E) {
			E.printStackTrace();
		}

	}
	private void ParseStatus(String xmlContent,SQLiteDatabase sqdb) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));

			Document doc = db.parse(is);
			NodeList statusNodes = doc.getElementsByTagName("status");
			int getcountStatus = statusNodes.getLength();

			for (int i = 0; i < getcountStatus; i++) {

				Element element = (Element) statusNodes.item(i);

				NodeList nl = element.getElementsByTagName("status_id");
				Element elm = (Element) nl.item(0);
				String statusID = getCharacterDataFromElement(elm);

				nl = element.getElementsByTagName("status_name");
				elm= (Element) nl.item(0);
				String status_name = getCharacterDataFromElement(elm);

				
				StoreStatustype(statusID, status_name,sqdb);
				

			}

		} catch (Exception E) {

		}

	}


	private void StoreStatustype(String status_Id, String status_name,SQLiteDatabase sqdb) {
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("status_id", Integer.valueOf(status_Id));
			cv.put("status_name", status_name);

			try {
				sqdb.insertOrThrow("statustype", null, cv);
				updateFlag[3]=true;
			} catch (SQLiteConstraintException e) {
				Log.e("Already Stored", "true");
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	private void Storetravelmode(String travelmode_Id, String travelmode_name,SQLiteDatabase sqdb) {

		sqdb.execSQL("DELETE from travelmode where travel_id=" + Integer.parseInt(travelmode_Id));

		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("travel_id", Integer.valueOf(travelmode_Id));
			cv.put("travel_mode", travelmode_name);

			try {
				sqdb.insertOrThrow("travelmode", null, cv);
				updateFlag[4]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	
	private void Storepeergoup(String emp_detail,SQLiteDatabase sqdb) {
		 // employee_id~employee_name~designation~possition~approval_status(for approval screen) 
		//47~Magan Singh~2~MH-SEO-PUN-01~6~SEO-Sales Executive Officer~2
		String[] rowData = emp_detail.split("~");		
		sqdb.execSQL("DELETE from emp_detail where employee_id = "+rowData[0]);
		sqdb.beginTransaction();
		
		try {
			ContentValues cv = new ContentValues();
			cv.put("employee_id", Integer.valueOf(rowData[0]));
			cv.put("employee_name", rowData[1]);
			cv.put("designation", rowData[2]);
			cv.put("position", rowData[3]);
			cv.put("approval_status", rowData[4]);
			try {
				sqdb.insertOrThrow("emp_detail", null, cv);
				updateFlag[5]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	private void Storegrouptype(String group_Id, String group_name,SQLiteDatabase sqdb) {
		sqdb.execSQL("DELETE from gst_group_type where group_id=" + Integer.parseInt(group_Id));
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("group_id", Integer.valueOf(group_Id));
			cv.put("group_name", group_name);

			try {
				sqdb.insertOrThrow("gst_group_type", null, cv);
				updateFlag[6]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	private void Storeexpansetype(String expansetype_Id,String expansegrp_Id, String expanse_label,String expanse_name,SQLiteDatabase sqdb) {
		sqdb.execSQL("DELETE from gst_exp_type_master where exp_type_id=" + Integer.parseInt(expansetype_Id));
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("exp_type_id", Integer.valueOf(expansetype_Id));
			cv.put("exp_group_id", expansegrp_Id);
			cv.put("exp_type_label", expanse_label);
			cv.put("exp_type_name", expanse_name);
			try {
				sqdb.insertOrThrow("gst_exp_type_master", null, cv);
				updateFlag[7]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	private void StoreTriptype(String trip_Id, String trip_name,SQLiteDatabase sqdb) {
		sqdb.execSQL("DELETE from gst_trip_type where trip_id=" + Integer.parseInt(trip_Id));
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("trip_id", Integer.valueOf(trip_Id));
			cv.put("trip_name", trip_name);

			try {
				sqdb.insertOrThrow("gst_trip_type", null, cv);
				updateFlag[8]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}	

	//rel_id,grp_id,exp_id, sqdb
	private void StoreRelExpGrp(String rel_Id,String grp_Id, String exp_id,SQLiteDatabase sqdb) {
		sqdb.execSQL("DELETE from gst_exp_grp_rel where rel_Id=" + Integer.parseInt(rel_Id));
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("rel_id", Integer.valueOf(rel_Id));
			cv.put("group_id", grp_Id);
			cv.put("exp_id", exp_id);
			try {
				sqdb.insertOrThrow("gst_exp_grp_rel", null, cv);
				updateFlag[13]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	
	

	//rel_id,grp_id,exp_id, sqdb
	private void StoreExpMstr(String exp_id,String exp_lable, String exp_title,String exp_desc,SQLiteDatabase sqdb) {
		sqdb.execSQL("DELETE from gst_expense_master where exp_id=" + Integer.parseInt(exp_id));
		sqdb.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("exp_id", Integer.valueOf(exp_id));
			cv.put("exp_lable", exp_lable);
			cv.put("exp_title", exp_title);
			cv.put("exp_desc", exp_desc);
			try {
				sqdb.insertOrThrow("gst_expense_master", null, cv);
				updateFlag[14]=true;
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
			sqdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqdb.endTransaction();
	}
	
	public static String getCharacterDataFromElement(Element e) {
		Node child = ((Node) e).getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

}
