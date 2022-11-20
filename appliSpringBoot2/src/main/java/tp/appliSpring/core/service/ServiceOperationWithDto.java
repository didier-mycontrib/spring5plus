package tp.appliSpring.core.service;

import tp.appliSpring.dto.OperationDto;

public interface ServiceOperationWithDto extends StdGenericServiceWithDto<OperationDto,Long>{

	OperationDto saveNewWithAccountNumber(OperationDto opDto, Long numCompte);


}
