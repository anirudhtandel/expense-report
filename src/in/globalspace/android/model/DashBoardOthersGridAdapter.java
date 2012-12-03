package in.globalspace.android.model;

import in.globalspace.android.expensereport.R;

import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class DashBoardOthersGridAdapter extends
		ArrayAdapter<DashBoardOthersGridAdapter.DashboardGridModel> {

	private Context context;
	private ArrayList<DashboardGridModel> items;


	public DashBoardOthersGridAdapter(Context context,
			ArrayList<DashboardGridModel> objects) {
		super(context, R.layout.dashboard_grid_items,
				R.id.dashboard_grid_items_img, objects);

		this.context = context;
		this.items = objects;

	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewWrapper viewWrapper;
		final int positionstring = position;

		final DashboardGridModel currentGridModel = items.get(position);

		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.dashboard_grid_items, null);

			viewWrapper = new ViewWrapper(view);
			view.setTag(viewWrapper);

		} else {
			viewWrapper = (ViewWrapper) view.getTag();
		}

		viewWrapper.getImg().setImageResource(currentGridModel.getResourceId());
		viewWrapper.getImg().setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try{
					if(positionstring<1){	//for Sync Service Call
						context.startActivity(currentGridModel.getIntent());
					}
					else
					{
						Intent iIntent = new Intent();
						iIntent.setAction("in.globalspace.android.expense.syncservice.EXPENSE_SYNC_INTENT_SERVICE");
						iIntent.putExtra("RequestType", "all");
						context.startService(iIntent);						
					}
				}catch(ActivityNotFoundException ex){ex.printStackTrace();}

			}
		});
		return view;
	}

	private class ViewWrapper {
		private View base;
		private ImageView img;

		public ViewWrapper(View base) {
			this.base = base;
		}

		public ImageView getImg() {
			if (img == null)
				img = (ImageView) base
						.findViewById(R.id.dashboard_grid_items_img);
			return img;
		}

	}

	public static class DashboardGridModel {
		private String title;
		private int resourceId;
		private Intent intent;

		public DashboardGridModel(int resourceId, Intent intent) {
			this.resourceId = resourceId;
			this.intent = intent;
		}

		public int getResourceId() {
			return resourceId;
		}

		public DashboardGridModel setResourceId(int resourceId) {
			this.resourceId = resourceId;
			return this;
		}

		public Intent getIntent() {
			return intent;
		}

		public DashboardGridModel setIntent(Intent intent) {
			this.intent = intent;
			return this;
		}
	}

}
