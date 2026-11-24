package com.projecthub.controllers;

import com.projecthub.entities.Account;
import com.projecthub.entities.Profile;
import com.projecthub.enums.Gender;
import com.projecthub.enums.LookingFor;
import com.projecthub.repositories.AccountRepository;
import com.projecthub.repositories.ProfileRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    private final AccountRepository accountRepository;

    private final ProfileRepository profileRepository;

    public ProfileController(AccountRepository accountRepository, ProfileRepository profileRepository) {
        this.accountRepository = accountRepository;
        this.profileRepository = profileRepository;
    }

    @Getter
    @Setter
    public static class ProfileForm {
        @NotBlank(message = "Display name is required")
        @Size(min = 3, max = 30, message = "Display name must be 3–30 characters")
        private String displayName;

        @Size(max = 100, message = "Location must be 100 characters or fewer")
        private String location;

        @Size(max = 1000, message = "Bio must be 1000 characters or fewer")
        private String bio;

        @Size(max = 200, message = "Skills must be 200 characters or fewer")
        private String skills;

        private Gender gender;

        private LookingFor lookingFor;
    }

    @GetMapping("/profile")
    public String index(Model model, Principal principal) {
        model.addAttribute("profile", getProfile(principal));
        return "profile/index";
    }

    @GetMapping("/profile/edit")
    public String edit(Model model, Principal principal) {
        Profile profile = getProfile(principal);
        ProfileForm profileForm = new ProfileForm();
        profileForm.setDisplayName(profile.getDisplayName());
        profileForm.setLocation(profile.getLocation());
        profileForm.setBio(profile.getBio());
        profileForm.setSkills(profile.getSkills());
        profileForm.setGender(profile.getGender());
        profileForm.setLookingFor(profile.getLookingFor());
        model.addAttribute("profileForm", profileForm);
        addEnumOptions(model);
        return "profile/edit";
    }

    @PostMapping("/profile/edit")
    public String editPost(@Valid @ModelAttribute ProfileForm profileForm, BindingResult bindingResult,
                           Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            addEnumOptions(model);
            return "profile/edit";
        }
        Profile profile = getProfile(principal);
        profile.setDisplayName(profileForm.getDisplayName());
        profile.setLocation(profileForm.getLocation());
        profile.setBio(profileForm.getBio());
        profile.setSkills(profileForm.getSkills());
        profile.setGender(profileForm.getGender());
        profile.setLookingFor(profileForm.getLookingFor());
        profileRepository.save(profile);
        return "redirect:/profile";
    }

    private void addEnumOptions(Model model) {
        model.addAttribute("genders", Gender.values());
        model.addAttribute("lookingForOptions", LookingFor.values());
    }

    private Profile getProfile(Principal principal) {
        Account account = accountRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        return profileRepository.findById(account.getId()).orElseGet(() -> {
            Profile profile = new Profile();
            profile.setAccount(account);
            return profileRepository.save(profile);
        });
    }
}
