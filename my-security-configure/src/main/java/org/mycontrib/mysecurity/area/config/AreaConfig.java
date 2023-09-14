package org.mycontrib.mysecurity.area.config;

import java.util.Arrays;

//Web Protected Area Config as arrays
public class AreaConfig {
	private String[] whitelist; //for .permitAll
	private String[] readonlylist ; //for .permitAll in GET only
	private String[] protectedlist;//for .authenticated
	private String[] blacklist; //for .denyAll
	
	public AreaConfig() {
		super();
		//default empty arrays (to override by set...)
		whitelist=new String[0];
		readonlylist=new String[0];
		protectedlist=new String[0];
		blacklist=new String[0];
	}
	
	
	
	@Override
	public String toString() {
		return "AreaConfig [whitelist=" + Arrays.toString(whitelist) + ", readonlylist=" + Arrays.toString(readonlylist)
				+ ", protectedlist=" + Arrays.toString(protectedlist) + ", blacklist=" + Arrays.toString(blacklist)
				+ "]";
	}



	public String[] getWhitelist() {
		return whitelist;
	}
	public void setWhitelist(String[] whitelist) {
		this.whitelist = whitelist;
	}
	public String[] getReadonlylist() {
		return readonlylist;
	}
	public void setReadonlylist(String[] readonlylist) {
		this.readonlylist = readonlylist;
	}
	public String[] getProtectedlist() {
		return protectedlist;
	}
	public void setProtectedlist(String[] protectedlist) {
		this.protectedlist = protectedlist;
	}
	public String[] getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(String[] blacklist) {
		this.blacklist = blacklist;
	}
	
	
	

}
