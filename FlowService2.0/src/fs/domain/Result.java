package fs.domain;

import java.util.HashMap;

public class Result<T> {
	private T result;
	private String skip = null;
	private String skipToFlowName = null;
	private String breakTo;
	private String continueTo;

	public String getContinueTo() {
		return continueTo;
	}
	public void setContinueTo(String continueTo) {
		this.continueTo = continueTo;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public String getBreakTo() {
		return breakTo;
	}
	public void setBreakTo(String breakTo) {
		this.breakTo = breakTo;
	}
	public void setSkip(String skip) {
		this.skip = skip;
	}
	public String getSkipToFlowName() {
		return skipToFlowName;
	}
	public String getSkip() {
		return skip;
	}
	
	public void setSkipToDefault(){
		skip = null;
		skipToFlowName = null;
	}

	public T get() {
		return result;
	}

	public void set(T result) {
		this.result = result;
	}
	public void resetBreakTo() {
		breakTo = null;
	}
	public void resetContinueTo() {
		continueTo = null;
	}
	
}
