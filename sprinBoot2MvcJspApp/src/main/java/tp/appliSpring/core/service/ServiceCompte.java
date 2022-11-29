package tp.appliSpring.core.service;

import java.util.List;

import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.exception.NotFoundException;

public interface ServiceCompte {
      Compte rechercherCompteParNumero(long numero);
      Compte rechercherCompteParNumeroAvecEx(long numero)throws NotFoundException;
      List<Compte> rechercherTousComptes();
      List<Compte> rechercherComptesDuClient(long numClient);
      Compte sauvegarderCompte(Compte compte);
      void supprimerCompte(long numCpt);
      void transferer(double montant,long numCptDeb,long numCptCred);
}
