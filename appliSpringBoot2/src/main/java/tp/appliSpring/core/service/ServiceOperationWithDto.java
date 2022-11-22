package tp.appliSpring.core.service;

import tp.appliSpring.dto.OperationDto;

public interface ServiceOperationWithDto extends GenericServiceWithDto<OperationDto,OperationDto,Long>{

	OperationDto saveNewWithAccountNumber(OperationDto opDto, Long numCompte);


}
