package in.globalspace.android.expansemodule;

import android.content.Intent;

public class GreatRepTabModel {

	private Intent childTabIntent;
	private int tabIndicatorResource;
	private String tabTag;

	public GreatRepTabModel(Intent childTabIntent, int tabIndicatorResource) {
		this.childTabIntent = childTabIntent;
		this.tabIndicatorResource = tabIndicatorResource;
	}

	public Intent getChildTabIntent() {
		return childTabIntent;
	}

	public GreatRepTabModel setChildTabIntent(Intent childTabIntent) {
		this.childTabIntent = childTabIntent;
		return this;
	}

	public int getTabIndicatorResource() {
		return tabIndicatorResource;
	}

	public GreatRepTabModel setTabIndicatorResource(int tabIndicatorResource) {
		this.tabIndicatorResource = tabIndicatorResource;
		return this;
	}

	public String getTabTag() {
		return tabTag;
	}

	public GreatRepTabModel setTabTag(String tabTag) {
		this.tabTag = tabTag;
		return this;
	}

}
