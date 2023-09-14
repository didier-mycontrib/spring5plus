package org.mycontrib.mysecurity.area.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
plusieurs sous parties complementaires:
 

 mysecurity.area.site.whitelist (site @Controller + JSP ou thymeleaf)
 plus
 mysecurity.area.rest.whitelist (api rest)
 plus
 mysecurity.area.tools.whitelist (h2: h2-console , swagger, ...)
 mysecurity.area.other.whitelist (parties static html , .jpeg, ...)
 
 */

@ConfigurationProperties(prefix="mysecurity.area")
public class MySecurityAreasProperties {
	
	private MySecurityAreaProperties rest;
	private MySecurityAreaProperties site;
	private MySecurityAreaProperties other;
	private MySecurityAreaProperties tools;
	
	//NB: default values can be set by WebProtectedAreaConfigurer

	public MySecurityAreasProperties() {
		super();
	}

	@Override
	public String toString() {
		return "MySecurityAreasProperties [rest=" + rest + ", site=" + site + ", other=" + other + ", tools=" + tools
				+ "]";
	}

	public MySecurityAreaProperties getRest() {
		return rest;
	}

	public void setRest(MySecurityAreaProperties rest) {
		this.rest = rest;
	}

	public MySecurityAreaProperties getSite() {
		return site;
	}

	public void setSite(MySecurityAreaProperties site) {
		this.site = site;
	}

	public MySecurityAreaProperties getOther() {
		return other;
	}

	public void setOther(MySecurityAreaProperties other) {
		this.other = other;
	}

	public MySecurityAreaProperties getTools() {
		return tools;
	}

	public void setTools(MySecurityAreaProperties tools) {
		this.tools = tools;
	}

	

}
