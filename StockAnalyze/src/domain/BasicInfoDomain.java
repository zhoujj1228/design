package domain;

public class BasicInfoDomain {
	private String code;
	private String name;
	private String industry;
	private String area;
	private String pe;
	private String outstanding;
	private String totals;
	private String totalAssets;
	private String liquidAssets;
	private String fixedAssets;
	private String reserved;
	private String reservedPerShare;
	private String esp;
	private String bvps;
	private String pb;
	private String timeToMarket;
	private String undp;
	private String perundp;
	private String rev;
	private String profit;
	private String gpr;
	private String npr;
	private String holders;
	private double backupDouble;
	
	public double getBackupDouble() {
		return backupDouble;
	}

	public void setBackupDouble(double backupDouble) {
		this.backupDouble = backupDouble;
	}

	BasicInfoDomain(){
		
	}
	
	public BasicInfoDomain(String code, String name, String industry, String area, String pe, String outstanding,
			String totals, String totalAssets, String liquidAssets, String fixedAssets, String reserved,
			String reservedPerShare, String esp, String bvps, String pb, String timeToMarket, String undp,
			String perundp, String rev, String profit, String gpr, String npr, String holders) {
		super();
		this.code = code;
		this.name = name;
		this.industry = industry;
		this.area = area;
		this.pe = pe;
		this.outstanding = outstanding;
		this.totals = totals;
		this.totalAssets = totalAssets;
		this.liquidAssets = liquidAssets;
		this.fixedAssets = fixedAssets;
		this.reserved = reserved;
		this.reservedPerShare = reservedPerShare;
		this.esp = esp;
		this.bvps = bvps;
		this.pb = pb;
		this.timeToMarket = timeToMarket;
		this.undp = undp;
		this.perundp = perundp;
		this.rev = rev;
		this.profit = profit;
		this.gpr = gpr;
		this.npr = npr;
		this.holders = holders;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPe() {
		return pe;
	}
	public void setPe(String pe) {
		this.pe = pe;
	}
	public String getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(String outstanding) {
		this.outstanding = outstanding;
	}
	public String getTotals() {
		return totals;
	}
	public void setTotals(String totals) {
		this.totals = totals;
	}
	public String getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}
	public String getLiquidAssets() {
		return liquidAssets;
	}
	public void setLiquidAssets(String liquidAssets) {
		this.liquidAssets = liquidAssets;
	}
	public String getFixedAssets() {
		return fixedAssets;
	}
	public void setFixedAssets(String fixedAssets) {
		this.fixedAssets = fixedAssets;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getReservedPerShare() {
		return reservedPerShare;
	}
	public void setReservedPerShare(String reservedPerShare) {
		this.reservedPerShare = reservedPerShare;
	}
	public String getEsp() {
		return esp;
	}
	public void setEsp(String esp) {
		this.esp = esp;
	}
	public String getBvps() {
		return bvps;
	}
	public void setBvps(String bvps) {
		this.bvps = bvps;
	}
	public String getPb() {
		return pb;
	}
	public void setPb(String pb) {
		this.pb = pb;
	}
	public String getTimeToMarket() {
		return timeToMarket;
	}
	public void setTimeToMarket(String timeToMarket) {
		this.timeToMarket = timeToMarket;
	}
	public String getUndp() {
		return undp;
	}
	public void setUndp(String undp) {
		this.undp = undp;
	}
	public String getPerundp() {
		return perundp;
	}
	public void setPerundp(String perundp) {
		this.perundp = perundp;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getGpr() {
		return gpr;
	}
	public void setGpr(String gpr) {
		this.gpr = gpr;
	}
	public String getNpr() {
		return npr;
	}
	public void setNpr(String npr) {
		this.npr = npr;
	}
	public String getHolders() {
		return holders;
	}
	public void setHolders(String holders) {
		this.holders = holders;
	}

}
