package tp.appliSpring.converter;


import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.entity.Operation;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;
import tp.appliSpring.dto.OperationDto;

//interface habituellement implémentée via mapStruct mais pas dans ce projet (par simplicité)
public interface MyMapper {
	public static MyMapper INSTANCE = new MyMapperImpl(); 
	
	OperationDto operationToOperationDto(Operation source);
	Operation operationDtoToOperation(OperationDto source);
	
	ClientDto clientToClientDto(Client source);
	ClientDtoEx clientToClientDtoEx(Client source);
	
	Client clientDtoToClient(ClientDto source);
	Client clientDtoExToClient(ClientDtoEx source);
	
	CompteDto compteToCompteDto(Compte compte);
	Compte compteDtoToCompte(CompteDto compteDto);
	
	CompteDtoEx compteToCompteDtoEx(Compte compte);
	Compte compteDtoExToCompte(CompteDtoEx compteDto);
}
