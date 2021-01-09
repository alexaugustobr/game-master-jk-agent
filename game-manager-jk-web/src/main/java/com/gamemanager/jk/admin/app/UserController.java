package com.gamemanager.jk.admin.app;

import com.gamemanager.jk.admin.domain.user.User;
import com.gamemanager.jk.admin.domain.user.UserRepository;
import com.gamemanager.jk.admin.exception.EntityNotFoundException;
import com.gamemanager.jk.admin.app.model.MessageModel;
import com.gamemanager.jk.admin.app.model.PasswordInput;
import com.gamemanager.jk.admin.app.model.UserProfileInput;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping
@AllArgsConstructor
public class UserController {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/user")
	public String profile(Model model, @AuthenticationPrincipal User authUser) {
		User user = userRepository.findById(authUser.getId()).orElseThrow(EntityNotFoundException::new);
		model.addAttribute("userProfileInput", new UserProfileInput(user.getName(), user.getEmail()));
		return "profile";
	}
	
	@PostMapping("/user")
	public String saveProfile(@Valid UserProfileInput userProfileInput,
							  BindingResult bindingResult,
							  Model model,
							  @AuthenticationPrincipal User authUser,
							  RedirectAttributes attributes
							  ) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("userProfileInput", userProfileInput);
			return "profile";
		}
		
		User user = userRepository.findById(authUser.getId())
				.orElseThrow(EntityNotFoundException::new);
		
		user.setName(userProfileInput.getName());
		user.setEmail(userProfileInput.getEmail());
		
		userRepository.save(user);
		
		authUser.setName(userProfileInput.getName());
		authUser.setEmail(userProfileInput.getEmail());
		
		attributes.addFlashAttribute("message", MessageModel.success("User updated."));
		
		return "redirect:/user";
	}
	
	@GetMapping("/change-password")
	public String pass(@AuthenticationPrincipal User user,  Model model) {
		model.addAttribute("passwordInput", new PasswordInput());
		return "change-password";
	}
	
	@PostMapping("/change-password")
	public String passSave(@Valid PasswordInput passwordInput,
						   BindingResult bindingResult,
						   Model model,
						   @AuthenticationPrincipal User authUser,
						   RedirectAttributes attributes) {
		
		if (bindingResult.hasErrors()) {
			return "profile";
		}
		
		User user = userRepository.findById(authUser.getId())
				.orElseThrow(EntityNotFoundException::new);
		
		user.setPassword(passwordEncoder.encode(passwordInput.getPassword()));
		
		userRepository.save(user);
		
		attributes.addFlashAttribute("message", MessageModel.success("User password updated."));
		
		return "redirect:/change-password";
	}
	
}
