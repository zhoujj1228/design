package fs.domain;

import java.util.ArrayList;

public class Flow {
	private String name;
	private String type;
	private String classId;
	private String mappingId;
	private String method;
	private String condition;
	private ArrayList<Flow> subFlows = new ArrayList<Flow>();
	private Flow elseFlow;
	private ArrayList<Flow> ifFlows = new ArrayList<Flow>();
	
	private ArrayList<Flow> initFlows = new ArrayList<Flow>();
	private ArrayList<Flow> loopFlows = new ArrayList<Flow>();
	private String continueTo;
	private String breakTo;
	private Flow supFlow;
	public Flow getSupFlow() {
		return supFlow;
	}
	public void setSupFlow(Flow supFlow) {
		this.supFlow = supFlow;
	}
	public ArrayList<Flow> getInitFlows() {
		return initFlows;
	}
	public void setInitFlows(ArrayList<Flow> initFlows) {
		this.initFlows = initFlows;
	}
	public ArrayList<Flow> getLoopFlows() {
		return loopFlows;
	}
	public void setLoopFlows(ArrayList<Flow> loopFlows) {
		this.loopFlows = loopFlows;
	}
	public String getContinueTo() {
		return continueTo;
	}
	public void setContinueTo(String continueTo) {
		this.continueTo = continueTo;
	}
	public String getBreakTo() {
		return breakTo;
	}
	public void setBreakTo(String breakTo) {
		this.breakTo = breakTo;
	}
	public Flow getElseFlow() {
		return elseFlow;
	}
	public ArrayList<Flow> getIfFlows() {
		return ifFlows;
	}
	public void setIfFlows(ArrayList<Flow> ifFlows) {
		this.ifFlows = ifFlows;
	}
	public void setElseFlow(Flow elseFlow) {
		this.elseFlow = elseFlow;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
	public void addSubFlow(Flow flow){
		subFlows.add(flow);
	}

	public void addIfFlow(Flow flow){
		ifFlows.add(flow);
	}
	

	public void addInitFlow(Flow flow){
		initFlows.add(flow);
	}
	

	public void addLoopFlow(Flow flow){
		loopFlows.add(flow);
	}
	
}
