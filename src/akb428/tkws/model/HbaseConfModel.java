package akb428.tkws.model;

import java.util.List;

public class HbaseConfModel {
	boolean execute;
	List<String> resource;
	
	public boolean isExecute() {
		return execute;
	}
	public void setExecute(boolean execute) {
		this.execute = execute;
	}
	public List<String> getResource() {
		return resource;
	}
	public void setResource(List<String> resource) {
		this.resource = resource;
	}
}
