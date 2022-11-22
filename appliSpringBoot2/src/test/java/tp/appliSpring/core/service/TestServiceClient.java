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

import tp.appliSpring.AppliSpringBootApplication;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;
import tp.appliSpring.dto.CompteDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes={AppliSpringBootApplication.class}) //java config
@ActiveProfiles({"dev"})//pour tenir compte de application-dev.properties
public class TestServiceClient {
	
	private static Logger logger = LoggerFactory.getLogger(TestServiceClient.class);
	
	@Autowired
	private ServiceClientWithDto serviceClient; //à tester
	
	@Autowired
	private ServiceCompteWithDto serviceCompte; //pour aider à tester
	
	@Test
	public void testerRechercherUnClient() {
		CompteDto compteX1Sauvegarde = serviceCompte.saveNew(new CompteDto(null,"CompteX1",256.0));
		CompteDto compteX2Sauvegarde = serviceCompte.saveNew(new CompteDto(null,"CompteX2",156.0));
		ClientDtoEx clientXy = new ClientDtoEx(null,"jean","Xxx","12 rue Elle 75001 Paris","email1");
		clientXy.getComptes().add(compteX1Sauvegarde);
		clientXy.getComptes().add(compteX2Sauvegarde);
		ClientDto clientXySauvegarde= serviceClient.saveNew(clientXy);
		//ClientDto clientXyRelu = serviceClient.searchById(clientXySauvegarde.getNumber());
		ClientDtoEx clientXyRelu = serviceClient.searchCustomerWithAccountsById(clientXySauvegarde.getNumber());
		logger.debug("clientXyRelu="+clientXyRelu.toString());
		for(CompteDto cpt : clientXyRelu.getComptes()) {
			logger.debug("\t"+cpt);
		}
	}
	
	
	
	@Test
	public void testRechercherTousLesClients() {
		serviceClient.saveNew(new ClientDto(null,"jean","Bon","12 rue Elle 75001 Paris","email1"));
		serviceClient.saveNew(new ClientDto(null,"axelle","Air","2 rue xy 69001 Lyon","email2"));
		List<ClientDto> clients = serviceClient.searchAll();
		logger.debug("clients="+clients);
		Assertions.assertTrue(clients!= null && clients.size()>=2);
	}
	
	@Test
	 public void testAjoutEtRelectureEtSuppression() {
			//hypothese : base avec tables vides et existantes au lancement du test
				ClientDto client = new ClientDto(null,"alex","Therieur","2 rue xy 75002 Paris","emailQuiVaBien");
				ClientDto clientSauvegarde = this.serviceClient.saveNew(client); //INSERT INTO
				logger.debug("ClientSauvegarde=" + clientSauvegarde);
				ClientDto clientRelu = this.serviceClient.searchById(clientSauvegarde.getNumber()); 
				Assertions.assertEquals("alex",clientRelu.getFirstName());
				Assertions.assertEquals("Therieur",clientRelu.getLastName());
				logger.debug("ClientRelu apres insertion=" + clientRelu);
				clientSauvegarde.setFirstName("alain"); 
				ClientDto clientMisAjour = this.serviceClient.updateExisting(clientSauvegarde); //UPDATE
				logger.debug("ClientMisAjour=" + clientMisAjour);
				clientRelu = this.serviceClient.searchById(clientSauvegarde.getNumber());  //SELECT
				Assertions.assertEquals("alain",clientRelu.getFirstName());
				
				logger.debug("clientRelu apres miseAjour=" + clientRelu);
				//+supprimer :
				this.serviceClient.deleteById(clientSauvegarde.getNumber());
				//verifier bien supprimé (en tentant une relecture qui renvoi null)
				boolean clientEncoreExistant =
				 this.serviceClient.existsById(clientSauvegarde.getNumber()); 
				Assertions.assertFalse(clientEncoreExistant);
			}
	
	
}
