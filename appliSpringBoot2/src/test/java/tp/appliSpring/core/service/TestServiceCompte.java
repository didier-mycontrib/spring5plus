package tp.appliSpring.core.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;
import tp.appliSpring.dto.OperationDto;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes={AppliSpringBootApplication.class}) // java config
@SpringBootTest(classes={ConfigSuffisante.class})
//@ContextConfiguration(classes={ConfigSuffisante.class})
@ActiveProfiles({"dev"})//pour tenir compte de application-dev.properties
public class TestServiceCompte {

	private static Logger logger = LoggerFactory.getLogger(TestServiceCompte.class);

	@Autowired
	private ServiceCompteWithDto serviceCompteDto; // à tester

	@Autowired
	private ServiceClientWithDto serviceClient; // pour aider à tester

	@Autowired
	private ServiceOperationWithDto serviceOperation; // pour aider à tester

	/*
	 * @BeforeEach //ou bien @BeforeAll public void initialisation() {
	 * AnnotationConfigApplicationContext springContext = new
	 * AnnotationConfigApplicationContext(MySpringApplication.class) ;
	 * 
	 * this.serviceCompteDto = springContext.getBean(ServiceCompteDto.class); }
	 */

	@Test
	public void testVirement() {
		CompteDto compteDtoBSauvegarde = this.serviceCompteDto.saveNew(new CompteDto(null, "compteDtoB", 300.0));
		CompteDto CompteDtoBSauvegarde = this.serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoB", 100.0));
		long numCptA = compteDtoBSauvegarde.getNumero();
		long numCptB = CompteDtoBSauvegarde.getNumero();
		// remonter en memoire les anciens soldes des CompteDto A et B avant virement
		// (+affichage console ou logger) :
		double soldeA_avant = compteDtoBSauvegarde.getSolde();
		double soldeB_avant = CompteDtoBSauvegarde.getSolde();
		logger.debug("avant bon virement, soldeA_avant=" + soldeA_avant + " et soldeB_avant=" + soldeB_avant);
		// effectuer un virement de 50 euros d'un CompteDto A vers vers CompteDto B
		this.serviceCompteDto.transfer(50.0, numCptA, numCptB);
		// remonter en memoire les nouveaux soldes des CompteDto A et B apres virement
		// (+affichage console ou logger)
		CompteDto compteDtoBReluApresVirement = this.serviceCompteDto.searchById(numCptA);
		CompteDto CompteDtoBReluApresVirement = this.serviceCompteDto.searchById(numCptB);
		double soldeA_apres = compteDtoBReluApresVirement.getSolde();
		double soldeB_apres = CompteDtoBReluApresVirement.getSolde();
		logger.debug("apres bon virement, soldeA_apres=" + soldeA_apres + " et soldeB_apres=" + soldeB_apres);
		// verifier -50 et +50 sur les différences de soldes sur A et B :
		Assertions.assertEquals(soldeA_avant - 50, soldeA_apres, 0.000001);
		Assertions.assertEquals(soldeB_avant + 50, soldeB_apres, 0.000001);
		//logger.debug("après bon virement, operations sur compteDtoB:" + daoOperation.findByAccountNumber(numCptA));
		//logger.debug("après bon virement, operations sur CompteDtoB:" + daoOperation.findByAccountNumber(numCptB));
		this.serviceCompteDto.transfer(0.05, numCptA, numCptB); //second virement pour voir plusieurs operations
		logger.debug("après bon virement, operations sur compteDtoB:" + serviceOperation.searchById(numCptA));
		logger.debug("après bon virement, operations sur CompteDtoB:" + serviceOperation.searchById(numCptB));
	}

	@Test
	public void testMauvaisVirement() {
		
		CompteDto compteDtoBSauvegarde = this.serviceCompteDto.saveNew(new CompteDto(null, "compteDtoB", 300.0));
		CompteDto CompteDtoBSauvegarde = this.serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoB", 100.0));
		long numCptA = compteDtoBSauvegarde.getNumero();
		long numCptB = CompteDtoBSauvegarde.getNumero();
		// remonter en memoire les anciens soldes des CompteDto A et B avant virement
		// (+affichage console ou logger) :
		double soldeA_avant = compteDtoBSauvegarde.getSolde();
		double soldeB_avant = CompteDtoBSauvegarde.getSolde();
		logger.debug("avant mauvais virement, soldeA_avant=" + soldeA_avant + " et soldeB_avant=" + soldeB_avant);
		// effectuer un virement de 50 euros d'un CompteDto A vers vers CompteDto B
		try {
			this.serviceCompteDto.transfer(50.0, numCptA, -numCptB); //erreur voulue
		} catch (Exception e) {
			logger.error("echec normal du virement " + e.getMessage()); 
		}
		// remonter en memoire les nouveaux soldes des CompteDto A et B apres virement
		// (+affichage console ou logger)
		CompteDto compteDtoBReluApresVirement = this.serviceCompteDto.searchById(numCptA);
		CompteDto CompteDtoBReluApresVirement = this.serviceCompteDto.searchById(numCptB);
		double soldeA_apres = compteDtoBReluApresVirement.getSolde();
		double soldeB_apres = CompteDtoBReluApresVirement.getSolde();
		logger.debug("apres mauvais virement, soldeA_apres=" + soldeA_apres + " et soldeB_apres=" + soldeB_apres);
		// verifier -50 et +50 sur les différences de soldes sur A et B :
		Assertions.assertEquals(soldeA_avant , soldeA_apres, 0.000001);
		Assertions.assertEquals(soldeB_avant , soldeB_apres, 0.000001);
	}

	@Test
	public void testRechercherTousLesCompteDtos() {
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoZ1", 256.0));
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoZ2", 156.0));
		List<CompteDto> CompteDtos = serviceCompteDto.searchAll();
		logger.debug("CompteDtos=" + CompteDtos);
	}
	
	@Test
	public void testVerifierPasDecouvert() {
		CompteDto compteDtoSauvegarde = serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoPositif", 256.0));
		boolean bPasDecouvert = serviceCompteDto.verifierPasDecouvert(compteDtoSauvegarde.getNumero());
		logger.debug("bPasDecouvert=" + bPasDecouvert);
		Assertions.assertTrue(bPasDecouvert);
		
		CompteDto compteDtoSauvegarde2 = serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoNegatif", -256.0));
		boolean bPasDecouvert2 = serviceCompteDto.verifierPasDecouvert(compteDtoSauvegarde2.getNumero());
		logger.debug("bPasDecouvert2=" + bPasDecouvert2);
		Assertions.assertFalse(bPasDecouvert2);
	}
	
	
	@Test
	public void testRechercherCompteDtosAvecSoldeMini() {
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoW1", 256.0));
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoW3", -6.0));
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoW2", 156.0));
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoW4", -16.0));
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoW5", 234.0));
		serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoW6", 22.0));
		List<CompteDto> CompteDtos = serviceCompteDto.searchAccountsWithMinimumBalance(0.0);
		logger.debug("CompteDtos avec soldes positifs (par ordre croissants)=" + CompteDtos);
	}

	@Test
	public void testRechercherCompteDtosDunClient() {
		CompteDto compteDtoB1Sauvegarde = serviceCompteDto.saveNew(new CompteDto(null, "compteDtoB1", 256.0));
		CompteDto compteDtoB2Sauvegarde = serviceCompteDto.saveNew(new CompteDto(null, "compteDtoB2", 156.0));
		ClientDtoEx clientA = new ClientDtoEx(null, "aaa", "HaHa", "12 rue Elle 75001 Paris", "email1");
		clientA.getComptes().add(compteDtoB1Sauvegarde);
		clientA.getComptes().add(compteDtoB2Sauvegarde);
		clientA = serviceClient.saveNewEx(clientA);

		CompteDto CompteDtoB1Sauvegarde = serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoB1", 236.0));
		CompteDto CompteDtoB2Sauvegarde = serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoB2", 136.0));
		ClientDtoEx clientB = new ClientDtoEx(null, "bbb", "BeBe", "12 rue Elle 75002 Paris", "email2");
		clientB.getComptes().add(CompteDtoB1Sauvegarde);
		clientB.getComptes().add(CompteDtoB2Sauvegarde);
		clientB = serviceClient.saveNewEx(clientB);

		List<CompteDto> CompteDtosDuClientA = serviceCompteDto.searchCustomerAccounts(clientA.getNumber());
		logger.debug("CompteDtosDuClientA=" + CompteDtosDuClientA);
		Assertions.assertTrue(CompteDtosDuClientA.size() == 2);

		List<CompteDto> CompteDtosDuClientB = serviceCompteDto.searchCustomerAccounts(clientB.getNumber());
		logger.debug("CompteDtosDuClientB=" + CompteDtosDuClientB);
	}
	

	@Test
	// @Order(1)
	public void testRechercherCompteDtoWithOperations() {
		CompteDto compteDtoXy = serviceCompteDto.saveNew(new CompteDto(null, "CompteDtoXy", 200.0));
		Long numCompteDtoXy = compteDtoXy.getNumero(); // numero auto incrémenté

		OperationDto opXy1 = new OperationDto(null, "achat bonbons", -4.67);
		opXy1 = serviceOperation.saveNewWithAccountNumber(opXy1,numCompteDtoXy);
		logger.debug("opXy1="+opXy1);

		OperationDto opXy2 = new OperationDto(null, "achat gateau", -14.67);
		opXy2 = serviceOperation.saveNewWithAccountNumber(opXy2,numCompteDtoXy);
		logger.debug("opXy2="+opXy2);

		// CompteDto compteDtoXyRelu = serviceCompteDto.searchById(numCompteDtoXy);
		CompteDtoEx compteDtoXyRelu = serviceCompteDto.searchCompteWithOperationsById(numCompteDtoXy);
		logger.debug("compteDtoXyRelu=" + compteDtoXyRelu);
		Assertions.assertTrue(compteDtoXyRelu.getNumero() == numCompteDtoXy);
		logger.debug("operations du CompteDto CompteDtoXy:");
		for (OperationDto op : compteDtoXyRelu.getOperations()) {
			logger.debug("\t" + op);
		}

	}

	@Test
	// @Order(2)
	public void testAjoutEtRelectureEtSuppression() {
		// hypothese : base avec tables vides et existantes au lancement du test
		CompteDto compteDto = new CompteDto(null, "compteDtoB", 100.0);
		CompteDto compteDtoSauvegarde = this.serviceCompteDto.saveNew(compteDto); // INSERT INTO
		logger.debug("CompteDtoSauvegarde=" + compteDtoSauvegarde);
		CompteDto compteDtoRelu = this.serviceCompteDto.searchById(compteDtoSauvegarde.getNumero());
		Assertions.assertEquals("compteDtoB", compteDtoRelu.getLabel());
		Assertions.assertEquals(100.0, compteDtoRelu.getSolde());
		logger.debug("CompteDtoRelu apres insertion=" + compteDtoRelu);
		compteDtoSauvegarde.setSolde(150.0);
		compteDtoSauvegarde.setLabel("CompteDto_a");
		CompteDto compteDtoMisAjour = this.serviceCompteDto.updateExisting(compteDtoSauvegarde,compteDtoSauvegarde.getNumero()); // UPDATE
		logger.debug("compteDtoMisAjour=" + compteDtoMisAjour);
		compteDtoRelu = this.serviceCompteDto.searchById(compteDtoSauvegarde.getNumero()); // SELECT
		Assertions.assertEquals("CompteDto_a", compteDtoRelu.getLabel());
		Assertions.assertEquals(150.0, compteDtoRelu.getSolde());
		logger.debug("CompteDtoRelu apres miseAjour=" + compteDtoRelu);
		// +supprimer :
		this.serviceCompteDto.deleteById(compteDtoSauvegarde.getNumero());
		if(this.serviceCompteDto.existsById(compteDtoSauvegarde.getNumero()))
		    Assertions.fail("compte pas bien supprimé");
	}

}
