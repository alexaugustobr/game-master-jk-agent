package com.gamemanager.jk.admin;

import com.gamemanager.jk.admin.domain.customer.Customer;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.customer.CustomerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DBSetup implements ApplicationRunner {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomerRepository customerRepository;
	private final ServerRepository serverRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!userRepository.findAll().isEmpty()) {
			return;
		}
		
		User systemUser = User.builder()
				.email("system@gmail.com")
				.role(User.Role.ROOT)
				.type(User.Type.SYSTEM)
				.name("System")
				.password(null)
				.enabled(false)
				.build();
		
		userRepository.save(systemUser);
		
		User root = User.builder()
				.email("root@chozat.com")
				.role(User.Role.ROOT)
				.type(User.Type.BUSINESS)
				.name("Root")
				.password(passwordEncoder.encode("chozo"))
				.enabled(true)
				.build();
		
		userRepository.save(root);
		
		User admin = User.builder()
				.email("admin@chozat.com")
				.role(User.Role.ADMIN)
				.type(User.Type.BUSINESS)
				.name("Admin")
				.password(passwordEncoder.encode("chozo"))
				.enabled(true)
				.build();
		
		userRepository.save(admin);
		
		
		
		
		
		
		
		String tfsAgentToken = UUID.randomUUID().toString();
		
		Customer tfs = Customer.builder()
				.enabled(true)
				.name("TFS")
				.build();
		
		customerRepository.save(tfs);
		
		User bob = User.builder()
				.email("bob@gmail.com")
				.role(User.Role.ADMIN)
				.type(User.Type.CUSTOMER)
				.name("Bob")
				.password(passwordEncoder.encode("chozo"))
				.enabled(true)
				.customer(tfs)
				.build();
		
		userRepository.save(bob);
		
		Server barn = Server.builder()
				.name("TFS - Barn FA")
				.enabled(true)
				.customer(tfs)
				.ip("159.69.214.101")
				.port("29071")
				.agentToken(tfsAgentToken)
				.build();
		
		serverRepository.save(barn);
		
		Server pigsty = Server.builder()
				.name("TFS - Pigsty")
				.enabled(true)
				.customer(tfs)
				.ip("159.69.214.101")
				.port("29073")
				.agentToken(tfsAgentToken)
				.build();
		
		serverRepository.save(pigsty);
		
		Server lab = Server.builder()
				.name("TFS - Lab")
				.enabled(true)
				.customer(tfs)
				.ip("159.69.214.101")
				.port("29072")
				.agentToken(tfsAgentToken)
				.build();
		
		serverRepository.save(lab);
		
		
		
		
		
		
		
		
		
		
		Customer exe = Customer.builder()
				.enabled(true)
				.name("EXE")
				.build();
		
		customerRepository.save(exe);
		
		String exeAgentToken = UUID.randomUUID().toString();
		
		Server paro = Server.builder()
				.name("{EXE}-PAROXYSM EU")
				.enabled(true)
				.customer(exe)
				.ip("157.90.28.53")
				.port("29070")
				.agentToken(exeAgentToken)
				.build();
		
		serverRepository.save(paro);
		
		User noob = User.builder()
				.email("noob@gmail.com")
				.role(User.Role.ADMIN)
				.type(User.Type.CUSTOMER)
				.name("noob")
				.password(passwordEncoder.encode("chozo"))
				.enabled(true)
				.customer(exe)
				.build();
		
		userRepository.save(noob);
		
		
		
		
		
		
		
		
		
		
		Customer emp = Customer.builder()
				.enabled(true)
				.name("EMP")
				.build();
		
		customerRepository.save(emp);
		
		User sav = User.builder()
				.email("sav@gmail.com")
				.role(User.Role.ADMIN)
				.type(User.Type.CUSTOMER)
				.name("Sav")
				.password(passwordEncoder.encode("chozo"))
				.enabled(true)
				.customer(emp)
				.build();
		
		userRepository.save(sav);
		
		String empToken = UUID.randomUUID().toString();
		
		Server empduel = Server.builder()
				.name("DE Empire Duel [Ranked]")
				.enabled(true)
				.customer(emp)
				.ip("88.198.112.31")
				.port("29071")
				.agentToken(empToken)
				.build();
		serverRepository.save(empduel);
		
		Server t1 = Server.builder()
				.name("Turtles 1.4.9")
				.enabled(true)
				.customer(emp)
				.ip("116.203.181.181")
				.port("29070")
				.agentToken(empToken)
				.build();
		
		serverRepository.save(t1);
		
		Server t2 = Server.builder()
				.name("Turtles 1.5.3")
				.enabled(true)
				.customer(emp)
				.ip("116.203.181.181")
				.port("29071")
				.agentToken(empToken)
				.build();
		
		serverRepository.save(t2);
		
		
		
		
		
		
		
		
		
		Customer dn = Customer.builder()
				.enabled(true)
				.name("DN")
				.build();
		
		customerRepository.save(dn);
		
		User linl = User.builder()
				.email("linl@gmail.com")
				.role(User.Role.ADMIN)
				.type(User.Type.CUSTOMER)
				.name("Linl")
				.password(passwordEncoder.encode("chozo"))
				.enabled(true)
				.customer(dn)
				.build();
		
		userRepository.save(linl);
		
		String dnToken = UUID.randomUUID().toString();
		
		Server dnServer = Server.builder()
				.name("{DN} Relax & chill")
				.enabled(true)
				.customer(dn)
				.ip("116.203.157.129")
				.port("29070")
				.agentToken(dnToken)
				.build();
		
		serverRepository.save(dnServer);
		
		
		
		
	}
}
