package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.PropertyPostActivity;
import com.behrad.estatehub.service.PropertyPostActivityService;
import com.behrad.estatehub.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BuyerApplyController {

    private final PropertyPostActivityService propertyPostActivityService;
    private final UsersService usersService;

    @GetMapping("property-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        PropertyPostActivity propertyDetails = propertyPostActivityService.getOne(id);

        model.addAttribute("propertyDetails", propertyDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());

        return "property-details";
    }
}
