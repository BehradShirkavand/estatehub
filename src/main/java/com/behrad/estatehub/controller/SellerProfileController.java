package com.behrad.estatehub.controller;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.UsersRepository;
import com.behrad.estatehub.service.BuyerProfileService;
import com.behrad.estatehub.service.SellerProfileService;
import com.behrad.estatehub.service.UsersService;
import com.behrad.estatehub.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/seller-profile")
public class SellerProfileController {

    private final UsersService usersService;
    private final SellerProfileService sellerProfileService;
    private final BuyerProfileService buyerProfileService;

    @GetMapping("/")
    public String sellerProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();

            Users user = usersService.findByEmail(currentUsername);

            Optional<SellerProfile> sellerProfile = sellerProfileService.getOne(user.getUserId());

            sellerProfile.ifPresent(profile -> model.addAttribute("profile", profile));

        }
        return "seller_profile";
    }

    @PostMapping("addNew")
    public String addNew(SellerProfile sellerProfile, @RequestParam("image")MultipartFile multipartFile, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();

            Users users = usersService.findByEmail(currentUsername);

            sellerProfile.setUserId(users);
            sellerProfile.setUserAccountId(users.getUserId());
        }
        model.addAttribute("profile", sellerProfile);

        String fileName = "";
        if (!Objects.equals(multipartFile.getOriginalFilename(), "")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            sellerProfile.setProfilePhoto(fileName);
        }
        SellerProfile savedUser = sellerProfileService.addNew(sellerProfile);

        String uploadDir = "photos/seller/" + savedUser.getUserAccountId();

        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/dashboard/";
    }
}
