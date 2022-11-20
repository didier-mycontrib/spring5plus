package tp.appliSpring.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tp.appliSpring.converter.MyMapper;
import tp.appliSpring.core.dao.DaoCompte;
import tp.appliSpring.core.dao.DaoOperation;
import tp.appliSpring.core.entity.Operation;
import tp.appliSpring.core.service.ServiceOperationWithDto;
import tp.appliSpring.dto.OperationDto;

@Service
@Transactional
public class ServiceOperationWithDtoImpl 
  extends AbstractGenericStdServiceWithDto<OperationDto,Long,Operation,DaoOperation> 
  implements ServiceOperationWithDto{
	
	private DaoOperation daoOperation; //for specific methods of this class
	private DaoCompte daoCompte;//secondary Dao
	
	private MyMapper myMapper;
	
	@Autowired
	public ServiceOperationWithDtoImpl(DaoOperation daoOperation,DaoCompte daoCompte , MyMapper myMapper) {
		super(OperationDto.class, Operation.class, daoOperation);
		this.daoOperation=daoOperation;
		this.daoCompte=daoCompte;
		this.myMapper=myMapper;
	}

	@Override
	public OperationDto saveNewWithAccountNumber(OperationDto opDto, Long numCompte) {
		Operation operationEntity = myMapper.operationDtoOperation(opDto);
		if(operationEntity.getDateOp()==null)
			operationEntity.setDateOp(new Date());
		operationEntity.setCompte(daoCompte.findById(numCompte).get());
		daoOperation.save(operationEntity);
		opDto.setNumOp(operationEntity.getNumOp());
		opDto.setDateOp((new SimpleDateFormat("yyy-MM-dd")).format(operationEntity.getDateOp()));
		return opDto;
	}

}
