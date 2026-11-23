package com.projecthub.controllers.register;

import com.projecthub.entities.Account;
import com.projecthub.repositories.AccountRepository;
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
public class RegisterController {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public RegisterController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Getter
    @Setter
    public static class RegisterForm {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30, message = "Username must be 3–30 characters")
        private String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        @NotBlank(message = "Please confirm your password")
        private String confirmPassword;
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
        if (accountRepository.existsByUsername(registerForm.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "That username is taken");
            return "register/index";
        }
        if (accountRepository.existsByEmail(registerForm.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "That email is already registered");
            return "register/index";
        }

        accountRepository.save(new Account(registerForm.getUsername(),registerForm.getEmail(), passwordEncoder.encode(registerForm.getPassword())));

        return "redirect:/login?registered";
    }
}
