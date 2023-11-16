package org.mycontrib.util.generic.dto;

import java.io.Serializable;

public interface WithId<ID extends Serializable> {
	
	public ID extractId(); 

}
