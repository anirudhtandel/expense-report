package in.globalspace.android.expansemodule;

public class Model {

	private String name;
	private String areatype;
	private String metrotype;
	private String requestedby;
	private String position;
	private boolean selected;

	public Model(String requestedBy,String Position, String name, String AreaType,
			String MetroType) {
		this.requestedby = requestedBy;
		this.position=Position;
		this.name = name;
		this.areatype = AreaType;
		this.metrotype = MetroType;
	}

	public String getName() {
		return name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean getSelected() {
		return selected;
	}

	public String getAreaType() {
		return areatype;
	}

	public String getRequestedBy() {
		return requestedby;
	}
	
	public String getPosition() {
		return position;
	}

	public String getMetroType() {
		return metrotype;
	}
}
