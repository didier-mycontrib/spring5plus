package tp.appliSpring;

import java.util.ArrayList;
import java.util.List;

import org.mycontrib.mysecurity.config.MySecurityExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tp.appliSpring.core.service.ServiceClientWithDto;
import tp.appliSpring.dto.ClientDto;

@Profile("withSecurity")
//@Service(MySecurityExtension.MY_EXCLUSIVE_USERDETAILSSERVICE_NAME)
@Service(MySecurityExtension.MY_ADDITIONAL_USERDETAILSSERVICE_NAME)
public class MyUserDetailsService implements UserDetailsService {
	
	
	@Autowired
    private PasswordEncoder passwordEncoder;
   
	
	@Autowired
	private ServiceClientWithDto clientService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String password=null;
		if(username.equals("superAdmin")) {
			password=passwordEncoder.encode("007");//simulation password ici
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		else {
			try {
				Long numCli = Long.parseLong(username);
				ClientDto cli = clientService.searchById(numCli);
				authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				//password=cli.getPassword(); déjà stocké en base en mode crypté
				password=passwordEncoder.encode("pwd"+cli.getNumber());//simulation password ici
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				System.err.println(e.getMessage());
				throw new UsernameNotFoundException(username + " not found by MyUserDetailsService");
			}
		}
		return new User(username, password, authorities);
	} 

}
