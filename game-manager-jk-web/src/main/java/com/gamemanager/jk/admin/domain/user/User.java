package com.gamemanager.jk.admin.domain.user;

import com.gamemanager.jk.admin.domain.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
	)
	private UUID id;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private Role role;
	
	private Type type;
	
	private boolean enabled;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Customer customer;
	
	private OffsetDateTime createdAt;
	
	private OffsetDateTime updatedAt;
	
	private OffsetDateTime lastLoginAt;
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		String roleName = String.format("ROLE_%s_%s", role.name(), type.name());
		return Collections.singletonList(new SimpleGrantedAuthority(roleName));
	}
	
	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return enabled;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return enabled;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public enum Role {
		ROOT,
		ADMIN,
		ASSITANT,
		;
		
		public static List<Role> getRolesAvailableFor(User authUser) {
			List <Role> roles = new ArrayList<>(Arrays.asList(User.Role.values()));
			
			if (!authUser.getRole().equals(Role.ROOT)) {
				roles.remove(Role.ROOT);
			}
			
			return roles;
		}
	}
	
	public enum Type {
		SYSTEM,
		BUSINESS,
		CUSTOMER,
	}
}
