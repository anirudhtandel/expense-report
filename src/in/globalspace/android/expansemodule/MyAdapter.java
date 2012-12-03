package in.globalspace.android.expansemodule;

import in.globalspace.android.expensereport.R;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Model> {

	private final List<Model> list;
	private final Activity context;
	boolean checkAll_flag = false;
	boolean checkItem_flag = false;
	ViewGroup rootVIew;
	Typeface font1;

	public MyAdapter(Activity context, List<Model> list) {
		super(context, R.layout.approve_customlayout, list);
		this.context = context;
		this.list = list;

	}

	static class ViewHolder {
		protected TextView Requestedby;
		protected TextView Position;
		protected TextView Locationname;
		protected TextView Areatype;
		protected TextView Metrotype;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.approve_customlayout, null);
			rootVIew = (ViewGroup) convertView
					.findViewById(R.id.rootcustomlayout);
			viewHolder = new ViewHolder();

			viewHolder.Requestedby = (TextView) convertView
					.findViewById(R.id.Requestedby);
			
			viewHolder.Position = (TextView) convertView
					.findViewById(R.id.Position);
			viewHolder.Locationname = (TextView) convertView
					.findViewById(R.id.Locationname);
			viewHolder.Areatype = (TextView) convertView
					.findViewById(R.id.Areatype);
			viewHolder.Metrotype = (TextView) convertView
					.findViewById(R.id.Metrotype);
			viewHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.check);

			/*ViewUtils.setTypeFaceForChilds(rootVIew, addExpenseReport.font1);*/
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int getPosition = (Integer) buttonView.getTag();

							list.get(getPosition).setSelected(
									buttonView.isChecked());

						}
					});
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.Requestedby, viewHolder.Requestedby);
			convertView.setTag(R.id.Position, viewHolder.Position);
			convertView.setTag(R.id.Locationname, viewHolder.Locationname);
			convertView.setTag(R.id.Areatype, viewHolder.Areatype);
			convertView.setTag(R.id.Metrotype, viewHolder.Metrotype);
			convertView.setTag(R.id.check, viewHolder.checkbox);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.checkbox.setTag(position); // This line is important.

		viewHolder.Requestedby.setText(list.get(position).getRequestedBy());
		viewHolder.Position.setText(list.get(position).getPosition());
		viewHolder.Locationname.setText(list.get(position).getName());
		viewHolder.Areatype.setText(list.get(position).getAreaType());
		viewHolder.Metrotype.setText(list.get(position).getMetroType());
		viewHolder.checkbox.setChecked(list.get(position).isSelected());

		return convertView;
	}

}
