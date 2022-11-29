package tp.appliSpring.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tp.appliSpring.converter.MyGenericMapper;
import tp.appliSpring.core.dao.DaoClient;
import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.service.ServiceClientWithDto;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;

@Service
@Transactional
public class ServiceClientWithDtoImpl 
  extends AbstractGenericServiceWithDto<ClientDto,ClientDtoEx,Long,Client,DaoClient> 
  implements ServiceClientWithDto{
	
	private DaoClient daoClient; //for specific methods of this class
	
		
	static IdHelper<ClientDto,Client,Long> clientIdHelper = new IdHelper<>(){
		@Override public Long extractEntityId(Client e) {return e.getNumero();}
		@Override public Long extractDtoId(ClientDto dto) {return dto.getNumero();}
		@Override public void setDtoId(ClientDto dto, Long id) { dto.setNumero(id); }
	};
	
	@Autowired
	public ServiceClientWithDtoImpl(DaoClient daoClient) {
		super(ClientDto.class, ClientDtoEx.class ,Client.class, daoClient,clientIdHelper);
		this.daoClient=daoClient;
		}

	@Override
	public ClientDtoEx searchCustomerWithAccountsById(Long numClient) {
		Client client  = daoClient.findWithAccountById(numClient);
		return MyGenericMapper.map(client,ClientDtoEx.class);
	}

	@Override
	public ClientDtoEx saveNewEx(ClientDtoEx clientDtoEx) {
		Client clientEntity  = MyGenericMapper.map(clientDtoEx,Client.class);
		daoClient.save(clientEntity);
		clientDtoEx.setNumero(clientEntity.getNumero());
		return clientDtoEx;
	}

}
