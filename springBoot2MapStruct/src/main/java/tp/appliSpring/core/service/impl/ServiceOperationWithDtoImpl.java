package tp.appliSpring.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tp.appliSpring.converter.MyMapper;
import tp.appliSpring.core.dao.DaoCompte;
import tp.appliSpring.core.dao.DaoOperation;
import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.entity.Operation;
import tp.appliSpring.core.service.ServiceOperationWithDto;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.OperationDto;

@Service
@Transactional
public class ServiceOperationWithDtoImpl 
  extends AbstractGenericServiceWithDto<OperationDto,OperationDto,Long,Operation,DaoOperation> 
  implements ServiceOperationWithDto{
	
	private DaoOperation daoOperation; //for specific methods of this class
	private DaoCompte daoCompte;//secondary Dao
	
	private MyMapper myMapper;
	
	static IdHelper<OperationDto,Operation,Long> operationIdHelper = new IdHelper<>(){
		@Override public Long extractEntityId(Operation e) {return e.getNumOp();}
		@Override public Long extractDtoId(OperationDto dto) {return dto.getNumOp();}
		@Override public void setDtoId(OperationDto dto, Long id) { dto.setNumOp(id); }
	};
	
	@Autowired
	public ServiceOperationWithDtoImpl(DaoOperation daoOperation,DaoCompte daoCompte , MyMapper myMapper) {
		super(OperationDto.class, OperationDto.class,Operation.class, daoOperation,operationIdHelper);
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
