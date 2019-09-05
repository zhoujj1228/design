package domain;

import java.util.HashMap;

public class BusinessManageDomain {
	String jyps;//经营评述
	String zyfw;//主营服务
	HashMap<String, HashMap<String, HashMap<String, BusinessManageRangeDomain>>> bmRangeMap = new HashMap<String, HashMap<String,HashMap<String, BusinessManageRangeDomain>>>();
	public String getJyps() {
		return jyps;
	}
	public void setJyps(String jyps) {
		this.jyps = jyps;
	}
	public String getZyfw() {
		return zyfw;
	}
	public void setZyfw(String zyfw) {
		this.zyfw = zyfw;
	}
	public HashMap<String, HashMap<String, HashMap<String, BusinessManageRangeDomain>>> getBmRangeMap() {
		return bmRangeMap;
	}
	public void setBmRangeMap(HashMap<String, HashMap<String, HashMap<String, BusinessManageRangeDomain>>> bmRangeMap) {
		this.bmRangeMap = bmRangeMap;
	}
}
