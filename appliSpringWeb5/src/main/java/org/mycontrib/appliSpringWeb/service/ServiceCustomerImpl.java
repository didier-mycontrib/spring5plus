package org.mycontrib.appliSpringWeb.service;

import java.util.List;

import org.mycontrib.appliSpringWeb.dao.DaoCompte;
import org.mycontrib.appliSpringWeb.dao.DaoCustomer;
import org.mycontrib.appliSpringWeb.entity.Customer;
import org.mycontrib.util.generic.service.AbstractGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceCustomerImpl 
    extends AbstractGenericService<Customer, Long>
    implements ServiceCustomer {
	
	@Override
	public CrudRepository<Customer, Long> getMainDao() {
		return this.daoCustomer;
	}
	
	@Autowired(required=false)
	private PasswordEncoder passwordEncoder;
	
	
	Logger logger = LoggerFactory.getLogger(ServiceCustomerImpl.class);
	
	
	@Autowired
	private DaoCustomer daoCustomer; //dao principal
	
	@Autowired
	private DaoCompte daoCompte;  //dao secondaire/annexe
	

	@Override
	public boolean checkCustomerPassword(long customerId, String password) {
		Customer customer = daoCustomer.findById(customerId).orElse(null);
		if(customer==null || password==null) return false;
		if(this.passwordEncoder!=null) {
			return passwordEncoder.matches(password,customer.getPassword());
		}
		else {
		    return password.equals(customer.getPassword());
		}
	}

	@Override
	public String resetCustomerPassword(long customerId) {
		Customer customer = daoCustomer.findById(customerId).get();
		String pwd = "tempPwd"; //à améliorer
		customer.setPassword(pwd);
		saveOrUpdateEntity(customer);
		return pwd;
	}
	
	

	
	@Override  //override default implementation to encore password via passwordEncoder( bcrypt )
	public Customer saveOrUpdateEntity(Customer entity) {
		if(this.passwordEncoder!=null) {
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		}
		return super.saveOrUpdateEntity(entity);
	}

	@Override
	public Customer rechercherCustomerAvecComptesParNumero(long idCustomer) {
		return daoCustomer.findByIdWithComptes(idCustomer).orElse(null);
	}

	@Override
	public List<Customer> rechercherCustomerSelonPrenomEtNom(String prenom, String nom) {
		return daoCustomer.findByFirstnameAndLastname(prenom, nom);
	}

	@Override
	public void initEntityRelationShipsFromDtoBeforeSave(Customer entity, Object dto) {
		// TODO Auto-generated method stub
	}

	
	
}
