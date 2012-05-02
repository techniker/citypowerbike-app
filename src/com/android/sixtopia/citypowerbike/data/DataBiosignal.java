package com.android.sixtopia.citypowerbike.data;
/**
 * 
 *
 */
public class DataBiosignal {
	
	private boolean live;
	private int puls;
	
	public DataBiosignal(boolean live, int puls){
		this.live = live;
		this.puls = puls;
	}

	public DataBiosignal(){}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getPuls() {
		return puls;
	}

	public void setPuls(int puls) {
		this.puls = puls;
	}
	public String toString(){
		String msg = "\nDataBiosignal:\n";
		if(live) msg += "Status: OK\n";
		else msg += "Status: DEAD\n";
		msg += "Puls: " + puls + " bpm\n";
		return msg;
	}
}
