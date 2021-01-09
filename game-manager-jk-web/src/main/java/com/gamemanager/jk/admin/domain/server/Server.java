package com.gamemanager.jk.admin.domain.server;

import com.gamemanager.jk.admin.domain.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Server {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;
	
	private String name;
	
	private String ip;
	
	private String port;
	
	private String agentToken;
	
	@ManyToOne
	private Customer customer;
	
	private boolean enabled;
	
	@Builder.Default
	private boolean autoUpdate = true;
	
	@Builder.Default
	private int maxPlayers = 32;
	
	@Builder.Default
	private String location = "de";
	
}
