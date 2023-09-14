package org.mycontrib.mysecurity.area.properties;

public class MySecurityAreaProperties {
	
	private String whitelist; //for .permitAll
	private String blacklist; //for .denyAll
	private String readonlylist;//for .permitAll in mode GET only 
	  //(nb: all elements in readonlylist will also automatic be put in protectedlist for other modes (POST/PUT/...)
	private String protectedlist;//for .authenticated
	

	public MySecurityAreaProperties() {
		super();
	}


	@Override
	public String toString() {
		return "AreaProperties [whitelist=" + whitelist + ", blacklist=" + blacklist
				+ ", readonlylist=" + readonlylist + ", protectedlist=" + protectedlist + "]";
	}


	public String getWhitelist() {
		return whitelist;
	}


	public void setWhitelist(String whitelist) {
		this.whitelist = whitelist;
	}


	public String getBlacklist() {
		return blacklist;
	}


	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}


	public String getReadonlylist() {
		return readonlylist;
	}


	public void setReadonlylist(String readonlylist) {
		this.readonlylist = readonlylist;
	}


	public String getProtectedlist() {
		return protectedlist;
	}


	public void setProtectedlist(String protectedlist) {
		this.protectedlist = protectedlist;
	}

	


}
