package com.gamemanager.jk.admin.app;

import com.gamemanager.jk.admin.app.model.MessageModel;
import com.gamemanager.jk.admin.app.model.UserEditInput;
import com.gamemanager.jk.admin.app.model.UserNewInput;
import com.gamemanager.jk.admin.domain.customer.Customer;
import com.gamemanager.jk.admin.domain.customer.CustomerRepository;
import com.gamemanager.jk.admin.domain.server.Server;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import com.gamemanager.jk.admin.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/{customerId}")
@AllArgsConstructor
public class CustomerPanelController {
	
	private final ServerRepository serverRepository;
	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/dash")
	public String dash(@PathVariable UUID customerId) {
		return "customer-panel";
	}
	
	@GetMapping("/servers")
	public String servers(@PathVariable("customerId") UUID customerId, Model model) {
		List<Server> servers = serverRepository.findAllByCustomerId(customerId);
		model.addAttribute("servers", servers);
		return "customer-panel/servers";
	}
	
	@GetMapping("/users")
	public String users(@PathVariable("customerId") UUID customerId, Model model) {
		List<User> users = userRepository.findAllByCustomerId(customerId);
		model.addAttribute("users", users);
		return "customer-panel/users";
	}
	
	@GetMapping("/users/new")
	@PreAuthorize("hasAnyRole('ROOT_BUSINESS', 'ADMIN_BUSINESS', 'ROOT_CUSTOMER', 'ADMIN_CUSTOMER')")
	public String usersNew(@PathVariable("customerId") UUID customerId,
							@AuthenticationPrincipal User authUser,
							Model model) {
		model.addAttribute("userNewInput", new UserNewInput());
		model.addAttribute("roles", User.Role.getRolesAvailableFor(authUser));
		return "customer-panel/users-new";
	}
	
	@PostMapping("/users/new")
	@PreAuthorize("hasAnyRole('ROOT_BUSINESS', 'ADMIN_BUSINESS', 'ROOT_CUSTOMER', 'ADMIN_CUSTOMER')")
	public String usersNewSave(@PathVariable("customerId") UUID customerId,
								@Valid UserNewInput userNewInput,
								BindingResult bindingResult,
								Model model,
								@AuthenticationPrincipal User authUser,
								RedirectAttributes attributes) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("userNewInput", userNewInput);
			model.addAttribute("roles", User.Role.getRolesAvailableFor(authUser));
			return "customer-panel/users-new";
		}
		
		Customer customer = customerRepository.findById(customerId).orElseThrow(EntityNotFoundException::new);
		
		User newUser = User.builder()
				.email(userNewInput.getEmail())
				.name(userNewInput.getName())
				.role(userNewInput.getRole())
				.password(passwordEncoder.encode(userNewInput.getInitialPassword()))
				.customer(customer)
				.enabled(true)
				.build();
		
		userRepository.save(newUser);
		
		attributes.addFlashAttribute("message", MessageModel.success("User created."));
		
		return String.format("redirect:/%s/users", customerId);
	}
	
	
	@GetMapping("/users/{userId}")
	@PreAuthorize("hasAnyRole('ROOT_BUSINESS', 'ADMIN_BUSINESS', 'ROOT_CUSTOMER', 'ADMIN_CUSTOMER')")
	public String usersEdit(@PathVariable("customerId") UUID customerId,
							@PathVariable("userId") UUID userId,
							@Valid UserEditInput userEditInput,
							BindingResult bindingResult,
							Model model,
							@AuthenticationPrincipal User authUser) {
		
		User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
		
		model.addAttribute("userEditInput", new UserEditInput(user));
		model.addAttribute("roles", User.Role.getRolesAvailableFor(authUser));
		
		return "customer-panel/users-edit";
	}
	
	@PostMapping("/users/{userId}")
	@PreAuthorize("hasAnyRole('ROOT_BUSINESS', 'ADMIN_BUSINESS', 'ROOT_CUSTOMER', 'ADMIN_CUSTOMER')")
	public String usersEditSave(@PathVariable("customerId") UUID customerId,
								@PathVariable("userId") UUID userId,
								@Valid UserEditInput userEditInput,
								BindingResult bindingResult,
								Model model,
								@AuthenticationPrincipal User authUser,
								RedirectAttributes attributes) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("userEditInput", userEditInput);
			model.addAttribute("roles", User.Role.getRolesAvailableFor(authUser));
			return "customer-panel/users-edit";
		}
		
		User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
		
		user.setName(userEditInput.getName());
		user.setEmail(userEditInput.getEmail());
		user.setRole(userEditInput.getRole());
		user.setEnabled(userEditInput.getEnabled());
		
		userRepository.save(user);
		
		attributes.addFlashAttribute("message", MessageModel.success("User updated."));
		
		return String.format("redirect:/%s/users", customerId);
	}
	
}
