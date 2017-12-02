package fs.domain;

import java.util.ArrayList;

public class Flow {
	private String name;
	private String type;
	private String classId;
	private String mappingId;
	private String method;
	private ArrayList<Flow> subFlows = new ArrayList<Flow>();
	public void addSubFlow(Flow flow){
		subFlows.add(flow);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Flow> getSubFlows() {
		return subFlows;
	}
	public void setSubFlows(ArrayList<Flow> subFlows) {
		this.subFlows = subFlows;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getMappingId() {
		return mappingId;
	}
	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
}
