package domain;

public class BusinessManageRangeDomain {
	String date;
	String type;
	String pdName;
	String income;
	String incomeRate;
	String cost;
	String costRate;
	String profit;
	String profitRate;
	String pdProfitRate;
	public BusinessManageRangeDomain(String type, String pdName, String income, String incomeRate, String cost,
			String costRate, String profit, String profitRate, String pdProfitRate, String date) {
		super();
		this.date = date;
		this.type = type;
		this.pdName = pdName;
		this.income = income;
		this.incomeRate = incomeRate;
		this.cost = cost;
		this.costRate = costRate;
		this.profit = profit;
		this.profitRate = profitRate;
		this.pdProfitRate = pdProfitRate;
	}
	public BusinessManageRangeDomain() {
		super();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPdName() {
		return pdName;
	}
	public void setPdName(String pdName) {
		this.pdName = pdName;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getIncomeRate() {
		return incomeRate;
	}
	public void setIncomeRate(String incomeRate) {
		this.incomeRate = incomeRate;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getCostRate() {
		return costRate;
	}
	public void setCostRate(String costRate) {
		this.costRate = costRate;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getProfitRate() {
		return profitRate;
	}
	public void setProfitRate(String profitRate) {
		this.profitRate = profitRate;
	}
	public String getPdProfitRate() {
		return pdProfitRate;
	}
	public void setPdProfitRate(String pdProfitRate) {
		this.pdProfitRate = pdProfitRate;
	}
	
}
