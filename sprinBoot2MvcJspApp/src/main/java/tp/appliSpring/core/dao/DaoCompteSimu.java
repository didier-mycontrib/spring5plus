package tp.appliSpring.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import tp.appliSpring.core.entity.Compte;

@Repository //ou bien @Component
@Qualifier("simu")
public class DaoCompteSimu implements DaoCompte{
	
	private long compteur=0;
	private Map<Long,Compte> mapComptes = new HashMap<>();

	@Override
	public Compte findById(Long numCpt) {
		return mapComptes.get(numCpt); //compte selon clef
	}

	@Override
	public Compte save(Compte compte) {
		if(compte.getNumero()==null) {
		    compte.setNumero(++compteur);  //simule auto_incrémentation
		}
		mapComptes.put(compte.getNumero(),compte); //ajoute dans la table d'association
		              //une association entre numero de commpte et le compte complet
		return compte;
	}

	@Override
	public List<Compte> findAll() {
		return new ArrayList<Compte>(mapComptes.values());
	}

	@Override
	public void deleteById(Long numCpt) {
		mapComptes.remove(numCpt);
	}

	@Override
	public List<Compte> findByNumClient(Long numClient) {
		List<Compte> comptes = new ArrayList<Compte>(mapComptes.values());
		//simulation grossière :
		//comptes de numero pair pour client de numéro pair
		//et comptes de numero impair pour client de numéro impair
		return comptes.stream().filter(c -> (c.getNumero() % 2)==(numClient %2)).toList();
	}

}
