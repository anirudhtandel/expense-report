package in.globalspace.android.syncparse;

import in.globalspace.android.dbclass.DataHelperClass;
import in.globalspace.android.dbclass.SyncManagerDBAdapter;
import in.globalspace.android.soapservice.model.ResponseHandler;
import in.globalspace.android.soapservice.model.SOAPNameValuePair;
import in.globalspace.android.soapservice.model.SOAPRequestHelper;
import in.globalspace.android.soapservice.model.SOAPResponseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class Syncservice extends Service {
	Handler actHandler;
	AppConstants constants;
	String namespace, url, methodName, soapAction, ids, requestType;
	SoapRequestClass SRC;
	private Context mcontext;
	private static boolean syncStatus = false;
	SyncManagerDBAdapter dbHelper;
	DataHelperClass DHC;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		mcontext = this;
		SRC = new SoapRequestClass(this);
		constants = new AppConstants();
		constants.SOAPWebService(this);
		namespace = AppConstants.SOAP_NAMESPACE;
		url = AppConstants.SOAP_URL;
		methodName = AppConstants.INIT_SYNC;
		soapAction = AppConstants.SOAP_ACTION + methodName;
		/*
		 * actHandler = new Handler() { public void
		 * handleMessage(android.os.Message msg) { super.handleMessage(msg);
		 * Toast .makeText(mcontext, "Sync Completed..",
		 * Toast.LENGTH_SHORT).show(); syncStatus = false; stopSelf(); } };
		 */
		dbHelper = SyncManagerDBAdapter.getSharedObject(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		DHC = new DataHelperClass(getApplicationContext());
	}

	Handler handleFinish = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Toast.makeText(mcontext, "Sync Completed..", Toast.LENGTH_SHORT)
					.show();
			syncStatus = false;
			stopSelf();
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		if (!syncStatus) {
			String concatPrimaryString = "";
			syncStatus = true;
			constants.SOAPWebService(this);
			namespace = AppConstants.SOAP_NAMESPACE;
			url = AppConstants.SOAP_URL;
			methodName = AppConstants.INIT_SYNC;
			soapAction = AppConstants.SOAP_ACTION + methodName;
			requestType = "SYNC";
			if (Internet.checkConnection(mcontext)) {
				Toast.makeText(mcontext, "Syncing Started..",
						Toast.LENGTH_SHORT).show();
				SOAPRequestHelper soapRequestHelper = new SOAPRequestHelper(
						namespace, soapAction, methodName, url);
				requestType = "all";
				concatPrimaryString = SRC.ConcatPrimaryString(requestType);

				SOAPNameValuePair[] props = new SOAPNameValuePair[] { new SOAPNameValuePair(
						"xmlString", "<?xml version='1.0' encoding='UTF-8'?>"
								+ "<expense_sync>" + "<auth>"
								+ "<token_id>{$tokenAuth}</token_id>"
								+ "<user_id>{$user_id}</user_id>" + "<action>"
								+ requestType + "</action>" + "</auth>"
								+ concatPrimaryString + "</expense_sync>") };

				soapRequestHelper.setProperties(props);
				soapRequestHelper.performSOAPRequestWithCallback(mcontext,
						rsHandler);

			} else {
				syncStatus = false;
				Toast.makeText(mcontext, "No Network", Toast.LENGTH_SHORT)
						.show();
				stopSelf();
			}
		} else {
			Toast.makeText(mcontext, "Syncing", Toast.LENGTH_SHORT).show();
		}

		return START_STICKY;

	}

	public void pulldb() {
		try {
			copy(new File(
					"/data/data/in.globalspace.android.expensereport/databases/expanses_master.db"),
					new File(Environment.getExternalStorageDirectory()
							.toString() + File.separator + "expanses_master.db"));
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

	public void tellServerDatabaseUpdated() {
		// PullDBinPackage(mcontext);
		SOAPRequestHelper soapRequestHelper = new SOAPRequestHelper(namespace,
				soapAction, "wsupdate_syncstatus", url);

		SOAPNameValuePair[] props = new SOAPNameValuePair[] { new SOAPNameValuePair(
				"xmlString", "<?xml version='1.0' encoding='UTF-8'?>"
						+ "<expense_sync>" + "<auth>"
						+ "<token_id>{$tokenAuth}</token_id>"
						+ "<user_id>{$user_id}</user_id>" + "<action>"
						+ "requestType" + "</action>"
						+ "</auth></expense_sync>"), };

		soapRequestHelper.setProperties(props);
		soapRequestHelper.performSOAPRequest(mcontext);
		/*
		 * soapRequestHelper.performSOAPRequestWithCallback(mcontext,
		 * rsHandler);
		 */
	}

	public static String ReadSettings(Context context, String file)
			throws IOException {
		InputStreamReader isr = null;
		String data = null;
		File logFile = new File(Environment.getExternalStorageDirectory()
				+ "/response.xml");
		FileInputStream fIn = new FileInputStream(logFile);
		isr = new InputStreamReader(fIn);
		char[] inputBuffer = new char[fIn.available()];
		isr.read(inputBuffer);
		data = new String(inputBuffer);
		isr.close();
		fIn.close();
		return data;
	}

	ResponseHandler rsHandler = new ResponseHandler() {

		public void handleReposne(SOAPResponseHelper soapRes) {
			// TODO Auto-generated method stub
			try {
				final String xmlContent = soapRes.getResponse();
				if (!xmlContent.equals("")) {
					new Thread() {
						@Override
						public void run() {
							String xmlResp = "";
							try {
								xmlResp = ReadSettings(
										mcontext,
										Environment
												.getExternalStorageDirectory()
												+ "/response.xml");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							ExpanseParser Ep = new ExpanseParser();
							if (Ep.ParseXML(xmlResp, mcontext, requestType)) {
								tellServerDatabaseUpdated();
								pulldb();
								handleFinish.sendEmptyMessage(0);
							}
							// if (a.ParseXML(xmlContent, mcontext,
							// requestType)) {}
						}
					}.start();
				} else {
					syncStatus = false;
					Toast.makeText(mcontext, "Error", Toast.LENGTH_SHORT)
							.show();
					stopSelf();
				}
			} catch (Exception e) {
				syncStatus = false;
				Toast.makeText(mcontext, "Error", Toast.LENGTH_SHORT).show();
				stopSelf();
			}
		}

	};

}
