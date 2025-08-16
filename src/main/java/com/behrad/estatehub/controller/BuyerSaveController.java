package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.BuyerSave;
import com.behrad.estatehub.entity.PropertyPostActivity;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.service.BuyerProfileService;
import com.behrad.estatehub.service.BuyerSaveService;
import com.behrad.estatehub.service.PropertyPostActivityService;
import com.behrad.estatehub.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BuyerSaveController {

    private final UsersService usersService;
    private final BuyerProfileService buyerProfileService;
    private final PropertyPostActivityService propertyPostActivityService;
    private final BuyerSaveService buyerSaveService;

    @PostMapping("property-details/save/{id}")
    public String save(@PathVariable("id") int id, BuyerSave buyerSave) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<BuyerProfile> buyerProfile = buyerProfileService.getOne(user.getUserId());
            PropertyPostActivity propertyPostActivity = propertyPostActivityService.getOne(id);

            if (buyerProfile.isPresent() && propertyPostActivity != null) {
                buyerSave = new BuyerSave();
                buyerSave.setProperty(propertyPostActivity);
                buyerSave.setUserId(buyerProfile.get());
            } else {
                throw new RuntimeException("User not found");
            }
            buyerSaveService.addNew(buyerSave);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-properties/")
    public String savedProperties(Model model) {
        List<PropertyPostActivity> propertyPost = new ArrayList<>();
        Object currentUserProfile  = usersService.getCurrentUserProfile();
        List<BuyerSave> buyerSaveList = buyerSaveService.getCandidatesProperties((BuyerProfile) currentUserProfile);

        for (BuyerSave buyerSave: buyerSaveList) {
            propertyPost.add(buyerSave.getProperty());
        }
        model.addAttribute("propertyPost", propertyPost);
        model.addAttribute("user", currentUserProfile);

        return "saved-properties";
    }
}
