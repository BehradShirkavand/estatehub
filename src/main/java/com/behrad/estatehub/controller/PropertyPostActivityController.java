package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.*;
import com.behrad.estatehub.service.BuyerApplyService;
import com.behrad.estatehub.service.BuyerSaveService;
import com.behrad.estatehub.service.PropertyPostActivityService;
import com.behrad.estatehub.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class PropertyPostActivityController {

    private final UsersService usersService;
    private final PropertyPostActivityService propertyPostActivityService;
    private final BuyerApplyService buyerApplyService;
    private final BuyerSaveService buyerSaveService;

    @GetMapping("/dashboard/")
    public String searchProperty(
            Model model,
            @RequestParam(value = "property", required = false) String property,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "apartment", required = false) String apartment,
            @RequestParam(value = "villa", required = false) String villa,
            @RequestParam(value = "land", required = false) String land,
            @RequestParam(value = "shop", required = false) String shop,
            @RequestParam(value = "office", required = false) String office,
            @RequestParam(value = "farm", required = false) String farm,
            @RequestParam(value = "house", required = false) String house,
            @RequestParam(value = "warehouse", required = false) String warehouse,
            @RequestParam(value = "sale", required = false) String sale,
            @RequestParam(value = "rent", required = false) String rent,
            @RequestParam(value = "today", required = false) boolean today,
            @RequestParam(value = "day7", required = false) boolean day7,
            @RequestParam(value = "day30", required = false) boolean day30
    ) {

        model.addAttribute("apartment", Objects.equals(apartment, "Apartment"));
        model.addAttribute("villa", Objects.equals(villa, "Villa"));
        model.addAttribute("land", Objects.equals(land, "Land"));
        model.addAttribute("shop", Objects.equals(shop, "Shop"));
        model.addAttribute("office", Objects.equals(office, "Office"));
        model.addAttribute("farm", Objects.equals(farm, "Farm"));
        model.addAttribute("house", Objects.equals(house, "House"));
        model.addAttribute("warehouse", Objects.equals(warehouse, "Warehouse"));


        model.addAttribute("sale", Objects.equals(sale, "Sale"));
        model.addAttribute("rent", Objects.equals(rent, "Rent"));

        model.addAttribute("today", today);
        model.addAttribute("day7", day7);
        model.addAttribute("day30", day30);

        model.addAttribute("property", property);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<PropertyPostActivity> propertyPost = null;
        boolean dateSearchFlag = true;
        boolean propertyType = true;
        boolean listingType = true;

        if (day30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (day7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today){
            searchDate = LocalDate.now();
        } else {
            dateSearchFlag = false;
        }

        if (apartment==null && villa==null && land==null && shop==null && office==null && farm==null && house==null && warehouse==null) {
            apartment = "Apartment";
            villa = "Villa";
            land = "Land";
            shop = "Shop";
            office = "Office";
            farm = "Farm";
            house = "House";
            warehouse = "Warehouse";
            propertyType = false;
        }

        if (sale==null && rent==null) {
            sale = "Sale";
            rent = "Rent";
            listingType = false;
        }

        if (!dateSearchFlag && !propertyType && !listingType && !StringUtils.hasText(property) && !StringUtils.hasText(location)) {
            propertyPost = propertyPostActivityService.getAll();
        } else {
            propertyPost = propertyPostActivityService.search(property, location,
                    Arrays.asList(apartment, villa, land, shop, office, farm, house, warehouse),
                    Arrays.asList(sale, rent), searchDate);
        }


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
            } else {
                List<BuyerApply> buyerApplyList = buyerApplyService.getCandidatesProperties((BuyerProfile) currentUserProfile);
                List<BuyerSave> buyerSaveList = buyerSaveService.getCandidatesProperties((BuyerProfile) currentUserProfile);

                boolean exist;
                boolean saved;

                for (PropertyPostActivity propertyActivity : propertyPost) {
                    exist = false;
                    saved = false;
                    for (BuyerApply buyerApply : buyerApplyList) {
                        if (Objects.equals(propertyActivity.getPropertyPostId(), buyerApply.getProperty().getPropertyPostId())) {
                            propertyActivity.setIsActive(true);
                            exist = true;
                            break;
                        }
                    }

                    for (BuyerSave buyerSave: buyerSaveList) {
                        if (Objects.equals(propertyActivity.getPropertyPostId(), buyerSave.getProperty().getPropertyPostId())) {
                            propertyActivity.setIsSaved(true);
                            saved = true;
                            break;
                        }
                    }

                    if (!exist) {
                        propertyActivity.setIsActive(false);
                    }
                    if (!saved) {
                        propertyActivity.setIsSaved(false);
                    }

                    model.addAttribute("propertyPost", propertyPost);
                }

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
