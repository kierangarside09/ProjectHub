package com.projecthub.controllers;

import com.projecthub.entities.Account;
import com.projecthub.entities.Profile;
import com.projecthub.repositories.AccountRepository;
import com.projecthub.repositories.ProfileRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AccountRepository accountRepository;

    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(AccountRepository accountRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Getter
    @Setter
    public static class RegisterForm {
        @NotBlank(message = "Display name is required")
        @Size(min = 3, max = 30, message = "Display name must be 3–30 characters")
        private String displayName;

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        @NotBlank(message = "Please confirm your password")
        private String confirmPassword;
    }

    @GetMapping("/login")
    public String login() {
        return "login/index";
    }

    @GetMapping("/register")
    public String index(Model model){
        model.addAttribute("registerForm", new RegisterForm());
        return "register/index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterForm registerForm, BindingResult bindingResult) {
        //this returns errors via bindingResult which comes from springs built in validation
        if (bindingResult.hasErrors()) {
            return "register/index";
        }
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
            return "register/index";
        }
        if (accountRepository.existsByEmail(registerForm.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "That email is already registered");
            return "register/index";
        }

        Account account = accountRepository.save(new Account(registerForm.getEmail(), passwordEncoder.encode(registerForm.getPassword())));
        Profile profile = new Profile();
        profile.setAccount(account);
        profile.setDisplayName(registerForm.getDisplayName());
        profileRepository.save(profile);

        return "redirect:/login?registered";
    }
}
