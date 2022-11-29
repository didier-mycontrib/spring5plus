package tp.appliSpring.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.entity.Operation;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;
import tp.appliSpring.dto.OperationDto;

public class MyMapperImpl implements MyMapper {

	@Override
	public OperationDto operationToOperationDto(Operation source) {
		return new OperationDto(source.getNumOp(),source.getLabel(),source.getMontant(),source.getDateOp());
	}

	@Override
	public Operation operationDtoToOperation(OperationDto source) {
		Date dateOp = new Date();
		try {
			dateOp = (new SimpleDateFormat("yyy-MM-dd")).parse(source.getDateOp());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Operation(source.getNumOp(),source.getLabel(),source.getMontant(), dateOp);
	}

	@Override
	public ClientDto clientToClientDto(Client source) {
		return new ClientDto(source.getNumero(),source.getPrenom(),source.getNom(),source.getEmail(),source.getAdresse());
	}

	@Override
	public ClientDtoEx clientToClientDtoEx(Client source) {
		ClientDtoEx clientDtoEx = new ClientDtoEx();
		BeanUtils.copyProperties(source, clientDtoEx);
		clientDtoEx.setComptes(MyGenericMapper.map(source.getComptes(),CompteDto.class));
		return clientDtoEx;
	}

	@Override
	public Client clientDtoToClient(ClientDto source) {
		return new Client(source.getNumero(),source.getPrenom(),source.getNom(),source.getEmail(),source.getAdresse());
	}

	@Override
	public Client clientDtoExToClient(ClientDtoEx source) {
		Client client = new Client();
		BeanUtils.copyProperties(source, client);
		client.setComptes(MyGenericMapper.map(source.getComptes(),Compte.class));
		return client;
	}

	@Override
	public CompteDto compteToCompteDto(Compte compte) {
		return new CompteDto(compte.getNumero(),compte.getLabel(),compte.getSolde());
	}

	@Override
	public Compte compteDtoToCompte(CompteDto compteDto) {
		return new Compte(compteDto.getNumero(),compteDto.getLabel(),compteDto.getSolde());
	}

	@Override
	public CompteDtoEx compteToCompteDtoEx(Compte compte) {
		CompteDtoEx compteDtoEx = new CompteDtoEx();
		BeanUtils.copyProperties(compte, compteDtoEx);
		compteDtoEx.setOperations(MyGenericMapper.map(compte.getOperations(),OperationDto.class));
		return compteDtoEx;
	}

	@Override
	public Compte compteDtoExToCompte(CompteDtoEx compteDto) {
		Compte compte = new Compte();
		BeanUtils.copyProperties(compteDto, compte);
		compte.setOperations(MyGenericMapper.map(compteDto.getOperations(),Operation.class));
		return compte;
	}

}
