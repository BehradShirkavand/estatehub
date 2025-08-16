package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.*;
import com.behrad.estatehub.service.*;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BuyerApplyController {

    private final PropertyPostActivityService propertyPostActivityService;
    private final UsersService usersService;
    private final BuyerApplyService buyerApplyService;
    private final BuyerSaveService buyerSaveService;
    private final SellerProfileService sellerProfileService;
    private final BuyerProfileService buyerProfileService;


    @GetMapping("property-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        PropertyPostActivity propertyDetails = propertyPostActivityService.getOne(id);

        List<BuyerApply> buyerApplyList = buyerApplyService.getPropertyCandidates(propertyDetails);
        List<BuyerSave> buyerSaveList = buyerSaveService.getPropertyCandidates(propertyDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Seller"))) {
                SellerProfile user = sellerProfileService.getCurrentSellerProfile();
                if (user != null) {
                    model.addAttribute("applyList", buyerApplyList);
                }
            } else {
                BuyerProfile user = buyerProfileService.getCurrentBuyerProfile();
                if (user != null) {
                    boolean exists = false;
                    boolean saved = false;

                    for (BuyerApply buyerApply: buyerApplyList) {
                        if (buyerApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            exists = true;
                            break;
                        }
                    }
                    for (BuyerSave buyerSave: buyerSaveList) {
                        if (buyerSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
                            saved = true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied", exists);
                    model.addAttribute("alreadySaved", saved);
                }
            }

        }
        BuyerApply buyerApply = new BuyerApply();
        model.addAttribute("applyProperty", buyerApply);

        model.addAttribute("propertyDetails", propertyDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "property-details";
    }

    @PostMapping("property-details/apply/{id}")
    public String apply(@PathVariable int id, BuyerApply buyerApply) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<BuyerProfile> buyerProfile = buyerProfileService.getOne(user.getUserId());
            PropertyPostActivity propertyPostActivity = propertyPostActivityService.getOne(id);

            if (buyerProfile.isPresent() && propertyPostActivity != null) {
                buyerApply = new BuyerApply();
                buyerApply.setUserId(buyerProfile.get());
                buyerApply.setProperty(propertyPostActivity);
                buyerApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found");
            }
            buyerApplyService.addNew(buyerApply);
        }
        return "redirect:/dashboard/";

    }
}
