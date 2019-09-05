package domain;

import org.junit.Test;

public class HolderDomain {
	String code;
	String date;
	String name;
	String amount;
	String rate;
	String upDown;
	String change;
	
	public HolderDomain(String name, String amount, String rate, String upDown, String change) {
		super();
		this.name = name;
		this.amount = amount;
		this.rate = rate;
		this.upDown = upDown;
		this.change = change;
	}
	public HolderDomain(String date, String name, String amount, String rate, String upDown, String change) {
		super();
		this.date = date;
		this.name = name;
		this.amount = amount;
		this.rate = rate;
		this.upDown = upDown;
		this.change = change;
	}
	public HolderDomain(String code, String date, String name, String amount, String rate, String upDown,
			String change) {
		super();
		this.code = code;
		this.date = date;
		this.name = name;
		this.amount = amount;
		this.rate = rate;
		this.upDown = upDown;
		this.change = change;
	}
	public HolderDomain() {
		super();
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUpDown() {
		return upDown;
	}
	public void setUpDown(String upDown) {
		this.upDown = upDown;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public double getNumberOfRate() {
		double result = 0;
		try {
			if(rate.indexOf("%") > -1){
				result = Double.parseDouble(rate.substring(0, rate.indexOf("%")));
			}else{
				System.out.println("Rate No %");
				result = Double.parseDouble(rate);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Test
	public double getNumberOfJjAmount(){
		int isWanNum = amount.indexOf("Эђ");
		int isYiNum = amount.indexOf("вк");
		if(isWanNum > -1){
			String numStr = amount.substring(0, isWanNum);
			double sourceNum = Double.parseDouble(numStr);
			double result = sourceNum * 10000;
			return result;
		}
		if(isYiNum > -1){
			String numStr = amount.substring(0, isYiNum);
			double sourceNum = Double.parseDouble(numStr);
			double result = sourceNum * 100000000;
			return result;
		}
		return Double.parseDouble(amount);
	}
	
}
