package tp.appliSpring.core.service;

import java.util.List;

import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.exception.SoldeInsuffisantException;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;

public interface ServiceCompteWithDto extends GenericServiceWithDto<CompteDto,CompteDtoEx,Long>{
	
	public CompteDtoEx searchCompteWithOperationsById(Long numCompte);

	List<CompteDto> searchCustomerAccounts(Long numClient);

	List<CompteDto> searchAccountsWithMinimumBalance(Double soldeMini);

	public void transfer(double montant, long numCptDeb, long numCptCred) throws SoldeInsuffisantException;
	
	boolean verifierPasDecouvert(long numCpt) throws NotFoundException;
}
