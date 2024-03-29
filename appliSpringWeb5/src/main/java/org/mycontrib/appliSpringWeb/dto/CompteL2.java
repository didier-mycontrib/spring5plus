package org.mycontrib.appliSpringWeb.dto;

import java.util.List;

import org.mycontrib.util.generic.dto.DtoByLevelUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * CompteL2 au sens "DTO de niveau 2 (détaillé / étendu)"
 * avec sous dto 
 */
@Data @NoArgsConstructor
public class CompteL2  extends CompteL0{
    CustomerL0 customer;
    private List<OperationL0> operations;
    
}
