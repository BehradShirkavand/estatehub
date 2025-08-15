package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.PropertyPostActivity;
import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.entity.SellerPropertiesDto;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.service.PropertyPostActivityService;
import com.behrad.estatehub.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PropertyPostActivityController {

    private final UsersService usersService;
    private final PropertyPostActivityService propertyPostActivityService;

    @GetMapping("/dashboard/")
    public String searchProperty(Model model) {

        Object currentUserProfile = usersService.getCurrentUserProfile();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Seller"))) {

                List<SellerPropertiesDto> sellerProperties = propertyPostActivityService.getSellerProperties(
                        ((SellerProfile) currentUserProfile).getUserAccountId()
                );
                model.addAttribute("propertyPost", sellerProperties);
            }
        }
        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addProperties(Model model) {

        model.addAttribute("propertyPostActivity", new PropertyPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-properties";
    }

    @PostMapping("/dashboard/addNew")
    public String addNew(PropertyPostActivity propertyPostActivity, Model model) {
        Users user = usersService.getCurrentUser();

        if (user != null) {
            propertyPostActivity.setPostedById(user);
        }
        propertyPostActivity.setPostedDate(new Date());
        model.addAttribute("propertyPostActivity", propertyPostActivity);
        propertyPostActivityService.addNew(propertyPostActivity);
        return "redirect:/dashboard/";
    }

    @PostMapping("dashboard/edit/{id}")
    public String editProperty(@PathVariable("id") int id, Model model) {
        PropertyPostActivity propertyPostActivity = propertyPostActivityService.getOne(id);
        model.addAttribute("propertyPostActivity", propertyPostActivity);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-properties";
    }

}
