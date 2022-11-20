package tp.appliSpring.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tp.appliSpring.converter.MyGenericMapper;
import tp.appliSpring.converter.MyMapper;
import tp.appliSpring.core.dao.DaoCompte;
import tp.appliSpring.core.dao.DaoOperation;
import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.entity.Operation;
import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.exception.SoldeInsuffisantException;
import tp.appliSpring.core.service.ServiceCompteWithDto;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;

@Service
@Transactional
public class ServiceCompteWithDtoImpl 
  extends AbstractGenericStdServiceWithDto<CompteDto,Long,Compte,DaoCompte> 
  implements ServiceCompteWithDto{
	
	private DaoCompte daoCompte; //for specific methods of this class
	private DaoOperation daoOperation;
	
	private MyMapper myMapper;
	
	@Autowired
	public ServiceCompteWithDtoImpl(DaoCompte daoCompte,DaoOperation daoOperation ,MyMapper myMapper) {
		super(CompteDto.class, Compte.class, daoCompte);
		this.daoCompte=daoCompte;
		this.daoOperation=daoOperation;
		this.myMapper=myMapper;
	}

	@Override
	public List<CompteDto> searchCustomerAccounts(Long numClient) {
		return MyGenericMapper.map(daoCompte.findByClientsNumero(numClient),CompteDto.class);
	}

	@Override
	public List<CompteDto> searchAccountsWithMinimumBalance(Double soldeMini) {
		return MyGenericMapper.map(daoCompte.findBySoldeGreaterThanEqual(soldeMini),CompteDto.class);
	};
	
	@Override
	//@Transactional(/* propagation = Propagation.REQUIRED par défaut */)
	//maintenant @Transactional est placé dans le haut de la classe
	public void transfer(double montant, long numCptDeb, long numCptCred) {
		try {
			Compte cptDeb = daoCompte.findById(numCptDeb).orElse(null);
			if(cptDeb.getSolde() < montant)
				throw new SoldeInsuffisantException("solde insuffisant sur compte " + numCptDeb);
			cptDeb.setSolde(cptDeb.getSolde() - montant);
			//créer, rattacher et enregistrer un objet Operation sur de débit
			Operation opDebit = new Operation(null, "debit suite au virement", -montant);
			opDebit.setCompte(cptDeb);	daoOperation.save(opDebit);
			daoCompte.save(cptDeb); //v1 ou v2 sans .save() si @Transactional
			
			Compte cptCred = daoCompte.findById(numCptCred).orElse(null);
			cptCred.setSolde(cptCred.getSolde() + montant);
			//créer, rattacher et enregistrer un objet Operation sur le crédit
			Operation opCredit = new Operation(null, "crédit suite au virement", montant);
			opCredit.setCompte(cptCred);	daoOperation.save(opCredit);
			daoCompte.save(cptCred); // v1  ou v2 sans .save() si @Transactional
		} catch (SoldeInsuffisantException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException("echec virement" , e);
		}
	}
	
	@Override
	public CompteDtoEx searchCompteWithOperationsById(Long numCompte) {
		Compte compteEntityWithOperations = daoCompte.findWithOperationsById(numCompte);
		return myMapper.compteToCompteDtoEx(compteEntityWithOperations);
	}
	
	@Override
	public boolean verifierPasDecouvert(long numCpt) throws NotFoundException {
		Compte compte = daoCompte.findById(numCpt).orElse(null);
		if(compte==null)
			throw new NotFoundException("compte inexistant avec numero="+numCpt);
		return (compte.getSolde() >= 0 );
	}

}
