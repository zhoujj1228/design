package domain;

import java.util.ArrayList;
import java.util.List;

public class HolderHistoryDomain {
	String date;
	List<HolderDomain> ltgdList;
	List<HolderDomain> removeHolders;
	List<HolderDomain> jjHolders;
	
	public List<HolderDomain> getLtgdList() {
		return ltgdList;
	}
	public void setLtgdList(List<HolderDomain> ltgdList) {
		this.ltgdList = ltgdList;
	}
	public HolderHistoryDomain() {
		super();
		ltgdList = new ArrayList<HolderDomain>();
		removeHolders = new ArrayList<HolderDomain>();
		jjHolders = new ArrayList<HolderDomain>();
	}
	public List<HolderDomain> getJjHolders() {
		return jjHolders;
	}
	public void setJjHolders(List<HolderDomain> jjHolders) {
		this.jjHolders = jjHolders;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<HolderDomain> getRemoveHolders() {
		return removeHolders;
	}
	public void setRemoveHolders(List<HolderDomain> removeHolders) {
		this.removeHolders = removeHolders;
	}
}
