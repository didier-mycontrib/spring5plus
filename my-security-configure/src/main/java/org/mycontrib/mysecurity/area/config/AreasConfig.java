package org.mycontrib.mysecurity.area.config;

//Web Protected Area Config as arrays
public class AreasConfig {
	private AreaConfig rest;
	private AreaConfig site;
	private AreaConfig other;
	private AreaConfig tools;
	
	public AreasConfig() {
		super();
		rest=new AreaConfig();
		site=new AreaConfig();
		other=new AreaConfig();
		tools=new AreaConfig();
	}

	@Override
	public String toString() {
		return "AreasConfig [rest=" + rest + ",\n site=" + site + ",\n other=" + other + ",\n tools=" + tools + "]\n";
	}

	public AreaConfig getRest() {
		return rest;
	}

	public void setRest(AreaConfig rest) {
		this.rest = rest;
	}

	public AreaConfig getSite() {
		return site;
	}

	public void setSite(AreaConfig site) {
		this.site = site;
	}

	public AreaConfig getOther() {
		return other;
	}

	public void setOther(AreaConfig other) {
		this.other = other;
	}

	public AreaConfig getTools() {
		return tools;
	}

	public void setTools(AreaConfig tools) {
		this.tools = tools;
	}
	
}
