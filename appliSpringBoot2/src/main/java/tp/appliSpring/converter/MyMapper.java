package tp.appliSpring.converter;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.entity.Operation;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;
import tp.appliSpring.dto.OperationDto;

//@Mapper // MyMapper.INSTANCE...
@Mapper(componentModel = "spring") //for @Autowired
public interface MyMapper {
	
	MyMapper INSTANCE = Mappers.getMapper( MyMapper.class ); 
	
	OperationDto operationToOperationDto(Operation source);
	Operation operationDtoOperation(OperationDto source);
	
	@Mapping(target="number", source="numero")
	@Mapping(target="firstName", source="prenom")
	@Mapping(target="lastName", source="nom")
	@Mapping(target="address", source="adresse")
	ClientDto clientToClientDto(Client source);
	
	@InheritConfiguration 
	@Mapping(target="comptes", source="comptes" , resultType = CompteDto.class)
	ClientDtoEx clientToClientDtoEx(Client source);
	
	@Mapping(target="numero", source="number")
	@Mapping(target="prenom", source="firstName")
	@Mapping(target="nom", source="lastName")
	@Mapping(target="adresse", source="address")
	@Mapping(ignore = true, target="comptes" )
	Client clientDtoToClient(ClientDto source);
	
	@InheritConfiguration 
	@Mapping(ignore = false, target="comptes" )
	Client clientDtoExToClient(ClientDtoEx source);
	
	CompteDto compteToCompteDto(Compte compte);
	Compte compteDtoToCompte(CompteDto compteDto);
	
	CompteDtoEx compteToCompteDtoEx(Compte compte);
	Compte compteDtoExToCompte(CompteDtoEx compteDto);
}
