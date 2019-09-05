package domain;

public class HistoryDomain {
	String date;
	String open;
	String close;
	String high;
	String low;
	String count;
	double lastAount;
	
	public HistoryDomain(String date, String open, String close, String high, String low, String count) {
		super();
		this.date = date;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.count = count;
	}
	
	public HistoryDomain() {
		super();
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public double getNumberClose() {
		return Double.parseDouble(close);
	}
	public double getNumberOpen() {
		return Double.parseDouble(open);
	}

	public double getNumberCount() {
		return Double.parseDouble(count);
	}
	public double getAmountFlowValue(){
		double result = getNumberCount() - lastAount;
		return result;
	}
	
	public double getNumberRate(double lastNumberClose){
		if(lastNumberClose == 0){
			return -999;
		}
		double rate = ((getNumberClose() - lastNumberClose)/lastNumberClose) * 100;
		return rate;
	}
	
}
