package tp.appliSpring.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tp.appliSpring.converter.MyMapper;
import tp.appliSpring.core.dao.DaoClient;
import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.service.ServiceClientWithDto;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;

@Service
@Transactional
public class ServiceClientWithDtoImpl 
  extends AbstractGenericStdServiceWithDto<ClientDto,Long,Client,DaoClient> 
  implements ServiceClientWithDto{
	
	private DaoClient daoClient; //for specific methods of this class
	
	private MyMapper myMapper;
	
	@Autowired
	public ServiceClientWithDtoImpl(DaoClient daoClient,MyMapper myMapper) {
		super(ClientDto.class, Client.class, daoClient);
		this.daoClient=daoClient;
		this.myMapper=myMapper;
	}

	@Override
	public ClientDtoEx searchCustomerWithAccountsById(Long numClient) {
		Client client  = daoClient.findWithAccountById(numClient);
		return myMapper.clientToClientDtoEx(client);
	}

	@Override
	public ClientDtoEx saveNewEx(ClientDtoEx clientDtoEx) {
		Client clientEntity  = myMapper.clientDtoExToClient(clientDtoEx);
		daoClient.save(clientEntity);
		clientDtoEx.setNumber(clientEntity.getNumero());
		return clientDtoEx;
	}

}
